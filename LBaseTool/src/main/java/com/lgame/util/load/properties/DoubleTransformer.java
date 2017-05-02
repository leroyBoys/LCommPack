/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load.properties;

import java.lang.reflect.Field;

/**
 * double转换类
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class DoubleTransformer implements PropertyTransformer<Double> {

    public static final DoubleTransformer SHARED_INSTANCE = new DoubleTransformer();

    public Double transform(String value, Field field){
        try {
            return Double.valueOf(Double.parseDouble(value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
