package com.lsocket.module;

/**
 * Created by Administrator on 2017/4/5.
 */
public enum SocketSystemCode {
    /** 新连接数量超过最大值 */
    NEW_CONNECTS_TOO_MORE,
    /** 当前ip连接数超过最大值 */
    IP_CONNECTS_TOO_MORE,
    /** 总连接数已超最大值 */
    CONNECTIONS_TOO_MORE,
    /** 数据异常，请稍候再试 */
    SERVER_BUSY

}
