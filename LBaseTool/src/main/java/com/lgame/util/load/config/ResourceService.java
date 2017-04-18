/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load.config;

import com.lgame.util.PrintTool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class ResourceService {

    private ConcurrentHashMap<Class, Storage> storages = new ConcurrentHashMap(50);
    //  private String resourceLocation = "/usr/game/xml/";//+ File.separator//基础数据文件路径
    private String resourceLocation = "D:\\xml\\";//+ File.separator//基础数据文件路径

    private void initializeStorage(Class clazz) {
        Storage storage = (Storage) this.storages.get(clazz);
        if (storage == null) {
            storage = new Storage(clazz, this.resourceLocation);
            this.storages.putIfAbsent(clazz, storage);
            storage = (Storage) this.storages.get(clazz);
        }
        storage.reload();
    }

    private Storage getStorage(Class clazz) {
        initializeStorage(clazz);
        return (Storage) this.storages.get(clazz);
    }

    /**
     * 返回所有clazz的数据对象集合
     *
     * @param <T>
     * @param clazz
     * @return
     */
    
    public <T> Collection<T> listAll(Class<T> clazz) {
        Storage storage = getStorage(clazz);
        if (storage != null) {
            return storage.listAll();
        }
        return new ArrayList(0);
    }

    /**
     * 重新加载存储中所有存在的数据对象。不会新增基础对象类型
     */
    public void reloadAll() {
        for (Class clazz : this.storages.keySet()) {
            try {
                initializeStorage(clazz);
            } catch (Exception e) {
                PrintTool.error("加载 {"+clazz.getName()+"} 类时出错!");
            }
        }
    }
}
