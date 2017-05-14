package com.lsocket.handler;

import com.lsocket.listen.HandlerListen;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class ClientHandler extends IoHandlerAdapter {
    private HandlerListen handlerListen;
    public ClientHandler(HandlerListen handlerListen){
        this.handlerListen = handlerListen;
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        handlerListen.receiveMsg(message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
    }
}
