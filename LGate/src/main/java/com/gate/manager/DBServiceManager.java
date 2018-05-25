package com.gate.manager;

import com.gate.action.dao.mysql.ServerService;
import com.lgame.core.LQStart;
import com.lgame.entity.DBType;
import com.lgame.entity.LQConnConfig;
import com.lgame.entity.LQNewNode;
import com.lgame.module.*;
import com.lgame.mysql.impl.LQDataSource;
import com.lgame.util.comm.StringTool;
import com.lsocket.core.ICommon;

import java.util.List;
import java.util.Properties;

/**
 * Created by Administrator on 2017/4/14.
 */
public class DBServiceManager extends ICommon {
    private static DBServiceManager dbServiceManager = null;
    private Properties properties;
    private DBServiceManager(){
    }

    public synchronized static DBServiceManager getInstance(Properties properties){
        if(dbServiceManager != null){
            return dbServiceManager;
        }
        dbServiceManager = new DBServiceManager();
        dbServiceManager.properties = properties;
        return dbServiceManager;
    }

    private LQDataSource commUserPool;//用户中心数据连接池
    private LQDataSource commGamePool;//游戏数据连接池
    private GameServer gameServer;
    private ServerGroup serverGroup;
    private ServerService serverService;


    private LQConnConfig.LQDBConnConfig resetProper(Properties dbProper) {
        dbProper.setProperty("username",serverGroup.getSqlUserName());
        dbProper.setProperty("password",serverGroup.getSqlPwd());
        if(StringTool.isNotNull(dbProper.getProperty("jdbcUrl"))){
            dbProper.setProperty("jdbcUrl",serverGroup.getSqlUrl());
        }else {
            dbProper.setProperty("url",serverGroup.getSqlUrl());
        }
        return new LQConnConfig.LQDBConnConfig(serverGroup.getSqlUrl(),serverGroup.getSqlUserName(),serverGroup.getSqlPwd());
    }

    private void loadConfig(Properties properties) throws Exception {
        LQStart.scan("com.lgame");

        LQStart.init(properties);
        commUserPool = LQStart.getJdbcManager().getMaster();

        serverService = new ServerService(commUserPool);
        gameServer = serverService.getServerById(Integer.valueOf(properties.getProperty("server.id")));
        if(gameServer == null){
            throw new RuntimeException(properties.getProperty("server.id")+" cant find from db");
        }

        if(gameServer.getServerType() != GameServer.ServerType.gate){
            throw new RuntimeException(properties.getProperty("server.id")+" serverType:"+gameServer.getServerType());
        }
        serverGroup  = serverService.getServerGroup(gameServer.getGroupNum());
        if(serverGroup == null){
            throw new RuntimeException("can not find goupNum:"+gameServer.getGroupNum() + " in serverGroup db");
        }

        LQStart.addNewDataSource(DBType.db,new LQNewNode(null,"server"),resetProper(properties));
        commGamePool = LQStart.getJdbcManager().getMaster("server");
    }

    protected void initService(){
        try {
            loadConfig(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // RedisConnectionManager redisConnectionManager = new RedisConnectionManager(properties);
    }

    @Override
    protected void check() {
        List<ServerConnection> servers = serverService.getServerByGroup(serverGroup.getGroup());
        ServerManager.getIntance().init(servers);
    }


    public static DBServiceManager getDbServiceManager() {
        return dbServiceManager;
    }

    public LQDataSource getCommUserPool() {
        return commUserPool;
    }

    public LQDataSource getCommGamePool() {
        return commGamePool;
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    public ServerGroup getServerGroup() {
        return serverGroup;
    }

    public ServerService getServerService() {
        return serverService;
    }
}
