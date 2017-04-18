package com.lsocket.manager;

import com.lsocket.module.Visitor;
import org.apache.mina.util.ConcurrentHashSet;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Administrator on 2017/4/3.
 */
public class AddressManager {
    private static AddressManager addressManager = new AddressManager();
    private final ConcurrentHashSet<String> whitelist = new ConcurrentHashSet<>();//白名单表示在测试期间（不对外开放时候）允许访问的ip地址
    private final ConcurrentHashSet<String> blacklist = new ConcurrentHashSet<>();

    private AddressManager(){}

    public static AddressManager getIntstance(){
        return addressManager;
    }

    public void initWhiteAndBlank(CopyOnWriteArraySet<String> whites, CopyOnWriteArraySet<String> blanks) {
        if (whites == null) {
            whites = new CopyOnWriteArraySet<>();
            whites.add("127.0.0.1");
        }
        whitelist.addAll(whites);
        if (blanks == null) {
            blanks = new CopyOnWriteArraySet<>();
        }
        blacklist.addAll(blanks);
    }

    public void addBlack(Visitor session, String info) {
        if (session == null) {
            throw new IllegalArgumentException("session to unblock can not be null");
        }
        blacklist.add(session.getIp().getIp());
    }

//    public void addBlack(String ip) {
//        blacklist.add(ip);
//    }

    /**
     * Unblocks the specified endpoint.
     */
    public void unBlack(String ip) {
        blacklist.remove(ip);
    }

    public boolean isBlack(Visitor session) {
        return blacklist.contains(session.getIp().getIp());
    }

    public boolean isWhite(Visitor session) {
        return whitelist.contains(session.getIp().getIp());
    }
}
