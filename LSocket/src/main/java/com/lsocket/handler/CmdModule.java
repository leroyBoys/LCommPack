package com.lsocket.handler;

import com.lsocket.message.Request;
import com.lsocket.message.Response;
import com.lsocket.module.HttpRequestType;
import com.lsocket.module.Visitor;

/**
 * Created by Administrator on 2017/4/4.
 */
public abstract class CmdModule<V extends Visitor,Req extends Request,Res extends Response>{

    public abstract ModuleCmd getModuleCmd();

    public abstract void invoke(V vistor, Req request,Res response);

    public <V extends Visitor> void  sendMsg(V vistor, Res resesponse){
        vistor.sendMsg(resesponse);
    }

    public abstract Req getRequset(byte[] bytes,int module,int cmd,int sq) throws Exception;
}
