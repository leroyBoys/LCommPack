package com.lsocket.codec;

import com.logger.log.SystemLogger;
import com.lsocket.core.SocketServer;
import com.lsocket.util.DefaultSocketPackage;
import com.lsocket.util.ReceiveData;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.io.IOException;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public abstract class RequestDecoder extends CumulativeProtocolDecoder {
    protected SocketServer socketServer;
    public void init(SocketServer socketServer){
        this.socketServer = socketServer;
    }

    protected boolean doDecode(IoSession session, IoBuffer input, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        ReceiveData receiveData = DefaultSocketPackage.getContent(input, session);
        if (receiveData.isClose()) {
            session.closeNow();
            return false;//父类接收新数据
        }else if(receiveData.isHeart()){
            if(socketServer.getHeartListen() != null && !socketServer.getHeartListen().checkHeart(session)){//是否合法
                session.closeNow();
                return false;//父类接收新数据
            }else {
                session.write(DefaultSocketPackage.transformHeartMsg());
            }
            return receiveData.getNextType() == ReceiveData.NextType.Continue;
        }

        if (receiveData.getData() == null) {
            return false;//父类接收新数据
        }

        try {
            doRequeset(session,protocolDecoderOutput,receiveData);
        }catch (Exception e){
            e.printStackTrace();
            SystemLogger.error(this.getClass(),e);
            session.closeNow();
            return false;//父类接收新数据
        }
        return input.remaining() > 0;
    }

    public abstract void doRequeset(IoSession session, ProtocolDecoderOutput out,ReceiveData receiveData) throws IOException;

}
