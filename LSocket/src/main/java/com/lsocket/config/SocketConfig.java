/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsocket.config;


import com.lgame.util.load.properties.Properties;

/**
 * Server配置文件变量
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class SocketConfig extends AutoConfig {
    @Properties(defaultValue = "Hikari", fileName = "server", keyName = "server.dbtype")
    private String dbType;
    @Properties(defaultValue = "127.0.0.1", fileName = "server", keyName = "server.id")
    private int serverId;
    @Properties(defaultValue = "12345", fileName = "server", keyName = "server.socket.default.key")
    private String serverDKey;
    @Properties(defaultValue = "false", fileName = "server", keyName = "server.socket.isGzip")
    private Boolean isGzip;
    @Properties(defaultValue = "false", fileName = "server", keyName = "server.socket.isAES")
    private Boolean isAES;
    @Properties(defaultValue = "10101", fileName = "server", keyName = "server.socket.port")
    private int socketPort;
    @Properties(defaultValue = "false", fileName = "server", keyName = "server.socket.tcp.nodelay")
    private boolean tcpNodelay;
    @Properties(defaultValue = "8", fileName = "server", keyName = "server.socket.pool.min")
    private int workerPoolMin;
    @Properties(defaultValue = "64", fileName = "server", keyName = "server.socket.pool.max")
    private int workerPoolMax;
    @Properties(defaultValue = "2", fileName = "server", keyName = "server.socket.both.idle")
    private int socketBothIdle;
    @Properties(defaultValue = "300", fileName = "server", keyName = "server.socket.pool.idle")
    private int workerPoolIdle;
    @Properties(defaultValue = "20480", fileName = "server", keyName = "server.socket.buffer.read")
    private int readBufferSize;
    @Properties(defaultValue = "20480", fileName = "server", keyName = "server.socket.buffer.write")
    private int writeBufferSize;
    @Properties(defaultValue = "5000", fileName = "server", keyName = "server.max.backlog")
    private int serverMaxBacklog;
    @Properties(defaultValue = "60", fileName = "server", keyName = "server.socket.write.timeout")
    private int socketWriteTimeout;
    @Properties(defaultValue = "16", fileName = "server", keyName = "server.socket.nioprocess")
    private int serverNioProcess;
    @Properties(defaultValue = "true", fileName = "server", keyName = "server.socket.isOpenBlack")
    private boolean isOpenBlack;
    private static final SocketConfig config = new SocketConfig();
    public static SocketConfig getInstance(){
        return config;
    }

    public String getServerDKey() {
        return serverDKey;
    }

    public Boolean getGzip() {
        return isGzip;
    }

    public Boolean getAES() {
        return isAES;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public boolean isTcpNodelay() {
        return tcpNodelay;
    }

    public int getWorkerPoolMin() {
        return workerPoolMin;
    }

    public int getWorkerPoolMax() {
        return workerPoolMax;
    }

    public int getSocketBothIdle() {
        return socketBothIdle;
    }

    public int getServerId() {
        return serverId;
    }

    public String getDbType() {
        return dbType;

    }

    public static SocketConfig getConfig() {
        return config;
    }

    public int getWorkerPoolIdle() {
        return workerPoolIdle;
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public int getWriteBufferSize() {
        return writeBufferSize;
    }

    public int getServerMaxBacklog() {
        return serverMaxBacklog;
    }

    public int getSocketWriteTimeout() {
        return socketWriteTimeout;
    }

    public int getServerNioProcess() {
        return serverNioProcess;
    }

    public boolean isOpenBlack() {
        return isOpenBlack;
    }
}
