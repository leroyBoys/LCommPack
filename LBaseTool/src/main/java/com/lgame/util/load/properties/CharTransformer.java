/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load.properties;

import java.lang.reflect.Field;

/**
 * 字符转换类
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class CharTransformer implements PropertyTransformer<Character> {

    public static final CharTransformer SHARED_INSTANCE = new CharTransformer();

    public Character transform(String value, Field field){
        try {
            char[] chars = value.toCharArray();
            if (chars.length > 1) {
                throw new RuntimeException("To many characters in the value");
            }
            return Character.valueOf(chars[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
