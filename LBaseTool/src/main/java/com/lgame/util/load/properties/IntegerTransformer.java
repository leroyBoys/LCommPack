/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load.properties;

import java.lang.reflect.Field;

/**
 * Integer转换类
 *
 */
public class IntegerTransformer implements PropertyTransformer<Integer> {

    public static final IntegerTransformer SHARED_INSTANCE = new IntegerTransformer();

    public Integer transform(String value, Field field) {
        try {
            return Integer.decode(value == null || value.trim().isEmpty()?"0":value.trim());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
