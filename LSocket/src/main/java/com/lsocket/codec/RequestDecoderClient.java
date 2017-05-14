package com.lsocket.codec;

import com.lsocket.core.ClientServer;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public abstract class RequestDecoderClient extends CumulativeProtocolDecoder {
    private ClientServer clientServer;

    public ClientServer getClientServer() {
        return clientServer;
    }

    public void setClientServer(ClientServer clientServer) {
        this.clientServer = clientServer;
    }
}
