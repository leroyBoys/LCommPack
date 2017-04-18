package com.lsocket.codec;

import com.lsocket.control.HandlerListen;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class ResponseEncoder extends ProtocolEncoderAdapter {

    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {

    }
}
