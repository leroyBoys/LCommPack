package com.lgame.redis.impl;

import com.lgame.entity.Node;
import com.lgame.entity.NodeManger;

/**
 * Created by Administrator on 2017/4/15.
 */
public class RedisConnectionManager extends NodeManger<LQRedisConnection>{
    @Override
    protected Node<LQRedisConnection> intanceNode(boolean slowSlaveOn) {
        return new RedisNode(slowSlaveOn);
    }

}
