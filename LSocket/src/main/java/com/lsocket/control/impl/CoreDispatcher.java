package com.lsocket.control.impl;

import com.logger.log.SystemLogger;
import com.lsocket.control.Dispatcher;
import com.lsocket.core.SocketServer;
import com.lsocket.handler.CmdModule;
import com.lsocket.handler.ModuleHandler;
import com.lsocket.manager.CMDManager;
import com.lsocket.message.Request;
import com.lsocket.message.Response;
import com.lsocket.module.SocketSystemCode;
import com.lsocket.module.Visitor;
import com.lsocket.util.SocketConstant;
import org.apache.mina.core.session.IdleStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/4.
 */
public abstract class CoreDispatcher<V extends Visitor,M extends Request> implements Dispatcher<V,M> {
    protected final Map<Integer, ModuleHandler> moduleHandlers = new HashMap(5);

    public Response createResponse(M requset){
        return Response.defaultResponse(requset.getM_cmd(),requset.getSeq());
    }

    @Override
    public final void dispatch(V vistor, M request) {

        try {
            Response response = createResponse(request);
            CMDManager.getIntance().getCmdModule(request.getM_cmd()).invoke(vistor,request,response);
        }catch (Exception ex){
            SystemLogger.error(this.getClass(),ex);
            vistor.sendError(SocketSystemCode.CONNECTIONS_TOO_MORE);
        }

    }

    //注册模块到调度器中
    public void put(int moduleKey, ModuleHandler BaseHandler) {
        if (BaseHandler != null) {
            if (moduleHandlers.containsKey(moduleKey)) {
                throw new RuntimeException(String.format("Error: duplicated key [%d]", new Object[]{moduleKey}));
            }
            moduleHandlers.put(moduleKey, BaseHandler);
        }
    }

    public ModuleHandler getBaseHandler(int moduleKey) {
        return moduleHandlers.get(moduleKey);
    }

    public void session_closed(SocketServer socketServer, V v){
        socketServer.getNewSessionManager().doTriger();
        socketServer.getNewSessionManager().closeFromIP(v);
        session_closed(v);
    }

    public abstract void session_closed(V v);

    public abstract void sessionIdle(V v, IdleStatus idleStatus);
}
