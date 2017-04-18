package com.gate.socket;

import com.gate.codec.RequestDecoderRemote;
import com.gate.codec.ResponseEncoderRemote;
import com.gate.config.GateConfig;
import com.gate.config.IConfigurator;
import com.gate.control.CoreDispatcherRmote;
import com.gate.manager.ServerManager;
import com.gate.util.TaskScheduler;
import com.gate.util.VistorBeanFactory;
import com.lsocket.control.HandlerListen;
import com.lsocket.core.SocketServer;
import com.lsocket.module.Visitor;

import java.util.concurrent.TimeUnit;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class Lgate {
    private TaskScheduler taskScheduler;
    private volatile boolean initialized;
    private volatile boolean started;
    private final SocketServer<Visitor> socketServer;
    private ServerManager serverManager;
    private IConfigurator gateConfig;
    private HandlerListen handlerListen;

    private Lgate(){
        this.initialized = false;
        this.started = false;
        this.gateConfig = new GateConfig();
        this.serverManager = ServerManager.getIntance();
        handlerListen = null;
        socketServer = new SocketServer(new ResponseEncoderRemote(),new RequestDecoderRemote(),new CoreDispatcherRmote(),new VistorBeanFactory(),handlerListen);
        this.taskScheduler = new TaskScheduler(1);
    }

    public void start() throws Exception {
        if(!this.initialized) {
            this.initialize();
        }
        gateConfig.loadConfiguration();
        configureServer();
        startServer();
    }

    private void initialize() {
    }

    private void configureServer() {
        this.taskScheduler.resizeThreadPool(gateConfig.getSocketConfig().getSchedulerThreadPoolSize());
        socketServer.setConfg(gateConfig.getSocketConfig());
    }

    private void startServer() throws Exception {
        if(started){
            return;
        }
        socketServer.start();

        taskScheduler.scheduleAtFixedRate(serverManager, 20, 20, TimeUnit.SECONDS);
        started = true;
    }
}
