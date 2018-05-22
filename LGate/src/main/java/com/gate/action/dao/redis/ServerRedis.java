package com.gate.action.dao.redis;


import com.lgame.redis.impl.RedisConnectionManager;

/**
 * Created by Administrator on 2017/4/15.
 */
public class ServerRedis{
    protected RedisConnectionManager redisConnectionManager;

    public ServerRedis(RedisConnectionManager redisConnectionManager){
        this.redisConnectionManager = redisConnectionManager;
    }


}
