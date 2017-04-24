package com.config.excel;


import com.lgame.util.PrintTool;
import com.lgame.util.load.PropertyTransformer;
import com.lgame.util.load.PropertyTransformerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class DefaultBuilder implements IBuilder {
	private DefaultBuilder(){}
	private static final DefaultBuilder obj = new DefaultBuilder();

	public final static DefaultBuilder getInstance(){
		return obj;
	}


	@Override
	public <B> Iterator<B> initBuild(String[][] data, Class<B> beanClass) throws Exception {

		Field[] fieldMap = processProperties(beanClass,data[0]);
		String[] row;
		B b;
		List<B> list = new LinkedList<>();
		for (int i = 7; i < data.length; i++) {
			row = data[i];
			b = beanClass.newInstance();
			initBean(b,row,fieldMap);
			list.add(b);
		}

		return list.iterator();
	}

	private <B> void initBean(B b, String[] row,Field[] head) {

		for(int i = 0;i< head.length;i++){
				Object transformValue = transformerFieldValue(row[i], head[i]);
				try {
					head[i].set(b, transformValue);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	public static Object transformerFieldValue(String value, Field field) {
		try {
			value = value.trim().replaceAll("\\n","");

			Class<?> fieldType = field.getType();
			Class<? extends PropertyTransformer> formers = PropertyTransformer.class;
			PropertyTransformer<?> propertyTransformer = PropertyTransformerFactory.newTransformer(fieldType, formers);
			return propertyTransformer.transform(value, field);
		} catch (Exception e) {
		}
		PrintTool.info(field.getName()+" = "+value);
		return null;
	}

	public static Field[] processProperties(Class<?> clazz,String[] head) {
		Map<String, Field> bundleMaps = new HashMap();
		for (Field field : clazz.getDeclaredFields()) {
				if (!Modifier.isPublic(field.getModifiers())) {
					field.setAccessible(true);
				}
				bundleMaps.put(field.getName(),field);
				//Object transformValue = transformerFieldValue(value, field, annotation);
				/*try {
					field.set(object, transformValue);
				} catch (Exception e) {
					e.printStackTrace();
					if (annotation.required()) {
						throw new IllegalArgumentException(String.format("Parse %s ERROR Exception", new Object[]{annotation.keyName()}));
					}
				}*/
			}
			Field[] ret = new Field[head.length];
		    for(int i = 0;i<head.length;i++){
				ret[i] = bundleMaps.get(head[i]);
			}


			return ret;
	}
}

