/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logger.bases;

import com.lgame.util.comm.SplitConstant;
import com.logger.type.Entry;
import com.logger.type.GoodsType;

/**
 * 物品收支情况
 *
 */
public class LoggerGoods {

    /**
     * 收支情况. 0-收入, 1-支出
     */
    private Entry entry;

    /**
     * 物品的类型. 详细见: {@link GoodsType}
     */
    private int goodsType;

    /**
     * 物品的自增ID. 收入则该值为0
     */
    private long goodsId;

    /**
     * 基础ID
     */
    private int baseId;

    /**
     * 数量
     */
    private int count;

    /**
     * 是否自动购买, 0-手动购买, 1-自动购买
     */
    private int auto = 0;

    /**
     * 总金币价格
     */
    private long totalGolden;

    /**
     * 总特殊货币价格
     */
    private long totalCoupon;

    /**
     * 总游戏币价格
     */
    private long totalSilver;

    /**
     * 当前物品的详细信息
     */
    private String info = null;

    public Entry getEntry() {
        return entry;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public int getBaseId() {
        return baseId;
    }

    public int getCount() {
        return count;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public int getAuto() {
        return auto;
    }

    public long getTotalGolden() {
        return totalGolden;
    }

    public long getTotalSilver() {
        return totalSilver;
    }

    public long getTotalCoupon() {
        return totalCoupon;
    }

    /**
     * 花代币得到装备
     *
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @param price	物品单价
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods incomeEquipByGolden(int baseId, int count, long price) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.totalGolden = price;
        goodInfo.entry = Entry.INCOME;
        goodInfo.goodsType = GoodsType.EQUIP;
        return goodInfo;
    }

    /**
     * 花特殊货币得到装备
     *
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @param price	物品单价
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods incomeEquipByCoupon(int baseId, int count, long price) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.totalCoupon = price;
        goodInfo.entry = Entry.INCOME;
        goodInfo.goodsType = GoodsType.EQUIP;
        return goodInfo;
    }

    /**
     * 花游戏币得到装备
     *
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @param price	物品单价
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods incomeEquipBySilver(int baseId, int count, int silver) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.totalSilver = silver;
        goodInfo.entry = Entry.INCOME;
        goodInfo.goodsType = GoodsType.EQUIP;
        return goodInfo;
    }

    /**
     * 得到装备
     *
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods incomeEquip(int baseId, int count) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.entry = Entry.INCOME;
        goodInfo.goodsType = GoodsType.EQUIP;
        return goodInfo;
    }

    /**
     * 得到装备
     *
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @param price	物品单价
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods incomeEquipByMoney(int baseId, int count, long golden, long silver) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.totalSilver = silver;
        goodInfo.totalGolden = golden;
        goodInfo.entry = Entry.INCOME;
        goodInfo.goodsType = GoodsType.EQUIP;
        return goodInfo;
    }

    /**
     * 支出装备
     *
     * @param goodsId	物品自增ID
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods outcomeEquip(long goodsId, int baseId, int count) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.auto = 0;
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.goodsId = goodsId;
        goodInfo.entry = Entry.OUTCOME;
        goodInfo.goodsType = GoodsType.EQUIP;
        return goodInfo;
    }

    /**
     * 支出装备
     *
     * @param goodsId	物品自增ID
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods outcomeEquipBySilver(long goodsId, int baseId, int count, long silver) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.auto = 0;
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.goodsId = goodsId;
        goodInfo.totalSilver = silver;
        goodInfo.entry = Entry.OUTCOME;
        goodInfo.goodsType = GoodsType.EQUIP;
        return goodInfo;
    }

    /**
     * 自动购买支出装备
     *
     * @param goodsId	物品自增ID
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods outcomeEquipAutoBuySilver(long goodsId, int baseId, int count, long silver) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.auto = 1;
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.goodsId = goodsId;
        goodInfo.totalSilver = silver;
        goodInfo.entry = Entry.OUTCOME;
        goodInfo.goodsType = GoodsType.EQUIP;
        return goodInfo;
    }

    /**
     * 支出装备
     *
     * @param goodsId	物品自增ID
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods outcomeEquipByGolden(long goodsId, int baseId, int count, long golden) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.auto = 0;
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.goodsId = goodsId;
        goodInfo.totalGolden = golden;
        goodInfo.entry = Entry.OUTCOME;
        goodInfo.goodsType = GoodsType.EQUIP;
        return goodInfo;
    }

    /**
     * 支出装备
     *
     * @param goodsId	物品自增ID
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods outcomeEquipAutoBuyGolden(long goodsId, int baseId, int count, long golden) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.auto = 1;
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.goodsId = goodsId;
        goodInfo.totalGolden = golden;
        goodInfo.entry = Entry.OUTCOME;
        goodInfo.goodsType = GoodsType.EQUIP;
        return goodInfo;
    }

    /**
     * 支出装备通过货币
     *
     * @param goodsId	物品自增ID
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods outcomeEquipByMoney(long goodsId, int baseId, int count, long golden, long silver) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.auto = 0;
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.goodsId = goodsId;
        goodInfo.totalSilver = silver;
        goodInfo.totalGolden = golden;
        goodInfo.entry = Entry.OUTCOME;
        goodInfo.goodsType = GoodsType.EQUIP;
        return goodInfo;
    }

    /**
     * 支出装备通过货币
     *
     * @param goodsId	物品自增ID
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods outcomeEquipAutoBuyMoney(long goodsId, int baseId, int count, long golden, long silver) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.auto = 1;
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.goodsId = goodsId;
        goodInfo.totalSilver = silver;
        goodInfo.totalGolden = golden;
        goodInfo.entry = Entry.OUTCOME;
        goodInfo.goodsType = GoodsType.EQUIP;
        return goodInfo;
    }

    /**
     * 得到道具
     *
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods incomeProps(int baseId, int count) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.entry = Entry.INCOME;
        goodInfo.goodsType = GoodsType.PROPS;
        return goodInfo;
    }

    /**
     * 得到道具
     *
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods incomePropsBySilver(int baseId, int count, long silver) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.totalSilver = silver;
        goodInfo.entry = Entry.INCOME;
        goodInfo.goodsType = GoodsType.PROPS;
        return goodInfo;
    }

    /**
     * 得到道具
     *
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods incomePropsByGolden(int baseId, int count, long golden) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.totalGolden = golden;
        goodInfo.entry = Entry.INCOME;
        goodInfo.goodsType = GoodsType.PROPS;
        return goodInfo;
    }

    /**
     * 得到道具
     *
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @param coupon	礼金价格
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods incomePropsByCoupon(int baseId, int count, long coupon) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.totalCoupon = coupon;
        goodInfo.entry = Entry.INCOME;
        goodInfo.goodsType = GoodsType.PROPS;
        return goodInfo;
    }

    /**
     * 得到道具
     *
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods incomePropsByMoney(int baseId, int count, long golden, long silver) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.totalSilver = silver;
        goodInfo.totalGolden = golden;
        goodInfo.entry = Entry.INCOME;
        goodInfo.goodsType = GoodsType.PROPS;
        return goodInfo;
    }

    /**
     * 支出道具
     *
     * @param goodsId	物品自增ID
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods outcomeProps(long goodsId, int baseId, int count) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.goodsId = goodsId;
        goodInfo.entry = Entry.OUTCOME;
        goodInfo.goodsType = GoodsType.PROPS;
        return goodInfo;
    }

    /**
     * 支出道具
     *
     * @param goodsId	物品自增ID
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods outcomePropsByMoney(long goodsId, int baseId, int count, long golden, long silver) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.goodsId = goodsId;
        goodInfo.totalGolden = golden;
        goodInfo.totalSilver = silver;
        goodInfo.entry = Entry.OUTCOME;
        goodInfo.goodsType = GoodsType.PROPS;
        return goodInfo;
    }

    /**
     * 支出道具
     *
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods outcomeProps(int baseId, int count) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.entry = Entry.OUTCOME;
        goodInfo.goodsType = GoodsType.PROPS;
        return goodInfo;
    }

    /**
     * 自动购买道具支出
     *
     * @param goodsId	物品自增ID
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods outcomePropsAutoBuyGolden(int baseId, int count, long golden) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.auto = 1;
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.totalGolden = golden;
        goodInfo.entry = Entry.OUTCOME;
        goodInfo.goodsType = GoodsType.PROPS;
        return goodInfo;
    }

    /**
     * 自动购买道具支出
     *
     * @param goodsId	物品自增ID
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods outcomePropsAutoBuyMoney(int baseId, int count, long golden, long silver) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.auto = 1;
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.totalGolden = golden;
        goodInfo.totalSilver = silver;
        goodInfo.entry = Entry.OUTCOME;
        goodInfo.goodsType = GoodsType.PROPS;
        return goodInfo;
    }

    /**
     * 自动购买道具支出
     *
     * @param goodsId	物品自增ID
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods outcomePropsAutoBuySilver(int baseId, int count, long silver) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.auto = 1;
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.totalSilver = silver;
        goodInfo.entry = Entry.OUTCOME;
        goodInfo.goodsType = GoodsType.PROPS;
        return goodInfo;
    }

    /**
     * 装备收支...
     *
     * @param entry
     * @param userEquips
     * @return
     */
//	public static List<LoggerGoods> loggerEquip(Entry entry, Collection<UserEquip> userEquips){
//		List<LoggerGoods> list = new ArrayList<LoggerGoods>();
//		if(userEquips != null){
//			for(UserEquip userEquip : userEquips){
//				LoggerGoods goodInfo = new LoggerGoods();
//				goodInfo.baseId = userEquip.getBaseId();
//				goodInfo.count = 1;
//				goodInfo.goodsId = userEquip.getId();
//				goodInfo.goodsType = GoodsType.EQUIP;
//				goodInfo.entry = entry;
//				list.add(goodInfo);
//			}
//		}
//		return list;
//	}
	//-----------------------------------------------------------
    /**
     * 道具收入
     *
     * @param entry
     * @param userEquips
     * @return
     */
//	public static List<LoggerGoods> incomeProps(Collection<UserProps> userPropsList){
//		List<LoggerGoods> list = new ArrayList<LoggerGoods>();
//		if(userPropsList != null){
//			for(UserProps userProps : userPropsList){
//				LoggerGoods goodInfo = new LoggerGoods();
//				goodInfo.entry = Entry.INCOME;
//				goodInfo.baseId = userProps.getBaseId();
//				goodInfo.count = userProps.getCount(); 
//				goodInfo.goodsId = userProps.getId();
//				goodInfo.goodsType = GoodsType.PROPS;
//				list.add(goodInfo);
//			}
//		}
//		return list;
//	}
    /**
     * 物品收支
     *
     * @param entry
     * @param userEquips
     * @return
     */
//	public static List<LoggerGoods> loggerProps(Entry entry, Map<Long, Integer> updateMap, Collection<UserProps> userPropsList){
//		List<LoggerGoods> list = new ArrayList<LoggerGoods>();
//		if(userPropsList != null){
//			for(UserProps userProps : userPropsList){
//				LoggerGoods goodInfo = new LoggerGoods();
//				goodInfo.baseId = userProps.getBaseId();
//				goodInfo.count = updateMap.containsKey( userProps.getId() )? updateMap.get(userProps.getId()) : 0;
//				goodInfo.goodsId = userProps.getId();
//				goodInfo.goodsType = GoodsType.PROPS;
//				goodInfo.entry = entry;
//				list.add(goodInfo);
//			}
//		}
//		return list;
//	}
    /**
     * 物品收支
     *
     * @param entry
     * @param userEquips
     * @return
     */
//	public static List<LoggerGoods> updateProps(Map<Long, Integer> updateMap, Collection<UserProps> userPropsList){
//		List<LoggerGoods> list = new ArrayList<LoggerGoods>();
//		if(userPropsList != null){
//			for(UserProps userProps : userPropsList){
//				LoggerGoods goodInfo = new LoggerGoods();
//				Integer count = updateMap.get(userProps.getId());
//				if(count == null){
//					continue;
//				}
//				goodInfo.baseId = userProps.getBaseId();
//				goodInfo.count = Math.abs(count);
//				goodInfo.goodsId = userProps.getId();
//				goodInfo.goodsType = GoodsType.PROPS;
//				goodInfo.entry = count > 0 ? Entry.INCOME : Entry.OUTCOME;
//				list.add(goodInfo);
//			}
//		}
//		return list;
//	}
    /**
     * 变更道具
     *
     * @param entry	收支情况
     * @param goodsType	物品类型
     * @param baseId	基础装备ID
     * @param count	得到的数量
     * @return {@link LoggerGoods}	物品得失信息
     */
    public static LoggerGoods changedGoods(Entry entry, int goodsType, int baseId, int count) {
        LoggerGoods goodInfo = new LoggerGoods();
        goodInfo.count = count;
        goodInfo.baseId = baseId;
        goodInfo.entry = entry;
        goodInfo.goodsType = goodsType;
        return goodInfo;
    }

    @Override
    public String toString() {
        if (this.info == null) {
            this.info = new StringBuilder().append(entry.getCode()).append(SplitConstant.ATTRIBUTE_SPLIT)
                    .append(goodsType).append(SplitConstant.ATTRIBUTE_SPLIT)
                    .append(goodsId).append(SplitConstant.ATTRIBUTE_SPLIT)
                    .append(baseId).append(SplitConstant.ATTRIBUTE_SPLIT)
                    .append(count).append(SplitConstant.ATTRIBUTE_SPLIT)
                    .append(auto).append(SplitConstant.ATTRIBUTE_SPLIT)
                    .append(totalGolden).append(SplitConstant.ATTRIBUTE_SPLIT)
                    .append(totalSilver).append(SplitConstant.ELEMENT_DELIMITER).toString();
        }
        return this.info;
    }
}
