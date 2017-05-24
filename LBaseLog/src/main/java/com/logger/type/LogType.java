/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logger.type;

/**
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public enum LogType {
    Error("Error"),
    /**
     * 登陆日志
     */
    LOGIN("LOGIN"),
    /**
     * 游戏币日志
     */
    Card("card"),
    /**
     *
     *
     * 日志格式:
     */
    Play("play"),
    /**
     * 结算
     * <pre>
     * 日志格式:
     *
     * </pre>
     */
    Calculator("calculator");
    /**
     * 日志名
     */
    private String logName = "";

    /**
     * 日志类型
     *
     * @param logName	日志名
     */
    LogType(String name) {
        this.logName = name;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }
}
