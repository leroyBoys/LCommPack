package com.lgame.entity;

import java.util.Properties;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/24.
 */
class Node<T extends LQConntion> {
    private T master;
    private T[] slaves;
    private short max;
    private short cur;

    public void initProperties(NodeManger<T> nodeManger, Properties masterConfig, Properties... slavesConfig){
        master = nodeManger.initRedisConnection(masterConfig);
        if (slavesConfig == null || slavesConfig.length == 0) {
            slaves = nodeManger.createArray(1);
            slaves[0] = master;
            return;
        }

        slaves = nodeManger.createArray(slavesConfig.length);
        this.max = (short)(slavesConfig.length-1);
        this.cur = 0;
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

        synchronized (this){
            if(cur++==max){
                cur = 0;
            }
        }

        return slaves[cur];
    }

    public T[] getSlaves() {
        return slaves;
    }
}
