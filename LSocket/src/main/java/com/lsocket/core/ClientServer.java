package com.lsocket.core;

import com.lsocket.listen.HandlerListen;
import com.lsocket.handler.ClientHandler;
import com.lsocket.module.CommonCodecFactory;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class ClientServer {
    private int port;
    private String host;
    private SocketConnector connector;
    private IoSession session;
    private long CONNECT_TIMEOUT = 30 * 1000L;

    public ClientServer(String host, int port, long timeout , ProtocolEncoder encoder, ProtocolDecoder decoder,HandlerListen handlerListen) {
        this.host = host;
        this.port = port;
        this.CONNECT_TIMEOUT = timeout;
        connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getSessionConfig().setReadBufferSize(20480);
        connector.getSessionConfig().setSendBufferSize(20480);

        connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new CommonCodecFactory(encoder, decoder)));
        connector.getFilterChain().addLast("threadPool",
                new ExecutorFilter(Executors.newCachedThreadPool()));

        connector.setHandler(new ClientHandler(handlerListen));
        connector.setDefaultRemoteAddress(new InetSocketAddress(host, port));
    }

    public IoSession getSession() {
        return session;
    }

    public void start() {
        ConnectFuture connectFuture = connector.connect();
        connectFuture.awaitUninterruptibly(CONNECT_TIMEOUT);
        try {
            session = connectFuture.getSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
