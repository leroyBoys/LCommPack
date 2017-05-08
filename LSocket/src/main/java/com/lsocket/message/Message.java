/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsocket.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Message implements Serializable {
    private int module;
    private int cmd;
    private byte[] value;
    private com.google.protobuf.Message obj;
    private int seq = 0;
    private Map<Object,Object> attributeMap = new HashMap<>();

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public com.google.protobuf.Message getObj() {
        return obj;
    }

    public void setObj(com.google.protobuf.Message obj) {
        this.obj = obj;
       
    }

    public int getModule() {
        return module;
    }

    public int getCmd() {
        return cmd;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
//    public String toString() {

    public void setModule(int module) {
        this.module = module;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }
//        return "[module=" + this.module + ", cmd=" + this.cmd + ", messageType=" + this.messageType + ", sn=" + this.sn + ", value=" + this.value + "]";
//    }

    public void addAttribute(Object key,Object value){
        attributeMap.put(key,value);
    }

    public void removeAttribute(Object key){
        attributeMap.remove(key);
    }

    public Object getAttribute(Object key){
        return attributeMap.get(key);
    }
}
