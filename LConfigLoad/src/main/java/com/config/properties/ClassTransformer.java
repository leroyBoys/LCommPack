/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.config.properties;

import java.lang.reflect.Field;

/**
 * 类对象转换类
 *
 */
public class ClassTransformer implements PropertyTransformer<Class<?>> {

    public static final ClassTransformer SHARED_INSTANCE = new ClassTransformer();

    public Class<?> transform(String value, Field field){
        try {
            return Class.forName(value, false, getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find class with name '" + value + "'");
        }
    }
}
