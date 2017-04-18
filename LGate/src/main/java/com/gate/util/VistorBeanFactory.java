package com.gate.util;

import com.lsocket.core.SocketServer;
import com.lsocket.module.BeanFactory;
import org.apache.mina.core.session.IoSession;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class VistorBeanFactory implements BeanFactory<GateVisitor> {
    public GateVisitor createVistor(IoSession session, SocketServer socketServer, long timeOutTime) {
        return new GateVisitor(socketServer,session,timeOutTime);
    }
}
