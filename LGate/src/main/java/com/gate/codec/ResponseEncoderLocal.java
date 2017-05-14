package com.gate.codec;

import com.lsocket.codec.ResponseEncoder;
import com.lsocket.codec.ResponseEncoderClient;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class ResponseEncoderLocal extends ResponseEncoderClient {
    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {

    }
}
