package com.lgame.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/24.
 */
public abstract class NodeManger<T> {
    private Node<T> node;
    private String tmpNodeName;
    private Map<String,Node<T>> nodeMap = new HashMap<>();

    public void initProperties(String nodeName,Properties masterConfig, Properties... slavesConfig) throws Exception {

        if(nodeName == null){
            if(node != null && this.tmpNodeName == null){
                return;
            }
            node = Node.class.newInstance();
            node.initProperties(this,masterConfig,slavesConfig);
            this.tmpNodeName = null;
            return;
        }

        Node<T> nodeData = nodeMap.get(nodeName);
        if(nodeData != null){
            return;
        }

        nodeData = Node.class.newInstance();
        nodeData.initProperties(this,masterConfig,slavesConfig);

        if(nodeMap.isEmpty()){
            node = nodeData;
            this.tmpNodeName = nodeName;
        }

        nodeMap.put(nodeName,nodeData);
    }

    protected abstract T initRedisConnection(Properties properties);

    public T getMaster() {
        return node.getMaster();
    }

    public T getMaster(String nodeName) {
        Node<T> nodeData = nodeMap.get(nodeName);
        if(nodeData == null){
            return null;
        }
        return nodeData.getMaster();
    }

    public T getRandomSlave() {
        return node.getRandomSlave();
    }

    public T getRandomSlave(String nodeName) {
        Node<T> nodeData = nodeMap.get(nodeName);
        if(nodeData == null){
            return null;
        }
        return nodeData.getRandomSlave();
    }

    public T[] getSlaves() {
        return node.getSlaves();
    }

    public T[] getSlaves(String nodeName) {
        Node<T> nodeData = nodeMap.get(nodeName);
        if(nodeData == null){
            return null;
        }
        return nodeData == null?null: nodeData.getSlaves();
    }

    public abstract T[] createArray(int size);
}
