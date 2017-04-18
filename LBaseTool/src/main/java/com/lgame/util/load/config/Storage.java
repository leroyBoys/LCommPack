/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load.config;

import com.lgame.util.PrintTool;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @param <T>
 */
public class Storage<T extends BaseXml> {

    private Class<? extends BaseXml> clazz;
    private String location;
    private String suffix = "xml";
    private List<? extends BaseXml> dataTable = new ArrayList<>();
    private String resourceLocation = "xmldb" + File.separator;
    

    public Storage(Class<? extends BaseXml> clazz, String resourceLocation) {
        this.resourceLocation = resourceLocation;
        this.clazz = clazz;
        initialize(clazz);
    }

    private void initialize(Class clazz) {
        this.clazz = clazz;
        this.location = this.resourceLocation + clazz.getSimpleName() + "." + suffix;//配置文件
    }

    public synchronized void reload() {
        try {
//            URL resource = getClass().getClassLoader().getResource(this.location);
//            URL resource = new URL(location);
            File dir = new File(this.resourceLocation);
            final String filePath = this.location;
            String[] fileNames = dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    String path = dir+"\\"+name;
                    System.out.println("==checkName:path:"+path+"    filePath:"+filePath);
                    if(path.equalsIgnoreCase(filePath)){
                        return true;
                    }
                    return false;
                }
            });
            if (fileNames==null ||fileNames.length == 0) {
                PrintTool.error("基础数据["+this.clazz.getName()+"]所对应的资源文件["+this.location+"]不存在!");
                return;
            }
            File resource = new File(this.resourceLocation+fileNames[0]);
            InputStream input =  new FileInputStream(resource);
            Iterator<? extends BaseXml> it = JsonReader.read(input, this.clazz);

           List dataTable_copy = new ArrayList<>();//数据存储
            while (it.hasNext()) {
                T obj = (T) it.next();
                if (offer(obj, dataTable_copy)) {
                    throw new RuntimeException(String.format("重复异常: [%s]", new Object[]{obj}));
                }
                dataTable_copy.add(obj);
            }

            this.dataTable.clear();

            this.dataTable.addAll(dataTable_copy);
            PrintTool.info("完成加载  {} 基础数据...", this.clazz.getName());
        } catch (IOException e) {
            e.printStackTrace();
            PrintTool.error("基础数据["+this.clazz.getName()+"]所对应的资源文件["+this.location+"]不存在!");
        } catch (Exception e) {
            PrintTool.error("error sorage reload!",e);
        }
    }


    public Collection<T> listAll() {
        return new ArrayList(dataTable);
    }
    private boolean offer(T obj, List<T> dataTable_copy) {
        for(T ob:dataTable_copy){
            if(ob.isTheSame(obj)){
                return true;
            }
        }
        return false;
    }
}
