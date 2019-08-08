package test.lock;

import com.lgame.util.time.DateTimeTool;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static test.lock.RedisClubLock.lqRedisConnection;

public class Test {
    private static String testAbc= new String("ab")+"c";
    private String testA = "abc";
    private static AtomicInteger count = new AtomicInteger(0);
    static ExecutorService pool = Executors.newFixedThreadPool(20);
    public static void main(String[] args) throws Exception {
        for(int i = 0;i<10;i++){
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    go22();
                }
            });
        }
        pool.shutdown();
        while (!pool.isTerminated()){
            //Thread.sleep(1);
        }
        System.out.println("---------------------------------"+count.get());
    }

    public static void go(){
        String name = Thread.currentThread().getName();
        System.out.println("==***********=in  "+name);
        RedisClubLock lock = LockManager.getLock("abcs2");
        lock.lock();
        try{
            int i = new Random().nextInt(10);
            System.out.println("==***********=in"+name+"  "+i+ "sleeep");
            Thread.sleep(1000);
            System.out.println("==***********=in"+name+"  "+i+ "sleeep over");
            if(i<2){
                return;
            }
            go();
        }catch (Exception e){}finally {
            lock.unlock();
        }
    }

    public static void go2(){
        RedisClubLock lock = LockManager.getLock("abcs");
        String name = Thread.currentThread().getName();
        System.out.println("===in  "+name);
        lock.lock();
        try{
            int i = new Random().nextInt(10);
            System.out.println("================="+name+" "+i + "sleep");
            Thread.sleep(1000);

            System.out.println("================="+name+" "+i + "over");
            if(i<2){
                return;
            }
            go2();
        }catch (Exception e){}finally {
            lock.unlock();
        }
    }

    public static void go3(){
        RedisClubLock lock = LockManager.getLock("abcs");
        String name = Thread.currentThread().getName();
        System.out.println("==try=in  "+name);
        if(!lock.tryLock()){
            return;
        }
        try{
            int i = new Random().nextInt(10);
            System.out.println("=======try=========="+name+" "+i + "sleep");
            Thread.sleep(1000);

            System.out.println("========try========="+name+" "+i + "over");
            if(i<2){
                return;
            }
            go3();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public static void go22(){
        RedisClubLock lock = LockManager.getLock("abcs");
        long tim = 200;
        try {
            Thread.sleep(new Random().nextInt(10)+tim);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String name = Thread.currentThread().getName();
        lock.lock();
        try{
            int i = new Random().nextInt(10);
            System.out.println(DateTimeTool.getDateTime(new Date())+"================="+name+" "+i + "sleep");
            Thread.sleep(tim);
            count.addAndGet(1);
            String str = lqRedisConnection.get("nums");
            if(str == null){
                str = "0";
            }
            lqRedisConnection.set("nums",Integer.valueOf(str
            )+1+"");

            System.out.println(DateTimeTool.getDateTime(new Date())+"================="+name+" "+i + "over");
            if(i<2){
                return;
            }
            go22();
        }catch (Exception e){}finally {
            lock.unlock();
        }
    }
}
