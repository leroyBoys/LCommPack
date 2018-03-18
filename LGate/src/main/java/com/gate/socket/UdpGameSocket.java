package com.gate.socket;

import com.gate.codec.RequestDecoderRemote;
import com.gate.codec.ResponseEncoderRemote;
import com.gate.control.CoreDispatcherRmote;
import com.gate.util.GateVisitor;
import com.lsocket.codec.RequestDecoder;
import com.lsocket.codec.ResponseEncoder;
import com.lsocket.config.SocketConfig;
import com.lsocket.core.UdpServer;
import com.lsocket.listen.HeartListen;
import com.lsocket.module.ModuleDispaterInstance;
import org.apache.mina.core.session.IoSession;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class UdpGameSocket extends UdpServer<GateVisitor> {
    public final static UdpGameSocket gameSocket = new UdpGameSocket();

    private UdpGameSocket(){
        super(new CoreDispatcherRmote());
    }

    public static UdpGameSocket getIntance(){
        return gameSocket;
    }

    @Override
    public GateVisitor createVistor(IoSession session, long timeOutTime) {
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
    protected void initModuleHanderConfig() {
    }

    @Override
    public HeartListen initHeartListen() {
        return null;
    }

    @Override
    public SocketConfig initConfig() {
        return SocketConfig.getInstance();
    }

    @Override
    public ModuleDispaterInstance getInnerModuleDispaterConfig() {
        return null;
    }

}
