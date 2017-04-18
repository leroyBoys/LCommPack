package com;

import com.lsocket.manager.NewSessionManager;
import com.lsocket.module.Visitor;

import java.util.Vector;
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
    public static  int threads = 5000;

    public static long startTime = 0;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

       final NewSessionManager manager = new NewSessionManager();
        manager.startNow();

        runTest(threads, new MainThread() {
            @Override
            public void run(final Integer id) {
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

        if(threads<<1 == _num.get()){
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

                       //System.out.println("==add:"+id);
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
