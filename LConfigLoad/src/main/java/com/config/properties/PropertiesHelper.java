/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.config.properties;

import com.lgame.util.PrintTool;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 */
public class PropertiesHelper {
    public static void processProperties(Object object) {
        Class<?> clazz = null;
        if ((object instanceof Class)) {
            clazz = (Class) object;
            object = null;
        } else {
            clazz = object.getClass();
        }
        PrintTool.info("加载配置文件"+clazz.getName()+"......");
        Map<String, ResourceBundle> bundleMaps = new HashMap();
        for (Field field : clazz.getDeclaredFields()) {
            Properties annotation = (Properties) field.getAnnotation(Properties.class);
            if (annotation != null) {
                if (Modifier.isFinal(field.getModifiers())) {
                    String fileName = field.getName();
                    String clazzName = clazz.getName();
                    throw new RuntimeException("Attempt to proceed final field " + fileName + " of class " + clazzName);
                }
                String keyName = annotation.keyName();
                String fileName = annotation.fileName();
                ResourceBundle resourceBundle = (ResourceBundle) bundleMaps.get(fileName);
                if (resourceBundle == null) {
                    bundleMaps.put(fileName, ResourceBundle.getBundle(fileName));
                    resourceBundle = (ResourceBundle) bundleMaps.get(fileName);
                }
                String value = null;
                try {
                    value = resourceBundle.getString(keyName);
                } catch (Exception e) {
                    value = annotation.defaultValue();
                }
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                Object transformValue = transformerFieldValue(value, field, annotation);
                try {
                    field.set(object, transformValue);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (annotation.required()) {
                        throw new IllegalArgumentException(String.format("Parse %s ERROR Exception", new Object[]{annotation.keyName()}));
                    }
                }
            }
        }
        PrintTool.info("加载配置文件"+clazz.getName()+"完成");
    }

    private static Object transformerFieldValue(String value, Field field, Properties annotation) {
        try {
            Class<?> fieldType = field.getType();
            Class<? extends PropertyTransformer> formers = annotation.propertyTransformer();
            PropertyTransformer<?> propertyTransformer = PropertyTransformerFactory.newTransformer(fieldType, formers);
            return propertyTransformer.transform(value, field);
        } catch (Exception e) {
        }
        PrintTool.info(field.getName()+" = "+value);
        return annotation.defaultValue();
    }
}
