package com.lsocket.listen;

import org.apache.mina.core.session.IoSession;

/**
 * Created by Administrator on 2017/4/25.
 */
public interface HeartListen {
    public boolean checkHeart(IoSession session);
}
