package test;

import com.lgame.util.comm.RandomTool;
import com.lgame.util.thread.TaskIndieThread;
import com.lgame.util.thread.TaskPools;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/3/31.
 */
public class ThreadTestDemo {
    public static Vector<Integer> numList = new Vector<>();
    public static LinkedBlockingDeque<Integer>  sdf = new LinkedBlockingDeque<> ();
    public static AtomicInteger _num = new AtomicInteger(0);
    public static  int threads = 2;

    public static long startTime = 0;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

        runTest(threads, new MainThread() {
            @Override
            public void run(final Integer id) {
                    if(RandomTool.getRandom().nextInt(10) < 100){
                        System.out.println("size:"+sdf.size());
                        Iterator<Integer> ites = sdf.iterator();
                        while (ites.hasNext()){
                            int rid = ites.next();
                            try{
                                ites.remove();
                                System.out.println(id+"remove:"+rid);
                                if(RandomTool.getRandom().nextBoolean()){
                                    break;
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                // System.out.println(id+"--------remove:"+e.getMessage());
                            }

                        }
                    }
                long ses = System.currentTimeMillis();
                sdf.addFirst(id);
                System.out.println(id);
                ses = System.currentTimeMillis() -ses;
                if(ses>2){
                    System.out.println("------------------------ses:"+ses);
                }
            }
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


       /* while (true){
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            System.out.println("..."+TaskPools.user_save_pools.getActiveCount());
           *//* if(TaskPools.linkedBlockingQueue.isEmpty()){
                break;
            }*//*
           if(TaskPools.user_save_pools.getActiveCount() == 0){
               break;
           }
        }*/

        System.out.println("----------->>>>>>>>shutdown");
      /* TaskPools.user_save_pools.shutdown();
       // while (!TaskPools.user_save_pools.isTerminated()) {
        while (TaskPools.user_save_pools.getActiveCount() != 0) {
            System.out.println(TaskPools.user_save_pools.getQueue().size()+"..."+TaskPools.user_save_pools.getActiveCount());
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("=list:"+numList.size()+"/"+(System.currentTimeMillis()-startTime)+" s");*/
    }

    private static void add_Num(){
        _num.incrementAndGet();

        if(threads == _num.get()){
            System.out.println((System.currentTimeMillis()-startTime) + " ms"+Math.ceil((System.currentTimeMillis()-startTime)/1000)+" s");
        }
    }

    public static void runTest(final int threads,final MainThread mainThread){
        final CountDownLatch latch = new CountDownLatch( threads );
        for ( int i = 0; i < threads; ++i )
        {
            final int id = i;
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.countDown();
                        latch.await();

                        System.out.println("==add:"+id);
                      /*  TaskPools.addTask(new TaskIndieThread() {
                            @Override
                            public void doExcute(Object... objects) {
                                mainThread.run(id);
                                add_Num();
                            }
                        });
*/
                        mainThread.run(id);
                        add_Num();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                 //   add_Num();
                }
            } );
            thread.start();

        }
    }


    interface MainThread{
        public void run(Integer id);
    }
}
