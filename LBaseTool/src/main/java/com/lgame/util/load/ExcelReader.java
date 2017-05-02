/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load;

import com.lgame.util.PrintTool;
import com.lgame.util.load.annotation.Resource;
import com.lgame.util.load.excel.DefaultBuilder;
import com.lgame.util.load.excel.ExcelLoader;

import java.io.InputStream;
import java.util.Iterator;

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
