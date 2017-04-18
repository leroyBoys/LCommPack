/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load;

import java.lang.reflect.Field;

/**
 * 枚举转换类
 *
 */
public class EnumTransformer implements PropertyTransformer<Enum<?>> {

    public static final EnumTransformer SHARED_INSTANCE = new EnumTransformer();

    public Enum<?> transform(String value, Field field) {
        Class<? extends Enum> clazz = (Class<? extends Enum>) field.getType();
        try {
            return Enum.valueOf(clazz, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
