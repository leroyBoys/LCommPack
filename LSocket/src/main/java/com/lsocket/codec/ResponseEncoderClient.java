package com.lsocket.codec;

import com.lsocket.core.ClientServer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class ResponseEncoderClient extends ProtocolEncoderAdapter {
    private ClientServer clientServer;

    public ClientServer getClientServer() {
        return clientServer;
    }

    public void setClientServer(ClientServer clientServer) {
        this.clientServer = clientServer;
    }

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {

    }
}
