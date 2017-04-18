/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logger.bases;

import com.lgame.util.time.DateTimeTool;
import com.logger.type.Entry;

import java.util.Date;

import static com.lgame.util.comm.SplitConstant.DELIMITER_ARGS;

/**
 * 日志的条目
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class LogItem {

    /**
     * 条目名称
     */
    private String name;

    /**
     * 条目值
     */
    private Object value;

    /**
     * 构建条目对象
     *
     * @param name
     * @param value
     */
    private LogItem(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 构建一个日志子项
     *
     * @param name 条目名称
     * @param value 条目值
     */
    public static LogItem valueOf(String name, Object value) {
        return new LogItem(name, value);
    }

    /**
     * 将若干个{@link Term}信息变成: 名字1:信息1 的字符串
     *
     * @return {@link String}	返回构造后的字符串
     */
    @Override
    public String toString() {
        return new StringBuffer().append(name).append(DELIMITER_ARGS).append(value).toString();
    }

    /**
     * 货币收入
     *
     * @return {@link Term}
     */
    public static LogItem moneyIncomeTerm() {
        return new LogItem(Entry.MONEY_INCOME.getName(), Entry.MONEY_INCOME.getCode());
    }

    /**
     * 货币支出
     *
     * @return {@link Term}
     */
    public static LogItem moneyOutcomeTerm() {
        return new LogItem(Entry.MONEY_OUTCOME.getName(), Entry.MONEY_OUTCOME.getCode());
    }

    /**
     * 获得时间的日志断
     *
     * @return {@link Term}	日子子类
     */
    public static LogItem getTimeTerm() {
        return LogItem.valueOf("time", DateTimeTool.formatTime(new Date()));
    }
}
