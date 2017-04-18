package com.lsocket.manager;

import com.lsocket.module.Visitor;
import com.lsocket.util.SocketConstant;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2017/4/3.
 */
public class NewSessionManager<V extends Visitor> implements Runnable {
    protected volatile boolean run = false;
    protected boolean trigger = true;
    protected LinkedBlockingDeque<V> newSession = new LinkedBlockingDeque<>();
    private final ReentrantLock ipLock = new ReentrantLock();
    protected LinkedHashMap<String,Integer> ipLimit = new LinkedHashMap<>(10);
    /** 特殊ip的连接数限制ip-count(0无限制) */
    protected LinkedHashMap<String,Integer> whiteIpLimit = new LinkedHashMap<>(10);

    public NewSessionManager(){
    }

    public final void startNow(){
        if(run){
            return;
        }
        run = true;
        new Thread(this).start();
    }

    public final boolean addSession(V v){
        if(!this.addIp(v)){
            return false;
        }
        newSession.add(v);
        doTriger();

        return true;
    }

    private String getIp(V v){
        return v.getIp().getIp();
    }

    protected boolean addIp(V v){
        String ip = getIp(v);

        ipLock.lock();
        try {
            Integer count =  ipLimit.get(ip);
            if(count == null){
                ipLimit.put(ip,1);
            }else{
                if(count >= SocketConstant.sameIpMaxConnections){
                    Integer specialCount = whiteIpLimit.get(ip);
                    if(specialCount == null || specialCount<=count){
                        //提示同一个ip链接过多
                        return false;
                    }
                }
                ipLimit.put(ip,count+1);
            }
            return true;
        } finally {
            ipLock.unlock();
        }
    }

    public void addWhiteIpLimit(String ip,int count){
        ipLock.lock();
        try {
            whiteIpLimit.put(ip,count);
        } finally {
            ipLock.unlock();
        }
    }

    public void closeFromIP(V v){
        String ip = getIp(v);
        ipLock.lock();
        try {
            Integer count = ipLimit.get(ip);
            if(count == null){
                return;
            }else if(count == 1){
                ipLimit.remove(ip);
            }else {
                ipLimit.put(ip,count-1);
            }
        } finally {
            ipLock.unlock();
        }
    }

    public final void doTriger(){
        synchronized (newSession){
            if(trigger){
                return;
            }
            trigger = true;
            newSession.notify();
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                synchronized (newSession){
                    if(newSession.isEmpty()){
                        newSession.wait();
                    }else if(trigger){
                        trigger = false;
                    }else {
                        newSession.wait(2000);
                    }
                }
                long curTime = System.currentTimeMillis();
                Iterator<V> items = newSession.iterator();
                V temV;
                while (items.hasNext()){
                    temV = items.next();
                    if(temV.isShouldClose(curTime)){
                        temV.getIoSession().closeNow();
                        items.remove();
                    }else if(temV.getStatus() == Visitor.Status.Logined){
                        items.remove();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    class Triger implements Runnable{

        @Override
        public void run() {

        }
    }
}
