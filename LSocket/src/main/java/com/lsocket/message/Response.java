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

    public static Response defaultResponse(int m_cmd, int sq, byte[] value) {
        Response response = new Response();
        response.setM_cmd(m_cmd);
        response.setValue(value);
        response.setSeq(sq);

        return response;
    }

    public static Response defaultResponse(int m_cmd, int sq, Object obj) {
        Response response = new Response();
        response.setM_cmd(m_cmd);
        response.setObj(obj);
        response.setSeq(sq);
        return response;
    }

    public static Response defaultResponse(int m_cmd, int sq) {
        Response response = new Response();
        response.setM_cmd(m_cmd);
        response.setSeq(sq);
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
}
