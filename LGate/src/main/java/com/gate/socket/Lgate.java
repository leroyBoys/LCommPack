package com.gate.socket;

import com.gate.codec.RequestDecoderRemote;
import com.gate.codec.ResponseEncoderRemote;
import com.gate.control.CoreDispatcherRmote;
import com.gate.listen.GateHeartLinste;
import com.gate.manager.CoreServiceManager;
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
import com.lsocket.module.ModuleDispaterInstance;
import com.lsocket.module.Visitor;
import org.apache.mina.core.session.IoSession;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class Lgate extends SocketServer<Visitor>{
    public final static Lgate lgate = new Lgate();

    private Lgate(){
        super(new CoreDispatcherRmote());
       // DBServiceManager.getInstance(PropertiesTool.loadProperty("server.properties")).load();
        //CoreServiceManager.getIntance().load();
    }

    public static Lgate getIntance(){
        return lgate;
    }

    @Override
    public Visitor createVistor(IoSession session, long timeOutTime) {
        return new GateVisitor(this,session,timeOutTime);
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

    @Override
    public ModuleDispaterInstance getInnerModuleDispaterConfig() {
        ModuleDispaterInstance ins = new ModuleDispaterInstance();
        List<ModuleDispaterInstance.Obj> objs = new LinkedList<>();
        objs.add(new ModuleDispaterInstance.Obj("com.gate.action.handler"));
        ins.setObjList(objs);
        return ins;
    }

}
