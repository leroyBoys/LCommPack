package com.lgame.entity;

import com.lgame.mysql.impl.JDBCManager;
import com.lgame.redis.impl.RedisConnectionManager;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/25.
 */
public enum DBType {
    redis(new RedisConnectionManager()),db(new JDBCManager());
    private NodeManger nodeManger;
    DBType(NodeManger nodeManger){
        this.nodeManger = nodeManger;
    }

    public NodeManger getNodeManger() {
        return nodeManger;
    }
}
