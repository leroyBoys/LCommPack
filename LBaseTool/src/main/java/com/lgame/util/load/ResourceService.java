/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load;

import java.util.Collection;
import java.util.List;

/**
 * 基础数据操作接口类
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public abstract interface ResourceService {

    /**
     * 通过@Id获取数据对象
     *
     * @param <T>
     * @param id
     * @param clazz
     * @return
     */
    public abstract <T> T get(Object paramObject, Class<T> paramClass);

    /**
     * 返回索引名称为indexName，并且值等于indexValues的所有数据对象列表
     *
     * @param <T>
     * @param indexName
     * @param clazz
     * @param indexValues
     * @return
     */
    public abstract <T> List<T> listByIndex(String paramString, Class<T> paramClass, Object... paramVarArgs);

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
    public abstract <T, PK> List<PK> listIdByIndex(String paramString, Class<T> paramClass, Class<PK> pk, Object... paramVarArgs);

    /**
     * 返回索引名称为indexName，并且值等于indexValues的所有数据对象，第一个匹配的。
     *
     * @param <T>
     * @param indexName
     * @param clazz
     * @param indexValues
     * @return
     */
    public abstract <T> T getByUnique(String paramString, Class<T> paramClass, Object... paramVarArgs);

    /**
     * 返回所有clazz的数据对象集合
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public abstract <T> Collection<T> listAll(Class<T> paramClass);

    /**
     * 为已存在的基础对象增加新的索引和id关系，索引键将为indexName和clazz名称组合
     *
     * @param <T>
     * @param indexName
     * @param id
     * @param clazz
     */
    public abstract <T> void addToIndex(String paramString, Object paramObject, Class<T> paramClass);

    /**
     * 为已存在的基础对象增加新的索引和id关系,索引键将为indexValues的组合
     *
     * @param <T>
     * @param indexName
     * @param id
     * @param clazz
     * @param indexValues
     */
    public abstract <T> void addToIndex(String paramString, Object paramObject, Class<T> paramClass, Object... paramVarArgs);

    /**
     * 重新加载存储中所有存在的数据对象。不会新增基础对象类型
     */
    public abstract void reloadAll();
}
