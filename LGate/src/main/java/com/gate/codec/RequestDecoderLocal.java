package com.gate.codec;

import com.lsocket.codec.RequestDecoderClient;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class RequestDecoderLocal extends RequestDecoderClient {

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        return false;
    }
}
