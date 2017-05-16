/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load.properties;

import java.lang.reflect.Field;

/**
 * short转换类
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class ShortTransformer implements PropertyTransformer<Short> {

    public static final ShortTransformer SHARED_INSTANCE = new ShortTransformer();

    public Short transform(String value, Field field) {
        try {
            return Short.decode(value == null || value.trim().isEmpty()?"0":value.trim());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
