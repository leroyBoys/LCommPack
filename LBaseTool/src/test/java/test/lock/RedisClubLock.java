package test.lock;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * serverGroup,version均为服务器启动唯一表示
 * maxTime：当等待多久之后去redis取出value核对数据
 * 支持tryLock一次性获得锁：不堵塞,如果获得锁需要释放
 * lock 堵塞可重入 获得锁需要释放
 */
public final class RedisClubLock extends ReentrantLock{
    final static int serverGroup=2;
    static String version="aljdsjfoj";
    static {
        version = UUID.randomUUID().toString();
    }

    private AtomicInteger waitCount = new AtomicInteger(0);
    final private String key;
    private volatile int lockCount = 0;//锁的深度
    private long lastFailLockTime;
    final static int maxTime = 30*1000;//30秒检测一次
    public RedisClubLock(String key){
        this.key = key;
    }

    public final static RedisConnection lqRedisConnection = new RedisConnection("redis://0@192.168.101.131:6378/123456");

    private boolean lockRedisKey(){
        try{
            String server = System.currentTimeMillis()/1000+","+serverGroup+","+version;
            return "OK".equalsIgnoreCase(lqRedisConnection.set(key,server,"NX","EX",300));
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    private void delKey(){
        try{
            lqRedisConnection.del(key);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getValue(){
        try{
            return lqRedisConnection.get(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void hset(String key,String field,String value){
        try{
            lqRedisConnection.hset(key,field,value);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     *
     * @return 返回是否获得锁
     */
    private boolean recordSlowBlock(){
            String v = getValue();
            if(v == null){
                return true;
            }

            try{
                String[] array = v.split(",");
                long startTime = System.currentTimeMillis()/1000 -Long.valueOf(array[0]) - 60*60*8;//一个分布式任务占用八个小时直接删除这个锁定
                if(startTime >0){
                    delKey();
                    return true;
                }

                if(serverGroup - Integer.valueOf(array[1]) == 0){
                    if(!array[2].equals(version)){
                        delKey();
                        return true;
                    }
                }else {
                    //记录异常锁队列
                    hset("checkLockQueue",key,"1");
                }

            }catch (Exception e){
                e.printStackTrace();
                delKey();
                return true;
            }
            return  false;
    }

    private boolean isTimeOut(){
        return System.currentTimeMillis()-lastFailLockTime-maxTime>0;
    }

    @Override
    public boolean tryLock() {
        final boolean suc = super.tryLock();
        System.out.println("--申请锁"+suc);
        if(!suc){
            return false;
        }
        waitCount.incrementAndGet();

        if(lockCount==0 && !lockRedisKey()){
            if(lastFailLockTime == 0){
                lastFailLockTime = System.currentTimeMillis();
            }else if(isTimeOut()&&recordSlowBlock()&&lockRedisKey()){
                lockSuc();
                System.out.println("- try getLock timeout--redis  获得锁："+key);
                return true;
            }
            this.superUnlock();
            return false;
        }

        lockSuc();
        System.out.println("- try getLock--redis  获得锁："+key);
        return true;
    }

    private void lockSuc(){
        lockCount++;
        lastFailLockTime = 0;
        System.out.println("锁 suc："+key);
    }

    @Override
    public void lock() {
        waitCount.incrementAndGet();
        super.lock();

        if(lockCount>0 ){
            lockSuc();
            System.out.println(lockCount+"---redis  获得锁(重入锁)："+key);
            return;
        }
        lastFailLockTime = System.currentTimeMillis();
        while (true){
            if(lockRedisKey()){
                lockSuc();
                System.out.println("---redis  获得锁："+key);
                return;
            }
            try {
              //  System.out.println("---redis --等待获得锁："+key);
                Thread.sleep(10);

                if(isTimeOut()){
                    recordSlowBlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void unlock() {
        if(lockCount >0){
            if(--lockCount == 0){
                delKey();
            }
            superUnlock();
        }
    }

    void superUnlock(){
        if(waitCount.decrementAndGet() <= 0){
            LockManager.finsh(key);
        }
        super.unlock();
    }
}
