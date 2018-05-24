package com.lgame.entity;

import com.lgame.mysql.impl.JDBCInitCache;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.Properties;
import java.util.Random;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/24.
 */
class Node<T> {
    private static Random random = new Random();
    private T master;
    private T[] slaves;

    public void initProperties(NodeManger<T> nodeManger,JDBCInitCache jdbcInitCache, Properties masterConfig, Properties... slavesConfig){
        master = nodeManger.initRedisConnection(jdbcInitCache,masterConfig);
        if (slavesConfig == null || slavesConfig.length == 0) {
            slaves = nodeManger.createArray(1);
            slaves[0] = master;
            return;
        }

        slaves = nodeManger.createArray(1);
        for (int i = 0; i < slavesConfig.length; i++) {
            slaves[i] = nodeManger.initRedisConnection(jdbcInitCache,slavesConfig[i]);
        }
        return;
    }


    public T getMaster() {
        return master;
    }

    public T getRandomSlave() {
        if(slaves.length == 1){
            return slaves[0];
        }

        return slaves[random.nextInt(slaves.length)];
    }

    public T[] getSlaves() {
        return slaves;
    }
}
