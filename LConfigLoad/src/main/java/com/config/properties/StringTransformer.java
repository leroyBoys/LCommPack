/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.config.properties;

import java.lang.reflect.Field;

/**
 * String转换类
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class StringTransformer implements PropertyTransformer<String> {

    public static final StringTransformer SHARED_INSTANCE = new StringTransformer();

    public String transform(String value, Field field){
        return value;
    }
}
