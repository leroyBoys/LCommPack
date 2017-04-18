/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.thread;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 定时调度，同一时间同一线程当且只有一个在运行
 * @author leroy
 */
public abstract class TimerIndieThread {

    private long cur_time = 0;

    /**
     * 获得执行间隔时间单位秒 （如果小于0表示永不执行)
     *
     * @return
     */
    public abstract int getDeriodTime();

    public abstract void excute(Map map);

    /**
     * 是否执行
     *
     * @return
     */
    public boolean _go() {
        int deriodtime = getDeriodTime();
        if (deriodtime < 0) {
            return false;
        }
        long temp = System.currentTimeMillis();
        if (temp - cur_time >= deriodtime * 1000) {
            cur_time = temp;
            return true;
        }
        return false;
    }
    private AtomicBoolean isRun = new AtomicBoolean(false);

   public void run(Map map) {
   //     System.out.println(isRun + "===================33================   " + this.getClass().getSimpleName() + "执行一次" + DateUtil.getCurrentDateStr(DateUtil.C_TIME_PATTON_DEFAULT));
        if (isRun.get()) {
            ////打印TimerTaskPools 添加线程池处理能力过慢
       //     FileThreadPools.addThreadLog(FileThreadPools.ThreadLogType.timerTaskPoolThread, this.getClass().getSimpleName() + "线程处理速度过慢加大间隔时间或则忽略");
            return;
        }
       isRun.set(true);
        _run(map);
    }

    private void _run(Map map) {
        try {
            excute(map);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            isRun.set(false);
        }
    }

}
