/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.config.properties;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class PatternTransformer implements PropertyTransformer {

    public static final PatternTransformer SHARED_INSTANCE = new PatternTransformer();

    public Pattern transform(String value, Field field) {
        try {
            return Pattern.compile(value);
        } catch (Exception e) {
            throw new RuntimeException("Not valid RegExp: " + value, e);
        }
    }
}
