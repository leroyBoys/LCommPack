package com.lgame.redis.impl;

import com.lgame.entity.NodeManger;
import com.lgame.mysql.impl.JDBCInitCache;

import java.util.Properties;
import java.util.Random;

/**
 * Created by Administrator on 2017/4/15.
 */
public class RedisConnectionManager extends NodeManger<RedisConnectionImpl>{

    @Override
    protected RedisConnectionImpl initRedisConnection(JDBCInitCache jdbcInitCache, Properties config) {
        int timeOut = 5000;
        int maxTotal = 3000;
        int maxIdel = 1500;
        long maxWaitMillis = -1l;
        if(config.getProperty("timeOut") != null){
            timeOut = Integer.valueOf(config.getProperty("timeOut"));
        }

        if(config.getProperty("maxTotal") != null){
            maxTotal = Integer.valueOf(config.getProperty("maxTotal"));
        }

        if(config.getProperty("maxIdel") != null){
            maxIdel = Integer.valueOf(config.getProperty("maxIdel"));
        }

        if(config.getProperty("maxWaitMillis") != null){
            maxWaitMillis = Long.valueOf(config.getProperty("maxWaitMillis"));
        }
        return new RedisConnectionImpl(config.getProperty("url"),timeOut,maxTotal,maxIdel,maxWaitMillis);
    }

    @Override
    public RedisConnectionImpl[] createArray(int size) {
        return new RedisConnectionImpl[size];
    }
}
