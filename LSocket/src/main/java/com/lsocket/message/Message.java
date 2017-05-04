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
    /**  module+cmd */
    private int m_cmd;
    private byte[] value;
    private Object obj;
    private int seq = 0;
    private Map<Object,Object> attributeMap = new HashMap<>();

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
       
    }

    public int getM_cmd() {
        return m_cmd;
    }

    public void setM_cmd(int m_cmd) {
        this.m_cmd = m_cmd;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
//    public String toString() {
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
