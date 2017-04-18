package com.lsocket.util;

import com.lsocket.module.IP;
import org.apache.mina.core.session.IoSession;

import java.net.SocketAddress;

/**
 * Created by Administrator on 2017/4/3.
 */
public final class SocketConstant {
    public final static String logName = "lsocket.log";
    /** 新连接空闲最大时间(无操作（毫秒)） */
    public static long NewSessionIdleTime = 10000;

    /**
     * 最大数据包长度
     */
    public static int maxSocketLenght = 1024 * 5;

    private static IP localIP;
    /** 每个玩家最大的请求处理数量，超过次数量则拒绝新请求直到此队列减少足够 */
    public static int maxQuqueVistor = 100;

    /** 同一个ip最大链接数 */
    public static int sameIpMaxConnections = 100;

    private static IP getIP(SocketAddress address) {
        String ipStr = address.toString().replaceAll("\\/", "");
        String first = ipStr.split(":")[0];
        String second = ipStr.split(":")[1];
        IP ip = new IP();
        ip.setIp(first);
        ip.setPort(Integer.valueOf(second));
        return ip;
    }

    public static IP getLocalIp(IoSession session) {
        return localIP;
    }

    public static void setLocalIP(IP lIp){
        localIP = lIp;
    }

    public static IP getClientp(IoSession session) {
        if(session == null){
            return null;
        }
        return getIP(session.getRemoteAddress());
    }

    public enum SessionKey{
        vistorKey,
        vistorControler;
    }
}
