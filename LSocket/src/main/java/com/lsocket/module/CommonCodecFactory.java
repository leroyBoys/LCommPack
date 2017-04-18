package com.lsocket.module;

import com.logger.log.SystemLogger;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class CommonCodecFactory implements ProtocolCodecFactory {

    private ProtocolEncoder encoder;
    private ProtocolDecoder decoder;

    public CommonCodecFactory(ProtocolEncoder encoder, ProtocolDecoder decoder) {
        if (encoder == null) {
            SystemLogger.error(this.getClass(),"ProtocolEncoder is null!");
            throw new NullPointerException("ProtocolEncoder is null!");
        }
        if (decoder == null) {
            SystemLogger.error(this.getClass(),"ProtocolDecoder is null!");
            throw new NullPointerException("ProtocolDecoder is null!");
        }
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public ProtocolDecoder getDecoder(IoSession session)
            throws Exception {
        return this.decoder;
    }

    public ProtocolEncoder getEncoder(IoSession session)
            throws Exception {
        return this.encoder;
    }
}
