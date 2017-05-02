/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load.properties;

import java.lang.reflect.Field;

/**
 * 字节转换类
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class ByteTransformer implements PropertyTransformer<Byte> {

    public static final ByteTransformer SHARED_INSTANCE = new ByteTransformer();

    public Byte transform(String value, Field field){
        try {
            return Byte.decode(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
