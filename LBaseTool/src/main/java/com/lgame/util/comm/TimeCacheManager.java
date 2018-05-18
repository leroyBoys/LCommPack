package com.lgame.util.comm;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/15.
 */
public class TimeCacheManager {
    private final static TimeCacheManager timerManager = new TimeCacheManager();
    private long curTime;

    private TimeCacheManager(){}
    public static TimeCacheManager getInstance(){
        return timerManager;
    }

    public void setCurTime(long time){
        this.curTime = time;
    }

    public long getCurTime(){
        return curTime;
    }

    public long getCurTimeNoCache(){
        return System.currentTimeMillis();
    }
}
