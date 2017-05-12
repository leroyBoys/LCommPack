package com.lsocket.core;

import com.lsocket.control.impl.CoreDispatcher;
import com.lsocket.handler.SocketHanlder;
import com.lsocket.module.Visitor;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2017/5/12.
 */
public abstract class UdpServer<V extends Visitor> extends SocketServer<V> {
    private NioDatagramAcceptor acceptor;

    public UdpServer(CoreDispatcher coreDispatcher) {
        super(coreDispatcher);
    }

    public void start(int port)
            throws Exception {
        if (this.codecFactory == null) {
            throw new NullPointerException("ProtocolCodecFactory is null...");
        }
        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());
        this.acceptor = new NioDatagramAcceptor();
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

        this.acceptor.setHandler(new SocketHanlder(this,confg.isOpenBlack()));
        this.address = new InetSocketAddress(port);
        this.acceptor.bind(this.address);
        this.initModuleHanderConfig();
        started();
        logger.info("udp -Listening on " + this.address.getHostName() + ":" + port);
    }

    public void stop() {
        if (this.acceptor != null) {
            this.acceptor.unbind();
            this.acceptor.dispose();
            this.acceptor = null;
        }
    }
}
