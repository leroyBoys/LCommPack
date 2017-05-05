package com.logger.log;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.logger.mongo.MongoDBManager;
import com.logger.type.LogType;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Administrator
 */
public class GameLog {

    public final static MongoDBManager mananger = MongoDBManager.getInstance("192.168.4.84:27017", "qwbbt");
    private static final Logger logger = LoggerFactory.getLogger(GameLog.class);

    private static String[] getExpands(String... expand) {
        if (expand == null || expand.length == 0) {
            return new String[]{};
        }
        return expand;
    }

    public static final ExecutorService user_save_pools = Executors.newCachedThreadPool();//线程池

    /**
     * 保存玩家行为日志
     *
     * @param type
     * @param userId
     * @param time
     * @param userName 玩家名称，可根据查询需要可以填空/null
     * @param expands
     */
    public static void saveUserActionLog(final LogType type, final int userId, final Date time, final String userName, final String... expands) {
        user_save_pools.execute(new Runnable() {
            @Override
            public void run() {
                BasicDBObject map = new BasicDBObject();
                map.append("userId", userId);
                mananger.append(type.name(), map);
            }
        });
    }

    public static void baseUserInfo(LogType type, int userId, int sex, Date loginTime, String userName, String... expands) {

        //TaskPools.addTask(LogMethod.class, "baseUserInfo", type.getName(), userId, sex, loginTime, userName, getExpands(expands));
    }

    public static void UserLevelUpdate(LogType type, int userId, int level, Date time, String userName, String... expands) {
        // TaskPools.addTask(LogMethod.class, "UserLevelUpdate", type.getName(), userId, level, time, userName, getExpands(expands));
    }

    public static void friendUpdate(LogType type, int userId, int frendAmount, Date registe_time, String... expands) {
        // TaskPools.addTask(LogMethod.class, "friendUpdate", type.getName(), userId, frendAmount, registe_time, getExpands(expands));
    }

    public static void registe(LogType type, int userId, String userName, String devId, Date registe_time, String ip, String... expands) {
        //  TaskPools.addTask(LogMethod.class, "registe", type.getName(), userId, userName, devId, registe_time, ip, getExpands(expands));
    }

    /**
     *
     * @param type login
     * @param userId
     * @param loginTime
     * @param desc
     * @param ip
     * @param expands
     */
    public static void login(LogType type, int userId, Date loginTime, String desc, String ip, String... expands) {
        //   TaskPools.addTask(LogMethod.class, "login", type.getName(), userId, loginTime, ip, getExpands(expands));
        logger.warn(type.name() + desc);
    }

    public static void loginOff(LogType type, int userId, Date loginTime, String desc, String... expands) {
        //  TaskPools.addTask(LogMethod.class, "loginOff", type.getName(), userId, loginTime, getExpands(expands));
        //    logger.warn(type.getName() + desc+"  userId:"+userId);
    }

    public static void addItemLog(LogType type, int uid, int itemId, int count, boolean isAdd, String activityID, Date opera_time, String desc, String... expands) {
        //   TaskPools.addTask(LogMethod.class, "addItemLog", type.getName(), uid, itemId, count, itemLogType, activityID, opera_time, getExpands(expands));
        // logger.warn(type.getName() + desc);
    }

    public static void addRankLog(LogType type, int uid, Date opera_time, String desc, String... expands) {
        //   TaskPools.addTask(LogMethod.class, "addItemLog", type.getName(), uid, itemId, count, itemLogType, activityID, opera_time, getExpands(expands));
        // logger.warn(type.getName() + desc);
    }

    /**
     * 玩家行为日志
     *
     * @param type
     * @param userId
     * @param time
     * @param ui
     * @param bid
     * @param second
     * @param index
     * @param expands
     */
    public static void addUserDoLog(LogType type, int userId, Date time, String ui, String bid, int second, int index, String... expands) {
        //     TaskPools.addTask(LogMethod.class, "addUserDoLog", type.getName(),userId, time, ui, bid, second, index,expands);
    }
}
