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

    public abstract int getModule();

    protected abstract void inititialize();

    public abstract CoreDispatcher getDispatcher();

    public ModuleHandler(){
        int mod = getModule();
        this.getDispatcher().put(mod, this);
        inititialize();
    }

    //注册指令和执行体到模块中
    public void putInvoker(CmdModule invoker) {
        CMDManager.getIntance().put(this.getModule(),invoker.getModuleCmd().getValue(),invoker);
    }

    public void session_ide(V vister, IdleStatus idleStatus) {
    }

    public void session_closed(V vister) {
    }
}
