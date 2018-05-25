package com.lgame.entity;

import java.util.Properties;
import java.util.Random;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/24.
 */
class Node<T extends LQConntion> {
    private static Random random = new Random();
    private T master;
    private T[] slaves;

    public void initProperties(NodeManger<T> nodeManger, Properties masterConfig, Properties... slavesConfig){
        master = nodeManger.initRedisConnection(masterConfig);
        if (slavesConfig == null || slavesConfig.length == 0) {
            slaves = nodeManger.createArray(1);
            slaves[0] = master;
            return;
        }

        slaves = nodeManger.createArray(slavesConfig.length);
        for (int i = 0; i < slavesConfig.length; i++) {
            slaves[i] = nodeManger.initRedisConnection(slavesConfig[i]);
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
