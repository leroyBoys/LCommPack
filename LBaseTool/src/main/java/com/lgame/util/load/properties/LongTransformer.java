/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load.properties;

import java.lang.reflect.Field;

/**
 * Long转换类
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class LongTransformer implements PropertyTransformer<Long> {

    public static final LongTransformer SHARED_INSTANCE = new LongTransformer();

    public Long transform(String value, Field field) {
        try {
            return Long.decode(value == null || value.trim().isEmpty()?"0":value.trim());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
