/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logger.type;

/**
 * 收支情况对象
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public enum Entry {

    /**
     * 0 - 收入
     */
    INCOME("income", 0),
    /**
     * 1 - 支出
     */
    OUTCOME("outcome", 1),
    /**
     * 0 - 货币收入
     */
    MONEY_INCOME("moneyEntry", 0),
    /**
     * 1 - 货币支出
     */
    MONEY_OUTCOME("moneyEntry", 1);

    private String name;

    private int code = -1;

    Entry(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
