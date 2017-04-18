package com.lsocket.control;

import com.lsocket.message.Request;
import com.lsocket.module.Visitor;

/**
 * Created by Administrator on 2017/4/3.
 */
public interface Dispatcher<V extends Visitor,Req extends Request> {
    public abstract void dispatch(V vistor, Req request);
}
