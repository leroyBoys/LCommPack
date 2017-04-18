/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logger.type;

/**
 * 物品的类型
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public interface GoodsType {

    /**
     * -1 - 不存在
     */
    int NONE = -1;

    /**
     * 0 - 道具类型
     */
    int PROPS = 0;

    /**
     * 1 - 装备类型
     */
    int EQUIP = 1;

    /**
     * 2 - 游戏币类型
     */
    int SILVER = 2;

    /**
     * 3 - 代币类型
     */
    int GOLDEN = 3;

    /**
     * 4 - 特殊货币类型，如运营活动奖励货币
     */
    int COUPON = 4;

    int[] CURRENCYS = {SILVER, GOLDEN};
}
