package com.redis.entity;

import com.mysql.entity.DBTable;
import com.redis.impl.RedisConnectionImpl;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/17.
 */
public abstract class RedisSerializer {
    public abstract void serializer(RedisConnectionImpl redisConnection,DBTable table,Object entity);
    public abstract <T> T mergeFrom(RedisConnectionImpl redisConnection,DBTable table,Class<T> instance,Object uniqueId);
}
