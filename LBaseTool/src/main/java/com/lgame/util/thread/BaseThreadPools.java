/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.thread;

import java.util.concurrent.*;

/**
 *
 * @author leroy
 */
public class BaseThreadPools extends ThreadPoolExecutor {

    public BaseThreadPools() {
        super(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    public BaseThreadPools(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     *
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param handler 队列满 时候处理策略，默认抛出异常，也可以继续添加
     */
    public BaseThreadPools(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,handler);
    }

    /**
     * 关闭线程池（阻塞）直到全部线程结束，此时不能有新的线程添加
     * @param handler
     */
    public void close(RejectedExecutionHandler handler){
        if(handler != null){
            this.setRejectedExecutionHandler(handler);
        }
        this.shutdown();
        while (!TaskPools.user_save_pools.isTerminated()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 关闭线程池（阻塞）直到全部线程结束，此时不能有新的线程添加
     */
    public void close(){
        close(null);
    }

    public static BaseThreadPools createSinglePool() {
        return new BaseThreadPools(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }
}
