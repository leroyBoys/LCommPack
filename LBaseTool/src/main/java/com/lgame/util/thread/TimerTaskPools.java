/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.thread;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 定时调度线程池()
 *
 * @author leroy
 */
public class TimerTaskPools {
    private static TimerTaskPools timerTaskPools = new TimerTaskPools();

    private final BaseThreadPools user_save_pools = new BaseThreadPools(10, 10,
            0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());//线程池

    private final List<TimerIndieThread> timers = new LinkedList<>();//线程池

    private AtomicBoolean isRun = new AtomicBoolean(true);

    private TimerTaskPools(){
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(isRun.get()){
                    return;
                }
                try {
                    for (TimerIndieThread thred : timers) {
                        if (thred._go()) {
                            _addTask(thred, null);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }, 2000, 1000);//2秒后执行 每1秒执行一次
    }

    private void _addTask(final TimerIndieThread st, final Map map) {
        if(isRun.get()){
            return;
        }

          user_save_pools.execute(new Runnable() {
            @Override
            public void run() {
    //            System.out.println("this is :" + st.getClass().getSimpleName() + "-TimerTaskPools-->" + JsonUtil.getJsonFromBean(map));
                try {
                    st.run(map);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void addTask(TimerIndieThread st) {
        timers.add(st);
    }

    public void shutDown(){
        isRun.set(false);
        user_save_pools.close();
    }

    public static TimerTaskPools getInstance(){
        return timerTaskPools;
    }
}
