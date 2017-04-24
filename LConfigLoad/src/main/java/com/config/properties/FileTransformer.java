/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.config.properties;

import java.io.File;
import java.lang.reflect.Field;

/**
 * 文件转换类
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class FileTransformer implements PropertyTransformer<File> {

    public static final FileTransformer SHARED_INSTANCE = new FileTransformer();

    public File transform(String value, Field field){
        return new File(value);
    }
}
