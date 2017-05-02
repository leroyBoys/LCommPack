package com.gate.action;

import com.gate.socket.Lgate;
import com.lsocket.control.impl.CoreDispatcher;
import com.lsocket.handler.ModuleHandler;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/27.
 */
public class LoginGateModuleHandler extends ModuleHandler {

    @Override
    protected Integer getModule() {
        return null;
    }

    @Override
    protected void inititialize() {

    }

    @Override
    protected CoreDispatcher getDispatcher() {
        return Lgate.getIntance().getCoreDispatcher();
    }
}
