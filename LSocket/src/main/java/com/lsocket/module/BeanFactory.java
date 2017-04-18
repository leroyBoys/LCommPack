package com.lsocket.module;

import com.lsocket.core.SocketServer;
import org.apache.mina.core.session.IoSession;

/**
 * Created by Administrator on 2017/4/5.
 */
public interface BeanFactory<V extends Visitor> {
    public V createVistor(IoSession session, SocketServer socketServer, long timeOutTime);
}
