/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lgame.util.load.properties;

import java.lang.reflect.Field;

/**
 * Float转换类
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class FloatTransformer implements PropertyTransformer<Float> {

    public static final FloatTransformer SHARED_INSTANCE = new FloatTransformer();

    public Float transform(String value, Field field){
        try {
            return Float.valueOf(Float.parseFloat(value == null || value.trim().isEmpty()?"0":value.trim()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
