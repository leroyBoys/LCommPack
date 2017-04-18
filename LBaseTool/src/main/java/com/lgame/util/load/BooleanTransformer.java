/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load;

import java.lang.reflect.Field;

/**
 * Boolean转换类
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class BooleanTransformer implements PropertyTransformer<Boolean> {

    public static final BooleanTransformer SHARED_INSTANCE = new BooleanTransformer();

    public Boolean transform(String value, Field field){
        if (("true".equalsIgnoreCase(value)) || ("1".equals(value))) {
            return Boolean.valueOf(true);
        }
        if (("false".equalsIgnoreCase(value)) || ("0".equals(value))) {
            return Boolean.valueOf(false);
        }
        throw new RuntimeException("Invalid boolean string: " + value);
    }
}
