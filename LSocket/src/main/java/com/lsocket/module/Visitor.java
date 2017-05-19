package com.lsocket.module;

import com.logger.log.SystemLogger;
import com.lsocket.core.SocketServer;
import com.lsocket.message.ErrorCode;
import com.lsocket.message.Request;
import com.lsocket.message.Response;
import com.lsocket.util.SocketConstant;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.util.ConcurrentHashSet;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Administrator on 2017/4/3.
 */
public abstract class Visitor<Req extends Request,Res extends Response,E extends ErrorCode> implements Runnable{
    private SocketServer socketServer;
    private volatile boolean run = false;
    private LinkedBlockingDeque<Req> receivePools = new LinkedBlockingDeque<>();
    private ConcurrentHashSet<String> requests = new ConcurrentHashSet<>();

    private IoSession ioSession;
    private long timeOutTime;
    private Status status = Status.New;
    private IP ip;
    private int uid;
    private int serverId;
    private boolean isSever = false;//是否是服务器

    public Visitor(SocketServer socketServer,IoSession ioSession,long timeOutTime){
        this.socketServer = socketServer;
        this.ioSession = ioSession;
        this.timeOutTime = timeOutTime;
        ip = SocketConstant.getClientp(ioSession);
    }

    public void receiveMsg(Req m){
        String cmdUrl = m.getUrlTag();
        if(cmdUrl != null){
            if(requests.contains(cmdUrl)){
                return;
            }
        }
        this.checkLimit();
        receivePools.add(m);

        if(cmdUrl != null){
            requests.add(cmdUrl);
        }
        trigger();
    }

    private void checkLimit(){
        if(receivePools.size() >= SocketConstant.maxQuqueVistor){
            if(!this.getIoSession().isReadSuspended()){
                this.getIoSession().suspendRead();
            }
        }else if(this.getIoSession().isReadSuspended()){
            this.getIoSession().resumeRead();
        }
    }

    public void trigger(){
        if(receivePools.isEmpty() || run){
            return;
        }
        run = true;
        new Thread(this).start();
    }

    @Override
   public final void run() {

        try {
            Iterator<Req> items = receivePools.iterator();
            Req temV;
            int count = 10;
            while (items.hasNext()){
                if(count-- <= 0){
                    break;
                }
                temV = items.next();
                items.remove();
                socketServer.getCoreDispatcher().dispatch(this,temV);
            }

        }catch (Exception e){
            SystemLogger.error(Visitor.class,e);
            e.printStackTrace();
        }
        run = false;
    }

    public abstract void sendError(SocketSystemCode code);

    public abstract void sendMsg(Res sendMsg);

    public abstract void sendError(E code);

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public IoSession getIoSession() {
        return ioSession;
    }

    public void setIoSession(IoSession ioSession) {
        this.ioSession = ioSession;
    }

    public Status getStatus() {
        return status;
    }

    public IP getIp() {
        return ip;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isTimeOut(long curTime) {
        if(isSever){
            return false;
        }

        return curTime >= timeOutTime;
    }

    public int getServerId() {
        return serverId;
    }

    public void setSever(boolean sever) {
        isSever = sever;
    }

    public void setIp(String ip, int port) {
        this.ip = new IP(ip,0);
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    /**
     * 是否应该强制关闭(如果是登录应该设置错误登录次数)
     * @return
     */
    public boolean isShouldClose(long curTime) {
        return !getIoSession().isConnected() || isTimeOut(curTime);
    }

    public enum Status{
        New,Logining,Logined,Destroy
    }
}
