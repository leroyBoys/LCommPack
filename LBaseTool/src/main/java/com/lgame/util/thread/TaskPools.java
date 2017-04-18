/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * db有关的程独立线程池
 *
 * @author leroy
 */
public class TaskPools {
    public static final BaseThreadPools user_save_pools = new BaseThreadPools(10, 50,
            0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());//线程池

    public static void addTask(final TaskIndieThread st, final Object... objects) {
        user_save_pools.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    st.doExcute(objects);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(st.getClass().getSimpleName() + "--TaskPools->" +ex.getMessage());
                }
            }
        });
    }
}
