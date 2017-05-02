/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load;

import com.lgame.util.PrintTool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class ResourceServiceImpl implements ResourceService{

    private ConcurrentHashMap<Class, Storage> storages = new ConcurrentHashMap(50);
   private String resourceLocation = "D:\\360极速浏览器下载\\棋牌_update\\qpgame\\t\\";//+ File.separator//基础数据文件路径

    private static ResourceServiceImpl resourceService = null;
    private ResourceServiceImpl(){}

    public synchronized static ResourceServiceImpl getInstance(String path){
        if(resourceService == null){
            resourceService = new ResourceServiceImpl();
            resourceService.resourceLocation = path;
        }
        return resourceService;
    }

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
        return (Storage) this.storages.get(clazz);
    }

    /**
     * 通过@Id获取数据对象
     *
     * @param <T>
     * @param id
     * @param clazz
     * @return
     */
    @Override
    public <T> T get(Object id, Class<T> clazz) {
        Storage storage = getStorage(clazz);
        if (storage != null) {
            return (T) storage.get(id);
        }
        return null;
    }

    /**
     * 返回索引名称为indexName，并且值等于indexValues的所有数据对象列表
     *
     * @param <T>
     * @param indexName
     * @param clazz
     * @param indexValues
     * @return
     */
    @Override
    public <T> List<T> listByIndex(String indexName, Class<T> clazz, Object... indexValues) {
        Storage storage = getStorage(clazz);
        if (storage != null) {
            return storage.getIndex(indexName, indexValues);
        }
        return null;
    }

    /**
     * 返回索引名称为indexName，并且值等于indexValues的所有Id列表
     *
     * @param <T>
     * @param <PK>
     * @param indexName
     * @param clazz
     * @param pk List中的数据类型
     * @param indexValues
     * @return
     */
    @Override
    public <T, PK> List<PK> listIdByIndex(String indexName, Class<T> clazz, Class<PK> pk, Object... indexValues) {
        Storage storage = getStorage(clazz);
        if (storage != null) {
            return storage.getIndexIdList(indexName, indexValues);
        }
        return new ArrayList(0);
    }

    /**
     * 返回索引名称为indexName，并且值等于indexValues的所有数据对象，第一个匹配的。
     *
     * @param <T>
     * @param indexName
     * @param clazz
     * @param indexValues
     * @return
     */
    @Override
    public <T> T getByUnique(String indexName, Class<T> clazz, Object... indexValues) {
        Storage storage = getStorage(clazz);
        if (storage != null) {
            List list = storage.getIndex(indexName, indexValues);
            return (T) ((list != null) && (!list.isEmpty()) ? list.get(0) : null);
        }
        return null;
    }

    /**
     * 为已存在的基础对象增加新的索引和id关系，索引键将为indexName和clazz名称组合
     *
     * @param <T>
     * @param indexName
     * @param id
     * @param clazz
     */
    @Override
    public <T> void addToIndex(String indexName, Object id, Class<T> clazz) {
        Storage storage = getStorage(clazz);
        if (storage == null) {
            return;
        }
        Map indexTable = storage.getIndexTable();
        if (indexTable == null) {
            return;
        }
        String newIndexKey = KeyBuilder.buildIndexKey(clazz, indexName, new Object[0]);
        List idList = (List) indexTable.get(newIndexKey);
        if (idList == null) {
            idList = new ArrayList();
            indexTable.put(newIndexKey, idList);
        }
        if (!idList.contains(id)) {
            idList.add(id);
        }
    }

    /**
     * 为已存在的基础对象增加新的索引和id关系,索引键将为indexValues的组合
     *
     * @param <T>
     * @param indexName
     * @param id
     * @param clazz
     * @param indexValues
     */
    @Override
    public <T> void addToIndex(String indexName, Object id, Class<T> clazz, Object... indexValues) {
        Storage storage = getStorage(clazz);
        if (storage == null) {
            return;
        }
        String newIndexKey = KeyBuilder.buildIndexKey(clazz, indexName, indexValues);
        Map indexTable = storage.getIndexTable();
        if (indexTable == null) {
            return;
        }
        List idList = (List) indexTable.get(newIndexKey);
        if (idList == null) {
            idList = new ArrayList();
            indexTable.put(newIndexKey, idList);
        }
        if (!idList.contains(id)) {
            idList.add(id);
        }
    }

    /**
     * 返回所有clazz的数据对象集合
     *
     * @param <T>
     * @param clazz
     * @return
     */
    @Override
    public <T> Collection<T> listAll(Class<T> clazz) {
        Storage storage = getStorage(clazz);
        if (storage == null) {
            initializeStorage(clazz);
            storage = getStorage(clazz);
        }
        if (storage != null) {
            return storage.listAll();
        }
        return new ArrayList(0);
    }

    /**
     * 重新加载存储中所有存在的数据对象。不会新增基础对象类型
     */
    @Override
    public void reloadAll() {
        for (Class clazz : this.storages.keySet()) {
            try {
                initializeStorage(clazz);
            } catch (Exception e) {
                PrintTool.error("加载 {"+ clazz.getName()+"} 类时出错!", e);
            }
        }
    }

    public static class KeyBuilder {

        public static String buildIndexKey(Class<?> clazz, String indexName, Object... indexValues) {
            if ((indexValues != null) && (indexValues.length > 0)) {
               // return StringUtils.arrayToDelimitedString(indexValues, "^");
                return "";
            }

            StringBuilder builder = new StringBuilder();
            if (clazz != null) {
                builder.append(clazz.getName()).append("&");
            }
            if (indexName != null) {
                builder.append(indexName).append("#");
            }
            return builder.toString();
        }
    }

}
