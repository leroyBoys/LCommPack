package com.redis.impl;

import com.redis.RedisConnection;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/11.
 */
public class RedisConnectionImpl extends RedisConnection {
    /**
     *
     * @param url:redis://user:pwd@119.254.166.136:6379/0
     * @param timeout
     * @param maxTotal
     * @param maxIdel
     */
    public RedisConnectionImpl(String url, int timeout, int maxTotal, int maxIdel){
        super(url,timeout,maxTotal,maxIdel);
    }

    /**
     * @param url:redis://user:pwd@119.254.166.136:6379/0
     */
    public RedisConnectionImpl(String url){
       super(url);
    }

    @Override
    public String get(String key) {
        return super.get(key);
    }

    @Override
    protected String set(String key, String v) {
        return super.set(key, v);
    }
}
