/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com;

import com.lgame.util.comm.RandomTool;
import com.lgame.util.json.JsonTool;
import com.lgame.util.time.DateTimeTool;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An TCP server used for performance tests.
 * 
 * It does nothing fancy, except receiving the messages, and counting the number of
 * received messages.
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class TcpServer extends IoHandlerAdapter {
    /** The listening port (check that it's not already in use) */
    public static final int PORT = 18567;

    /** The number of message to receive */
    public static final int MAX_RECEIVED = 100;

    /** The starting point, set when we receive the first message */
    private static long t0;

    /** A counter incremented for every recieved message */
    private AtomicInteger nbReceived = new AtomicInteger(0);

    /**
     * {@inheritDoc}
     */
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        session.closeNow();
    }
    public static volatile int num = 0;
    /**
     * {@inheritDoc}
     */
    @Override
    public void messageReceived(final IoSession session, final Object message) throws Exception {
        int nb = nbReceived.incrementAndGet();

        if (nb == 1) {
            t0 = System.currentTimeMillis();
        }

        if (nb == MAX_RECEIVED) {
            long t1 = System.currentTimeMillis();
            PrintOut.log("-------------> end " + (t1 - t0));
        }

        PrintOut.log("Received " + nb + " messages:");
        new Thread(new Runnable() {

            @Override
            public void run() {

                num++;
                if(num== 5){
                /*    PrintOut.log("=----sleepnum:"+num);
                    session.suspendWrite();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    PrintOut.log("=------------------------sleep weak:"+num);
                    session.resumeWrite();*/
                }else if(num== 3){
                    //   session.resumeRead();

                }
                // If we want to test the write operation, uncomment this line
                session.write(message);
            }
        }).start();



    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        PrintOut.log("Session closed...");

        // Reinitialize the counter and expose the number of received messages
        PrintOut.log("Nb message received : " + nbReceived.get());
        nbReceived.set(0);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionCreated(final IoSession session) throws Exception {
        PrintOut.log("Session created...");
        //if(num== 2){
      //  }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String str = "isClosing:"+session.isClosing()+"  isConnected:"+session.isConnected()+" "+ session.getId();
                    PrintOut.log(str);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        PrintOut.log("Session idle...");
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        final long ss = RandomTool.Next(99999);

        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintOut.log(ss+"messageSent=============sleep== ");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PrintOut.log(ss+"messageSent===============================>> ");
            }
        }).start();
    }

    /**
     * {@inheritDoc}
     * @param session the current seession
     * @throws Exception If something went wrong
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        PrintOut.log("Session Opened...");
    }

    /**
     * Create the TCP server
     * 
     * @throws IOException If something went wrong
     */
    public TcpServer() throws IOException {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setHandler(this);

        // The logger, if needed. Commented atm
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        //chain.addLast("logger", new LoggingFilter());
        chain.addLast("threadPool", new ExecutorFilter( new OrderedThreadPoolExecutor(10,
                10,
                5,
                TimeUnit.SECONDS)));
        acceptor.bind(new InetSocketAddress(PORT));

        PrintOut.log("Server started...");
    }

    /**
     * The entry point.
     * 
     * @param args The arguments
     * @throws IOException If something went wrong
     */
    public static void main(String[] args) throws IOException {
        new TcpServer();
    }
}
