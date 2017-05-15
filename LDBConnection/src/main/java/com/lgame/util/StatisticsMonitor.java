package com.lgame.util;

/**
 * Created by Administrator on 2017/5/15.
 */
public interface StatisticsMonitor {
    public MethodCacheTime start();
    public void end(MethodCacheTime methodCacheTime,String methodName);
}
