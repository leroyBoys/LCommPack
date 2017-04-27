package com.gate.manager;

import com.gate.util.TaskScheduler;
import com.lsocket.core.ICommon;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/4/14.
 */
public class CoreServiceManager extends ICommon {
    private final static CoreServiceManager obj = new CoreServiceManager();
    private CoreServiceManager(){}
    public static CoreServiceManager getIntance(){
        return obj;
    }

    private TaskScheduler taskScheduler;

    @Override
    protected void initService() {
        this.taskScheduler = new TaskScheduler(1);

        taskScheduler.scheduleAtFixedRate(ServerManager.getIntance(), 20, 20, TimeUnit.SECONDS);
    }

    @Override
    protected void check() {

    }
}
