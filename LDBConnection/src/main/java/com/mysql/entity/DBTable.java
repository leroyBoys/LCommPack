package com.mysql.entity;

import com.mysql.compiler.ColumInit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leroy:656515489@qq.com
 * 2018/4/25.
 */
public class DBTable {
    private String name;
    private String idColumName;
    private Map<String,String> colum_fieldMap = new HashMap<>(5);
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

    public void addColum(String columName, String fieldName){
        colum_fieldMap.put(columName,fieldName);
    }

    public String getFieldName(String columName){
        return colum_fieldMap.get(columName);
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

    public void addColumInit(String columName, ColumInit columInit) {
        columInitMap.put(columName,columInit);
    }

    public boolean doExute(String columName, Object obj, Object metho_v) {
        ColumInit columInit = columInitMap.get(columName);
        if(columInit == null){
            return false;
        }
        columInit.set(obj,metho_v);
        return true;
    }

    public void addRelationData(DBRelation dbRelation, RelationData relationData) {
        columRelationMap.put(dbRelation.colum(),relationData);
        relationData.put(dbRelation.colum(),dbRelation.targetColum());
        if(!fieldRelationMap.containsKey(relationData.getFieldName())){
            fieldRelationMap.put(relationData.getFieldName(),relationData);
        }
    }
}