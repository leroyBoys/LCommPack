package com.lgame.util;

/**
 * Created by Administrator on 2017/5/15.
 */
public class EmptyMontior implements StatisticsMonitor{
    @Override
    public MethodCacheTime start() {
        return null;
    }
    @Override
    public void end(MethodCacheTime methodCacheTime, String methodName) {
    }
}