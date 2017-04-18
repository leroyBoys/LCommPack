/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logger;

/**
 * 统计数据项集合
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public interface LogKeys {

    /**
     * 来源
     */
    String SOURCE = "source";
    /**
     * ip地址
     */
    String IP = "ip";

    /**
     * 角色创建时间
     */
    String CREATE_TIME = "createTime";

    /**
     * 角色账号名
     */
    String LOGIN_NAME = "loginName";
    /**
     * 角色ID
     */
    String PLAYERID = "playerId";
    /**
     * 角色名
     */
    String PLAYER_NAME = "playerName";
    /**
     * 角色的等级
     */
    String LEVEL = "level";
    /**
     * 增加/减少的代币
     */
    String GOLDEN = "golden";
    /**
     * 增加/减少的游戏币
     */
    String SILVER = "silver";
    /**
     * 道具的ID
     */
    String PROPS_ID = "propsId";

    /**
     * 使用数量
     */
    String USE_PROPS_COUNT = "usePropsCount";

    /**
     * 物品信息
     */
    String INFO = "info";

    /**
     * 货币类型
     */
    String CURRENCY = "currency";

    /**
     * 副本id
     */
    String COPY_ID = "copyId";

    /**
     * 掉落怪物名称
     */
    String MONSTER_NAME = "monsterName";

    /**
     * 基础任务名
     */
    String TASKNAME = "taskName";

    /**
     * 运营活动ID
     */
    String ACTIVE_ID = "activeId";

    /**
     * 角色登录日期
     */
    String LOGINTIME = "loginTime";

    /**
     * 角色登出时间
     */
    String LOGOUTTIME = "logoutTime";

    /**
     * 角色登录次数
     */
    String LOGINCOUNT = "loginCount";

    /**
     * 增加前的经验
     */
    String BEFORE_EXP = "beforeExp";

    /**
     * 增加的经验
     */
    String ADDEXP = "addExp";

    /**
     * 升级前等级
     */
    String BEFORE_LEVEL = "beforeLevel";

    /**
     * 升级后等级
     */
    String AFTER_LEVEL = "afterLevel";

    /**
     * 升级时间
     */
    String UPGRADE_TIME = "upgradeTime";

    /**
     * 帮派的名字
     */
    String ALLIANCE_NAME = "allianceName";

    /**
     * 帮派当前等级
     */
    String CURRENT_ALLIANCE_LEVEL = "currentAllianceLevel";

    /**
     * 类型
     */
    String TYPE = "type";
}
