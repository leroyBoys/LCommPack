/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.config;

import com.config.annotation.Resource;
import com.config.excel.DefaultBuilder;
import com.config.excel.ExcelLoader;
import com.config.excel.IBuilder;
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
 */
public class ExcelReader implements ResourceReader{

    public <E> Iterator<E> read(InputStream input, Class<E> clazz) {
        try {
            Resource resource = clazz.getAnnotation(Resource.class);

            String[][] dataOriginal = ExcelLoader.getInstance().loadConfig(input,
                    clazz.getSimpleName());
            // 构建数据
           return DefaultBuilder.getInstance().initBuild(dataOriginal,clazz,resource.dataFromLine());
        } catch (Exception e) {
            PrintTool.error("JsonReader读取基础数据:["+clazz.getSimpleName()+"] 文件异常!", e);
            throw new RuntimeException(e);
        }
    }
}
