/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load.properties;

import java.lang.reflect.Field;

/**
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 * @param <T>
 */
public abstract interface PropertyTransformer<T> {

    public abstract T transform(String paramString, Field paramField);
}
