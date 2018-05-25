package com.lgame.entity;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/25.
 */
public class LQNewNode {
    private String readNodeName;
    private String newNodeName;

    public LQNewNode(String readNodeName, String newNodeName) {
        this.readNodeName = readNodeName;
        this.newNodeName = newNodeName;
    }

    public String getReadNodeName() {
        return readNodeName;
    }

    public String getNewNodeName() {
        return newNodeName;
    }
}
