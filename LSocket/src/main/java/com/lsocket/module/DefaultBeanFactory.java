package com.lsocket.module;

import com.lsocket.core.SocketServer;
import com.lsocket.message.ErrorCode;
import com.lsocket.message.Response;
import org.apache.mina.core.session.IoSession;

/**
 * Created by Administrator on 2017/4/5.
 */
public class DefaultBeanFactory implements BeanFactory<Visitor>{
    @Override
    public Visitor createVistor(IoSession session, SocketServer socketServer,long timeOutTime) {
        return new Visitor(socketServer, session, timeOutTime) {
            @Override
            public void sendError(SocketSystemCode code) {
            }

            @Override
            public void sendMsg(Response sendMsg) {

            }

            @Override
            public void sendError(ErrorCode code) {

            }
        };
    }
}
