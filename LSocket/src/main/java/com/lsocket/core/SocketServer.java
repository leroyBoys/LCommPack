/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsocket.core;

import com.lsocket.codec.RequestDecoder;
import com.lsocket.codec.ResponseEncoder;
import com.lsocket.config.SocketConfig;
import com.lsocket.control.HandlerListen;
import com.lsocket.control.impl.CoreDispatcher;
import com.lsocket.handler.SocketHanlder;
import com.lsocket.manager.NewSessionManager;
import com.lsocket.module.BeanFactory;
import com.lsocket.module.CommonCodecFactory;
import com.lsocket.module.DefaultBeanFactory;
import com.lsocket.module.Visitor;
import com.lsocket.util.SocketConstant;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 *
 * @author leroy
 */
public final class SocketServer<V extends Visitor> {

    private static final Logger logger = LoggerFactory.getLogger(SocketConstant.logName);
    private SocketAcceptor acceptor;
    private InetSocketAddress address;
    private IoFilter cmdAttackFilter;
    private IoFilter byteAttackFilter;
    private ProtocolCodecFactory codecFactory;
    private int processorCount = Runtime.getRuntime().availableProcessors() + 1;
    private SocketConfig confg;
    private CoreDispatcher coreDispatcher;
    private NewSessionManager<V> newSessionManager = new NewSessionManager<>();
    private BeanFactory beanFactory = new DefaultBeanFactory();

    public SocketServer(ResponseEncoder encoder, RequestDecoder decoder, CoreDispatcher coreDispatcher, SocketConfig confg, BeanFactory beanFactory,HandlerListen handlerListen) {
        this.coreDispatcher = coreDispatcher;
        decoder.init(handlerListen);
        this.codecFactory = new CommonCodecFactory(encoder, decoder);
        this.processorCount = confg.getServerNioProcess();
        if(beanFactory != null){
            this.beanFactory = beanFactory;
        }
    }

    public SocketServer(ResponseEncoder encoder, RequestDecoder decoder, CoreDispatcher coreDispatcher, BeanFactory beanFactory,HandlerListen handlerListen) {
        this.coreDispatcher = coreDispatcher;
        decoder.init(handlerListen);
        this.codecFactory = new CommonCodecFactory(encoder, decoder);
        if(beanFactory != null){
            this.beanFactory = beanFactory;
        }
    }

/*

    public SocketServer(ProtocolCodecFactory protocolCodecFactory, IoHandler ioHandler, IoFilter byteAttackFilter, IoFilter cmdAttackFilter) {
     //   this(protocolCodecFactory, ioHandler);
        this.cmdAttackFilter = cmdAttackFilter;
        this.byteAttackFilter = byteAttackFilter;
    }
*/

    public void start()throws Exception {
        if (this.codecFactory == null) {
            throw new NullPointerException("ProtocolCodecFactory is null...");
        }
    /*    if (this.ioHandler == null) {
            throw new NullPointerException("IoHandler is null...");
        }*/
        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());
        this.acceptor = new NioSocketAcceptor(processorCount);
        this.acceptor.setReuseAddress(true);
        this.acceptor.setBacklog(confg.getServerMaxBacklog());
        this.acceptor.getSessionConfig().setAll(getSessionConfig());
        MdcInjectionFilter mdcInjectionFilter = new MdcInjectionFilter();
        DefaultIoFilterChainBuilder filterChain = this.acceptor.getFilterChain();
        filterChain.addLast("mdcInjectionFilter", mdcInjectionFilter);
        if (this.byteAttackFilter != null) {
            filterChain.addLast("byteAttackFilter", this.byteAttackFilter);
        }
        if (this.cmdAttackFilter != null) {
            filterChain.addLast("cmdAttackFilter", this.cmdAttackFilter);
        }
        filterChain.addLast("codecFactory", new ProtocolCodecFilter(this.codecFactory));

        this.acceptor.setHandler(new SocketHanlder<>(this,confg.isOpenBlack()));
        this.address = new InetSocketAddress(confg.getSocketPort());
        this.acceptor.bind(this.address);
        logger.info("Listening on " + this.address.getHostName() + ":" + this.address.getPort());
    }

    public SocketSessionConfig getSessionConfig() {
        SocketSessionConfig sessionConfig = new DefaultSocketSessionConfig();
        sessionConfig.setSoLinger(0);
        sessionConfig.setKeepAlive(true);
        sessionConfig.setReuseAddress(true);
        sessionConfig.setTcpNoDelay(confg.isTcpNodelay());
        sessionConfig.setBothIdleTime(confg.getSocketBothIdle());
        sessionConfig.setReadBufferSize(confg.getReadBufferSize());
        sessionConfig.setSendBufferSize(confg.getWriteBufferSize());
        sessionConfig.setWriteTimeout(confg.getSocketWriteTimeout());
        sessionConfig.setReceiveBufferSize(confg.getReadBufferSize());

        return sessionConfig;
    }

    public void stop() {
        if (this.acceptor != null) {
            this.acceptor.unbind();
            this.acceptor.dispose();
            this.acceptor = null;
        }
       /* if (FILTER_EXECUTOR != null) {
            FILTER_EXECUTOR.shutdown();
            try {
                FILTER_EXECUTOR.awaitTermination(5000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                logger.error("停服抛出了异常", e);
            }
        }*/
    }

    public ProtocolCodecFactory getProtocolCodecFactory() {
        return this.codecFactory;
    }

    public void setProtocolCodecFactory(ProtocolCodecFactory protocolCodecFactory) {
        this.codecFactory = protocolCodecFactory;
    }

/*    public IoHandler getIoHandler() {
        return this.ioHandler;
    }

    public void setIoHandler(IoHandler ioHandler) {
        this.ioHandler = ioHandler;
    }*/

    public IoFilter getFloodByteAttackFilter() {
        return this.byteAttackFilter;
    }

    public void setFloodByteAttackFilter(IoFilter floodByteAttackFilter) {
        this.byteAttackFilter = floodByteAttackFilter;
    }

    public IoFilter getFloodCmdAttackFilter() {
        return this.cmdAttackFilter;
    }

    public NewSessionManager<V> getNewSessionManager() {
        return newSessionManager;
    }

    public void setFloodCmdAttackFilter(IoFilter floodCmdAttackFilter) {
        this.cmdAttackFilter = floodCmdAttackFilter;
    }

    public CoreDispatcher getCoreDispatcher() {
        return coreDispatcher;
    }

    public BeanFactory<V> getBeanFactory() {
        return beanFactory;
    }

    public void setConfg(SocketConfig confg) {
        this.confg = confg;
    }
}
