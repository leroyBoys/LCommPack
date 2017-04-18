/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logger.type;

/**
 * 日志的来源
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public enum Source {
    //---------------- 1 --> 50 --------------------
    /**
     * 1 - 角色登陆
     */
    PLAYER_LOGIN(1),
    /**
     * 2 - 角色登出
     */
    PLAYER_LOGOUT(2),
    /**
     * 3 - 角色等级因为经验升级
     */
    PLAYER_LEVEL_UP_EXP(3),
    /**
     * 4 - 角色等级因为修改升级
     */
    PLAYER_LEVEL_UP_UPDATE(4),
    /**
     * 5 - 管理后台发放
     */
    ADMIN_MANAGER(5),
    //---------------- 51 --> 100 --------------------

    /**
     * 302 - 商店购买
     */
    BUY_SHOP_PROPS(51),
    /**
     * 403 - 出售装备
     */
    PROPS_SELL_USEREQUIP(52),
    /**
     * 404 - 出售道具
     */
    PROPS_SELL_USERPROPS(53),
    /**
     * 412 - 直接使用道具
     */
    PROPS_USE_PROPS(54),
    //---------------- 101 --> 150 --------------------
    /**
     * 战斗经验
     */
    EXP_FIGHT(101),
    /**
     * 经验道具
     */
    EXP_PROPS(102),
    /**
     * 任务
     */
    EXP_TASK(103),
    //---------------- 151 --> 200 --------------------
    /**
     * 661 - 副本
     */
    DUNGEON(151),
    /**
     * 691 - 帮派创建
     */
    ALLIANCE_CREATE(152),
    //---------------- 201 --> 250 --------------------
    /**
     * 1001 - 掉落
     */
    FIGHT_DROP(201),
    /**
     * 固定礼包
     */
    FASTEN_GIFT(202),
    /**
     * 1301 - 学习技能
     */
    PLAYER_LEARN_SKILL(203),
    //---------------- 251 --> 300 --------------------
    /**
     * 领取邮件附件奖励(元宝, 铜币, 物品,　装备)
     */
    RECEIVE_MAIL_REWARDS(251),
    /**
     * 领取CDKEY礼包奖励
     */
    RECEIVE_CDKEY_REWARDS(252),
    /**
     * 领取普通礼包奖励
     */
    RECEIVE_GIFT_REWARDS(253),
    //---------------- 301 --> 350 --------------------
    /**
     * 运营活动
     */
    ACTIVE_OPERATOR(301),
    /**
     * 领取成就奖励
     */
    ACHIEVE_REWARD_RECEIVED(302),;
    private int code = -1;

    Source(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
