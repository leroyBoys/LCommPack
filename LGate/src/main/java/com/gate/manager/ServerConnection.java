package com.gate.manager;

import com.gate.codec.RequestDecoderLocal;
import com.gate.codec.ResponseEncoderLocal;
import com.logger.log.SystemLogger;
import com.lsocket.control.HandlerListen;
import com.lsocket.core.ClientServer;
import com.lsocket.util.DefaultSocketPackage;
import org.apache.mina.core.session.IoSession;

import java.io.IOException;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class ServerConnection implements Runnable {
    private int id;
    private String name;
    private String ip;
    private int port;
    private volatile ServerStatus serverStatus = ServerStatus.closed;
    private int heartPerTime = 1000;//心跳间隔毫秒
    private final static int timeOutTime = 5*60*1000;//超时时间

    private ServerMonitor serverMonitor = new ServerMonitor();
    private ClientServer clientServer;
    private volatile boolean isRun = false;

    public ServerConnection(int id,String name,String ip,int port){
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.port = port;
        clientServer = new ClientServer(ip,port,2000,new ResponseEncoderLocal(),new RequestDecoderLocal(),serverMonitor);
    }

    public void check(long curTime){
        if(isRun){
            return;
        }
        isRun = true;
        checkConnected();
        heart(curTime);
    }

    private void checkConnected(){
        if(serverStatus != ServerStatus.closed){
            return;
        }

        reConnect();
    }

    private void reConnect(){
        new Thread(this).run();
    }


    private void heart(long curTime){
        long dif = curTime - serverMonitor.lastHeartTime;
        if(serverStatus == ServerStatus.closed || dif < heartPerTime){
            return;
        }
        try {
            if(dif > timeOutTime){
                reConnect();
                return;
            }

            send(DefaultSocketPackage.transformHeartMsg());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isRun = false;
        }
    }

    public int getId() {
        return id;
    }

    public void send(Object obj){
        serverMonitor.session.write(obj);
    }

    private int errorNum = 0;

    @Override
    public void run() {
        try {
            if(clientServer.getSession() != null){
                clientServer.getSession().closeNow();
            }

            clientServer.start();
            if(clientServer.getSession() != null){
                serverMonitor.session = clientServer.getSession();
                serverStatus = ServerStatus.notFull;
                SystemLogger.info(this.getClass(),"ip:"+ip+" port:"+port+" connected suc!");
            }else {
                errorNum++;
                if(errorNum/20 == 1){
                    SystemLogger.info(this.getClass(),"ip:"+ip+" port:"+port+" connected fail!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isRun = false;
        }
    }

    class ServerMonitor implements HandlerListen{
        protected long lastHeartTime = 0;
        protected int connections;
        protected IoSession session = new ServerSession();

        protected void init(){
        }

        protected void reset(){
            connections = 0;
            lastHeartTime = 0;
            session = new ServerSession();
        }

        @Override
        public void sendSuc() {
        }

        @Override
        public void receiveMsg() {
            lastHeartTime = TimeCacheManager.getInstance().getCurTime();
        }

        @Override
        public boolean checkHeart() {
            return false;
        }
    }

    enum ServerStatus{
        closed,notFull,full
    }
}
