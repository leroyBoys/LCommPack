package com.mysql.entity;

import com.dyuproject.protostuff.Schema;
import com.mysql.compiler.ColumInit;
import com.mysql.compiler.RelationGetIntace;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leroy:656515489@qq.com
 * 2018/4/25.
 */
public class DBTable {
    private String name;
    private String idColumName;
    private Schema schema;
    private RedisCache redisCache;
    private RelationGetIntace redisKeyGetInace;
    private Map<String,RelationGetIntace> columGetMap = new HashMap<>();
    private Map<String,String> field_columMap = new HashMap<>(5);
    private Map<String,ColumInit> columInitMap = new HashMap<>(5);
    private Map<String,RelationData> columRelationMap = new HashMap<>();
    private Map<String,RelationData> fieldRelationMap = new HashMap<>();

    public String getIdColumName() {
        return idColumName;
    }

    public void setIdColumName(String idColumName) {
        this.idColumName = idColumName;
    }

    public DBTable(String name) {
        this.name = name;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public void addColum(String columName, String fieldName){
        field_columMap.put(fieldName,columName);
    }

    public String getColumName(String fieldName){
        return field_columMap.get(fieldName);
    }

    public RelationData getRelationMap(String columName){
        return columRelationMap.get(columName);
    }

    public RelationData getRelationByFieldName(String fieldName){
        return fieldRelationMap.get(fieldName);
    }

    public ColumInit getColumInit(String columName){
        return columInitMap.get(columName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RedisCache getRedisCache() {
        return redisCache;
    }

    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    public void addColumInit(String columName, ColumInit columInit, RelationGetIntace relationGetIntace) {
        columInitMap.put(columName,columInit);
        columGetMap.put(columName,relationGetIntace);
    }

    public RelationGetIntace getRedisKeyGetInace() {
        return redisKeyGetInace;
    }

    public void setRedisKeyGetInace(RelationGetIntace redisKeyGetInace) {
        this.redisKeyGetInace = redisKeyGetInace;
    }

    public void putColumRelationMap(String columName, RelationData relationData) {
        if(!columRelationMap.containsKey(columName)){
            columRelationMap.put(columName,relationData);
        }
    }

    public void addRelationData(RelationData relationData) {
        fieldRelationMap.put(relationData.getFieldName(),relationData);
    }

    public Map<String, RelationGetIntace> getColumGetMap() {
        return columGetMap;
    }
}
