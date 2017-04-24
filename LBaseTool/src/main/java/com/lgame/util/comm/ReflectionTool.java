package com.lgame.util.comm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/24.
 */
public class ReflectionTool {
    public static Field getFirstDeclaredFieldWith(Class<?> clz, Class<? extends Annotation> annotationClass) {
        for (Field field : clz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                return field;
            }
        }
        return null;
    }

    public static Field[] getDeclaredFieldsWith(Class<?> clz, Class<? extends Annotation> annotationClass) {
        List<Field> fields = new ArrayList();
        for (Field field : clz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                fields.add(field);
            }
        }
        return (Field[]) fields.toArray(new Field[0]);
    }

    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, (Class)null);
    }

    public static Object getField(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException var3) {
            throw new IllegalStateException("Unexpected reflection exception - " + var3.getClass().getName() + ": " + var3.getMessage());
        }
    }

    public static Field findField(Class<?> clazz, String name, Class<?> type) {

        for(Class searchType = clazz; !Object.class.equals(searchType) && searchType != null; searchType = searchType.getSuperclass()) {
            Field[] fields = searchType.getDeclaredFields();
            Field[] arr$ = fields;
            int len$ = fields.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Field field = arr$[i$];
                if((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
        }

        return null;
    }
}
