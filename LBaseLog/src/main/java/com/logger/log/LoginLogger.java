/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logger.log;

import com.logger.bases.DetailLog;
import com.logger.bases.LogItem;
import com.logger.type.LogType;

import java.util.Date;

/**
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class LoginLogger extends DetailLog {
        
        /**
	 * 角色登出日志
	 * 
	 * @param player			角色对象
	 * @param battle			角色战斗属性
	 * @param clientIp			登出的IP信息
	 * @param branching			分线号
	 */
	public static void logout(Object player, String clientIp, int branching) {
        Date loginTime = new Date();//登陆时间，登陆创建player时赋值;
        Date logoutTime = new Date();//登陆时间，登出创建player时赋值;
        DetailLog.log(LogType.LOGIN, LogItem.valueOf(IP, clientIp)
        //                ,
        //                LogItem.valueOf(LEVEL, battle.getLevel()),
        //                LogItem.valueOf(PLAYERID, player.getId()),
        //                LogItem.valueOf(SILVER, player.getSilver()),
        //                LogItem.valueOf(GOLDEN, player.getGolden()),
        //                LogItem.valueOf(PLAYER_NAME, player.getName()),
        //                LogItem.valueOf(LOGINCOUNT, player.getLoginCount()),
        //                LogItem.valueOf(SOURCE, Source.PLAYER_LOGOUT.getCode()),
        //                LogItem.valueOf(LOGINTIME, DateUtil.formatTime(loginTime)),
        //                LogItem.valueOf(LOGOUTTIME, DateUtil.formatTime(logoutTime)),
        //                LogItem.valueOf(LOGIN_NAME, player.getUserName()),
        //                LogItem.valueOf(CREATE_TIME, DateUtil.formatTime(player.getCreateTime()))
        );
    }
;
//    public static void login(Test player, String clientIp, int branching) {
//        Date loginTime = new Date();//登陆时间，登陆创建player时赋值;
//        DetailLog.log(LogType.LOGIN, LogItem.valueOf(IP, clientIp),
//                LogItem.valueOf(PLAYERID, player.getId()),
//                LogItem.valueOf(PLAYER_NAME, player.getName())
//        //									LogItem.valueOf(LEVEL, battle.getLevel()),
//        //									LogItem.valueOf(SILVER, player.getSilver()),
//        //									LogItem.valueOf(GOLDEN, player.getGolden()),
//        //									LogItem.valueOf(PLAYERID, player.getId()),
//        //									LogItem.valueOf(PLAYER_NAME, player.getName()),
//        //									LogItem.valueOf(LOGINCOUNT, player.getLoginCount()),
//        //									LogItem.valueOf(SOURCE, Source.PLAYER_LOGIN.getCode()),
//        //									LogItem.valueOf(LOGINTIME, DateUtil.formatTime(loginTime)),
//        //									LogItem.valueOf(LOGIN_NAME, player.getUserName()),
//        //									LogItem.valueOf(CREATE_TIME, DateUtil.formatTime(player.getCreateTime()))
//        );
//    }
//
//    ;
//        
//        /**
//	 * 角色登出日志
//	 * 
//	 * @param player			角色对象
//	 * @param battle			角色战斗属性
//	 * @param clientIp			登出的IP信息
//	 * @param branching			分线号
//	 */
//	public static void logout(Object player, String clientIp, int branching) {
//        Date loginTime = new Date();//登陆时间，登陆创建player时赋值;
//        Date logoutTime = new Date();//登陆时间，登出创建player时赋值;
//        DetailLog.log(LogType.LOGIN, LogItem.valueOf(IP, clientIp)
//        //                ,
//        //                LogItem.valueOf(LEVEL, battle.getLevel()),
//        //                LogItem.valueOf(PLAYERID, player.getId()),
//        //                LogItem.valueOf(SILVER, player.getSilver()),
//        //                LogItem.valueOf(GOLDEN, player.getGolden()),
//        //                LogItem.valueOf(PLAYER_NAME, player.getName()),
//        //                LogItem.valueOf(LOGINCOUNT, player.getLoginCount()),
//        //                LogItem.valueOf(SOURCE, Source.PLAYER_LOGOUT.getCode()),
//        //                LogItem.valueOf(LOGINTIME, DateUtil.formatTime(loginTime)),
//        //                LogItem.valueOf(LOGOUTTIME, DateUtil.formatTime(logoutTime)),
//        //                LogItem.valueOf(LOGIN_NAME, player.getUserName()),
//        //                LogItem.valueOf(CREATE_TIME, DateUtil.formatTime(player.getCreateTime()))
//        );
//    }
//;
}
