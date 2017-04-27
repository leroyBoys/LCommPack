package com.lsocket.core;

/**
 * Created by Administrator on 2017/4/27.
 */
public abstract class ICommon {
    public void load(){
        initService();
        check();
    }
    protected abstract void initService();
    protected abstract void check();
}
