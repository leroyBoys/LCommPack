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

    /**
     * 登陆日志
     */
    LOGIN("LOGIN"),
    /**
     * 游戏币日志
     */
    SILVER("SILVER"),
    /**
     * 代币日志.
     *
     * 日志格式:
     */
    GOLDEN("GOLDEN"),
    /**
     * 物品日志
     * <pre>
     * 日志格式:
     *
     * </pre>
     */
    GOODS("GOODS"),
    /**
     * 角色经验日志
     *
     * 格式:
     * <pre>
     * </pre>
     */
    PLAYER_EXP("PLAYER_EXP"),
    /**
     * 角色等级日志
     *
     * 格式:
     * <pre>
     * time:当前服务器时间, playerId:角色ID,....
     * </pre>
     */
    PLAYER_LEVEL("PLAYER_LEVEL"),
    /**
     * 角色注册统计日志
     *
     * 格式:
     * <pre>
     * time:当前服务器时间,
     * info:
     * </pre>
     */
    PLAYER_REGISTER_STATISTIC("PLAYER_REGISTER_STATISTIC"),
    /**
     * 掉落日志
     *
     * 格式:
     * <pre>
     * time:当前服务器时间,source:来源, moneyOrient:货币的收支,playerId:拾取的角色ID,playerName:拾取的角色的名字,userName:账号,golden:拾取的金币数量,silver:拾取的游戏币数量, monsterName:怪物名字, info:物品详细信息
     * </pre>
     */
    DROP("DROP"),
    /**
     * 活动
     *
     */
    ACTIVE("ACTIVE"),
    /**
     * 副本
     */
    DUNGEON("DUNGEON"),;
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
