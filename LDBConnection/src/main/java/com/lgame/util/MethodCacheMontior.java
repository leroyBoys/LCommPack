package com.lgame.util;

/**
 * Created by Administrator on 2017/5/15.
 */
public class MethodCacheMontior implements StatisticsMonitor{
    @Override
    public MethodCacheTime start() {
        return new MethodCacheTime();
    }

    @Override
    public void end(MethodCacheTime methodCacheTime, String methodName) {
        methodCacheTime.dbTrace(methodName);
    }
}
