package com.lsocket.core;

import com.lsocket.module.Visitor;
import org.apache.mina.core.session.IoSession;

/**
 * Created by Administrator on 2017/4/3.
 */
public interface VistorFactory<V extends Visitor> {
    public V create(IoSession session);
}
