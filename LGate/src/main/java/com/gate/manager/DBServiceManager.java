package com.gate.manager;

import com.gate.action.dao.mysql.ServerService;
import com.mysql.impl.SqlPool;
import com.redis.RedisConnectionManager;

import java.util.List;
import java.util.Properties;

/**
 * Created by Administrator on 2017/4/14.
 */
public class DBServiceManager {
    private final static DBServiceManager dbServiceManager = new DBServiceManager();
    private DBServiceManager(){
    }

    public static DBServiceManager getInstance(){
        return dbServiceManager;
    }

    public void init(Properties properties){
        RedisConnectionManager redisConnectionManager = new RedisConnectionManager(properties);
        SqlPool sqlPool = new SqlPool();
        ServerService serverService = new ServerService(sqlPool);
        List<ServerConnection> servers = serverService.getServerByGroup((Integer) properties.get("server.group"));
        ServerManager.getIntance().init(servers);

    }
}
