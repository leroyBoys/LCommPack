/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logger.bases;

import static com.lgame.util.comm.SplitConstant.BETWEEN_ITEMS;

/**
 *
 */
public class ItemHelper {

    /**
     * 将若干个{@link}信息变成: 名字1:信息1|名字2:信息2|... 的字符串
     *
     * @param
     * @return {@link String}	返回构造后的字符串
     */
    public static String termToString(LogItem... Items) {
        StringBuilder builder = new StringBuilder();
        for (LogItem obj : Items) {
            builder.append(obj).append(BETWEEN_ITEMS);
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }
}
