/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load.config;

import com.lgame.util.PrintTool;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author leroy_boy
 */
public class JsonReader {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TypeFactory typeFactory = TypeFactory.defaultInstance();

    public static <E> Iterator<E> read(InputStream input, Class<E> clazz) {
        try {
            JavaType type = typeFactory.constructCollectionType(ArrayList.class, clazz);
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.setDateFormat(new SimpleDateFormat("yyyyMMdd"));
            List<E> list = (List) mapper.readValue(input, type);
            return list.iterator();
        } catch (IOException e) {
            PrintTool.error("JsonReader读取基础数据:["+clazz.getSimpleName()+"] 文件异常!", e);
            throw new RuntimeException(e);
        }
    }
}