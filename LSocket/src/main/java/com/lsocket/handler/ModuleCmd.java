package com.lsocket.handler;

import com.lsocket.module.HttpRequestType;

/**
 * Created by leroy:656515489@qq.com
 * 2017/5/8.
 */
public interface ModuleCmd {
    public int getValue();
    public int getCmd_c();
    public boolean isRequireOnline();
    public HttpRequestType getRequetType();
}
