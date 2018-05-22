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
        master = getRedisConnection(masterConfig);
        if(slavesConfig==null || slavesConfig.length == 0){
            return;
        }

        slaves = new RedisConnectionImpl[slavesConfig.length];
        for(int i=0;i<slavesConfig.length;i++){
            slaves[i] = getRedisConnection(slavesConfig[i]);
        }
    }

    private RedisConnectionImpl getRedisConnection(Properties config){
        int timeOut = 5000;
        int maxTotal = 3000;
        int maxIdel = 1500;

        if(config.getProperty("timeOut") != null){
            timeOut = Integer.valueOf(config.getProperty("timeOut"));
        }

        if(config.getProperty("maxTotal") != null){
            maxTotal = Integer.valueOf(config.getProperty("maxTotal"));
        }

        if(config.getProperty("maxIdel") != null){
            maxIdel = Integer.valueOf(config.getProperty("maxIdel"));
        }
        return new RedisConnectionImpl(config.getProperty("url"),timeOut,maxTotal,maxIdel);
    }

    public RedisConnectionImpl getMaster() {
        return master;
    }

    public RedisConnectionImpl getRandomSlave() {
        if(slaves == null){
            return master;
        }

        if(slaves.length == 1){
            return slaves[0];
        }

        return slaves[random.nextInt(slaves.length)];
    }

    public RedisConnectionImpl[] getSlaves() {
        return slaves;
    }
}
