/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load;

import com.lgame.util.comm.ReflectionTool;
import com.lgame.util.load.annotation.Id;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *
 */
public class GetterBuilder {

    private static class FieldGetter implements Getter {

        private final Field field;

        public FieldGetter(Field field) {
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
            }
            this.field = field;
        }

        public Object getValue(Object object) {
            Object value = null;
            try {
                value = this.field.get(object);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            return value;
        }
    }

    public static Getter createIdGetter(Class<?> clz) {
        Field field = ReflectionTool.getFirstDeclaredFieldWith(clz, Id.class);
        if (field == null) {
            throw new RuntimeException(clz.getSimpleName()+" cant find  id");
        }

        return new FieldGetter(field);
    }
}
