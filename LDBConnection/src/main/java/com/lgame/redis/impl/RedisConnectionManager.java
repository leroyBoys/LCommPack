package com.lgame.redis.impl;

import java.util.Properties;
import java.util.Random;

/**
 * Created by Administrator on 2017/4/15.
 */
public class RedisConnectionManager {
    private final Random random = new Random();
    private RedisConnectionImpl master;
    private RedisConnectionImpl[] slaves;

    public RedisConnectionManager(Properties masterConfig, Properties... slavesConfig){
        master = initRedisConnection(masterConfig);
        if(slavesConfig==null || slavesConfig.length == 0){
            slaves = new RedisConnectionImpl[1];
            slaves[0] = master;
            return;
        }

        slaves = new RedisConnectionImpl[slavesConfig.length];
        for(int i=0;i<slavesConfig.length;i++){
            slaves[i] = initRedisConnection(slavesConfig[i]);
        }
    }

    private RedisConnectionImpl initRedisConnection(Properties config){
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

    public RedisConnectionImpl getMaster() {
        return master;
    }

    public RedisConnectionImpl getRandomSlave() {
        if(slaves.length == 1){
            return slaves[0];
        }

        return slaves[random.nextInt(slaves.length)];
    }

    public RedisConnectionImpl[] getSlaves() {
        return slaves;
    }
}
