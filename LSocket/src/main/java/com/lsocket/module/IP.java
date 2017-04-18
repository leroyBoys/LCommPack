/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsocket.module;

/**
 *
 * @author leroy_boy
 */
public class IP {
    private String ip;
    private int port;

    public IP(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public IP() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAll() {
        return ip+":"+port;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj ==null || !(obj instanceof IP)){
            return false;
        }
        IP ip = (IP) obj;
        return this.ip != null && this.ip.equals(ip.ip) && this.port == ip.port;
    }

    @Override
    public int hashCode() {
       return 1;
    }

}
