package com.lsocket.handler;

import com.lsocket.control.impl.CoreDispatcher;
import com.lsocket.manager.CMDManager;
import com.lsocket.message.Request;
import com.lsocket.message.Response;
import com.lsocket.module.Visitor;
import org.apache.mina.core.session.IdleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/4/4.
 */
public abstract class ModuleHandler<V extends Visitor,Req extends Request,Res extends Response> {

    private static final Logger logger = LoggerFactory.getLogger(ModuleHandler.class);

    protected CoreDispatcher dispatcher;

    protected abstract Integer getModule();

    protected abstract void inititialize();
    private ModuleHandler(){}

    public ModuleHandler(CoreDispatcher dispatcher){
        this.dispatcher = dispatcher;
        int mod = getModule();
        this.dispatcher.put(mod, this);
        inititialize();
    }

    //注册指令和执行体到模块中
    public void putInvoker(int cmd, CmdModule invoker) {
        CMDManager.getIntance().put(this.getModule(),cmd,invoker);
    }

    public void session_ide(V vister, IdleStatus idleStatus) {
    }

    public void session_closed(V vister) {
    }
}
