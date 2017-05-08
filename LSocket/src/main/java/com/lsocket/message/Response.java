/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsocket.message;

import com.lsocket.handler.CmdModule;
import com.lsocket.module.Visitor;

import java.io.Serializable;

public class Response extends Message implements Serializable {
    private static final long serialVersionUID = 5211320125262708957L;
    private int status;

    private CmdModule cmdModule;

    public static Response defaultResponse(int status) {
        Response response = new Response();
        response.setStatus(status);
        return response;
    }

    public static Response defaultResponse(int module,int cmd, int sq, byte[] value) {
        Response response = defaultResponse(module,cmd,sq);
        response.setValue(value);
        return response;
    }

    public static Response defaultResponse(int module,int cmd, int sq, com.google.protobuf.Message obj) {
        Response response = defaultResponse(module,cmd,sq);
        response.setObj(obj);
        return response;
    }

    public static Response defaultResponse(int module,int cmd, int sq) {
        Response response = defaultResponse(module,cmd);
        response.setSeq(sq);
        return response;
    }

    public static Response defaultResponse(int module,int cmd) {
        Response response = new Response();
        response.setModule(module);
        response.setCmd(cmd);
        return response;
    }

    public void setCmdModule(CmdModule cmdModule) {
        this.cmdModule = cmdModule;
    }

    public Response setStatus(int status) {
        this.status = status;
        return this;
    }

    public void sendMsg(Visitor visitor){
        cmdModule.sendMsg(visitor,this);
    }

    public String toString() {
        return "Response" + super.toString();
    }

    public int getStatus() {
        return status;
    }
}
