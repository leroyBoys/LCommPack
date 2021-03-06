/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsocket.core;

import com.lgame.util.file.PropertiesTool;
import com.lgame.util.load.xml.XmlApi;
import com.lsocket.codec.RequestDecoder;
import com.lsocket.codec.ResponseEncoder;
import com.lsocket.config.SocketConfig;
import com.lsocket.control.impl.CoreDispatcher;
import com.lsocket.handler.SocketHanlder;
import com.lsocket.listen.HeartListen;
import com.lsocket.manager.NewSessionManager;
import com.lsocket.module.CommonCodecFactory;
import com.lsocket.module.ModuleDispaterInstance;
import com.lsocket.module.Visitor;
import com.lsocket.util.SocketConstant;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetSocketAddress;

/**
 *
 * @author leroy
 */
public abstract class SocketServer<V extends Visitor> {

    protected static final Logger logger = LoggerFactory.getLogger(SocketConstant.logName);
    private SocketAcceptor acceptor;
    protected InetSocketAddress address;
    protected IoFilter cmdAttackFilter;
    protected IoFilter byteAttackFilter;
    protected ProtocolCodecFactory codecFactory;
    protected int processorCount = Runtime.getRuntime().availableProcessors() + 1;
    protected SocketConfig confg;
    protected CoreDispatcher coreDispatcher;
    protected NewSessionManager<V> newSessionManager = new NewSessionManager<>();
    protected HeartListen heartListen;

    public SocketServer(CoreDispatcher coreDispatcher) {
        this.coreDispatcher = coreDispatcher;
        RequestDecoder decoder = initRequestDecoder();
        decoder.init(this);
        this.codecFactory = new CommonCodecFactory(initResponseEncoder(), decoder);
        this.confg = initConfig();
        this.heartListen = initHeartListen();
    }

    public abstract V createVistor(IoSession session, long timeOutTime);

    public abstract ResponseEncoder initResponseEncoder();

    public abstract RequestDecoder initRequestDecoder();

    public abstract HeartListen initHeartListen();

    public abstract SocketConfig initConfig();

    public void start(int port)throws Exception {
        if (this.codecFactory == null) {
            throw new NullPointerException("ProtocolCodecFactory is null...");
        }

        try{
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
            this.address = new InetSocketAddress(port);
            this.acceptor.bind(this.address);
            logger.info("Listening on " + this.address.getHostName() + ":" + port);

            this.initModuleHanderConfig();
            started();

        }finally {
            Runtime.getRuntime().addShutdownHook(
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            logger.info("server stop.....");
                            if (acceptor != null) {
                                acceptor.unbind();
                                acceptor.dispose();
                                acceptor = null;
                            }
                            stop();
                            logger.info("server stop over");
                        }
                    }, "SoketServerStop"));
        }
    }

    public void started(){}

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
      /*  if (FILTER_EXECUTOR != null) {
            FILTER_EXECUTOR.shutdown();
            try {
                FILTER_EXECUTOR.awaitTermination(5000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                logger.error("停服抛出了异常", e);
            }
        }*/
    }

    protected void initModuleHanderConfig(){
        ModuleDispaterInstance instances = this.getInnerModuleDispaterConfig();
        if(instances != null){
            instances.load();
        }

        File file = new File(PropertiesTool.getPropertiesPath("dispater.xml"));

        instances = XmlApi.readObjectFromXml(ModuleDispaterInstance.class, file.getAbsolutePath());
        instances.load();
    }

    public abstract ModuleDispaterInstance getInnerModuleDispaterConfig();

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

    public HeartListen getHeartListen() {
        return heartListen;
    }

    public CoreDispatcher getCoreDispatcher() {
        return coreDispatcher;
    }
}
