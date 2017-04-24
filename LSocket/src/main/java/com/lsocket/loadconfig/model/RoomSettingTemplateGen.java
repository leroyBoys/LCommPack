package com.lsocket.loadconfig.model;

import com.config.annotation.Id;
import com.config.annotation.Resource;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/24.
 */
@Resource(suffix="xls",sheetName = "RoomSettingTemplateGen")
public class RoomSettingTemplateGen {
    /**主键*/
    @Id
    private int tempId = 0;
    /**房间类型*/
    private boolean roomType = true;
    /**参与人数*/
    private int playerNum = 0;
    /**棋牌引擎类型*/
    private String engineType;
    /**棋牌库*/
    private String cardNumPool;
    /**棋牌类型,0麻将*/
    private int cardType;
    /**初始手牌数*/
    private int initHandCardCount;
    /**一张房卡可以玩的局数*/
    private int ticket;

    public int getTempId() {
        return tempId;
    }

    public void setTempId(int tempId) {
        this.tempId = tempId;
    }

    public boolean isRoomType() {
        return roomType;
    }

    public void setRoomType(boolean roomType) {
        this.roomType = roomType;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getCardNumPool() {
        return cardNumPool;
    }

    public void setCardNumPool(String cardNumPool) {
        this.cardNumPool = cardNumPool;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getInitHandCardCount() {
        return initHandCardCount;
    }

    public void setInitHandCardCount(int initHandCardCount) {
        this.initHandCardCount = initHandCardCount;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }
}
