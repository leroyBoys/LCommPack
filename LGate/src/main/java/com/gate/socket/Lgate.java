package com.gate.socket;

import com.gate.codec.RequestDecoderRemote;
import com.gate.codec.ResponseEncoderRemote;
import com.gate.control.CoreDispatcherRmote;
import com.gate.listen.GateHeartLinste;
import com.gate.manager.DBServiceManager;
import com.gate.manager.ServerManager;
import com.gate.util.GateVisitor;
import com.gate.util.TaskScheduler;
import com.lgame.util.file.PropertiesTool;
import com.lsocket.codec.RequestDecoder;
import com.lsocket.codec.ResponseEncoder;
import com.lsocket.config.SocketConfig;
import com.lsocket.core.SocketServer;
import com.lsocket.listen.HeartListen;
import com.lsocket.module.Visitor;
import org.apache.mina.core.session.IoSession;

import java.util.concurrent.TimeUnit;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class Lgate extends SocketServer<Visitor>{
    private TaskScheduler taskScheduler;
    private ServerManager serverManager;

    private Lgate(){
        super(new CoreDispatcherRmote());
        this.serverManager = ServerManager.getIntance();
        this.taskScheduler = new TaskScheduler(1);
        DBServiceManager.getInstance().init(PropertiesTool.loadProperty("server.properties"));
    }

    @Override
    public Visitor createVistor(IoSession session, SocketServer socketServer, long timeOutTime) {
        return new GateVisitor(socketServer,session,timeOutTime);
    }

    @Override
    public ResponseEncoder initResponseEncoder() {
        return new ResponseEncoderRemote();
    }

    @Override
    public RequestDecoder initRequestDecoder() {
        return new RequestDecoderRemote();
    }

    @Override
    public HeartListen initHeartListen() {
        return new GateHeartLinste();
    }

    @Override
    public SocketConfig initConfig() {
        return SocketConfig.getInstance();
    }


    public void started() {
        taskScheduler.scheduleAtFixedRate(serverManager, 20, 20, TimeUnit.SECONDS);
    }
}
