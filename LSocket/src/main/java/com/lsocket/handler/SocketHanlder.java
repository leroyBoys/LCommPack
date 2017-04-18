package com.lsocket.handler;

import com.lsocket.core.SocketServer;
import com.lsocket.manager.AddressManager;
import com.lsocket.message.Request;
import com.lsocket.module.Visitor;
import com.lsocket.util.SocketConstant;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/4/3.
 */
public class SocketHanlder<V extends Visitor,Req extends Request> extends IoHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SocketHanlder.class);

    private SocketServer<V> socketServer;
    private boolean isOpenBlack = true;

    public SocketHanlder(SocketServer<V> socketServer,boolean isOpenBlack){
        this.socketServer = socketServer;
        this.isOpenBlack = isOpenBlack;
    }

    @Override
    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
        throwable.printStackTrace();
        ioSession.closeNow();
    }

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        logger.info("sessionCreated");

        long curTime = System.currentTimeMillis();
        V vistor = socketServer.getBeanFactory().createVistor(ioSession,socketServer,curTime+SocketConstant.NewSessionIdleTime);
        if(isOpenBlack && AddressManager.getIntstance().isBlack(vistor)){
            inputClosed(ioSession);
            return;
        }else if(!socketServer.getNewSessionManager().addSession(vistor)){
            inputClosed(ioSession);
            return;
        }

        ioSession.setAttribute(SocketConstant.SessionKey.vistorKey,vistor);
    }

    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {
        logger.info("sessionOpened");
    }

    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {
        logger.info("sessionClosed");
        socketServer.getCoreDispatcher().session_closed(socketServer,(V) ioSession.getAttribute(SocketConstant.SessionKey.vistorKey));
    }

    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {
        logger.info("sessionIdle");
        socketServer.getCoreDispatcher().sessionIdle((V) ioSession.getAttribute(SocketConstant.SessionKey.vistorKey),idleStatus);
    }

    @Override
    public void messageReceived(IoSession ioSession, Object o) throws Exception {
        logger.info("messageReceived:"+o.toString());
        V vistor = (V) ioSession.getAttribute(SocketConstant.SessionKey.vistorKey);
        if(Visitor.Status.New == vistor.getStatus()){
            vistor.setStatus(Visitor.Status.Logining);
            socketServer.getNewSessionManager().doTriger();
        }
        vistor.receiveMsg((Request) o);
    }

    @Override
    public void messageSent(IoSession ioSession, Object o) throws Exception {
        logger.info("messageSent");
    }
}
