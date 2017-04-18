package com.gate.config;

import com.lsocket.config.SocketConfig;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public interface IConfigurator {
    void loadConfiguration() throws Exception;
    public SocketConfigRemote getSocketConfig();
    public ServerSetting getServerSetting();
}
