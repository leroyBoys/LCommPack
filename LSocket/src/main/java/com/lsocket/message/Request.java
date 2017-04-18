/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsocket.message;

import java.io.Serializable;

/**
 *
 */
public class Request extends Message implements Serializable {
    private String urlTag;

    private static final long serialVersionUID = 9175311565869855991L;

    public static Request valueOf(int m_cmd) {
        Request request = new Request();
        request.setM_cmd(m_cmd);
        return request;
    }

    public String getUrlTag() {
        return urlTag;
    }

    public void setUrlTag(String urlTag) {
        this.urlTag = urlTag;
    }

    public String toString() {
        return "Request" + super.toString();
    }

}
