package com.mysql.entity;

import com.lgame.util.PrintTool;
import com.mysql.compiler.ColumInit;
import com.mysql.compiler.ScanEntitysTool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/7.
 */
public class MoreJdbcColumsArray extends JdbcColumsArray{

    public MoreJdbcColumsArray(String[] array, Map<String,Set<String>> relationFieldNameMap) {
        super(array);
        this.relationFieldNameMap = relationFieldNameMap;
    }

    public <T> T doExute(DBTable dbTable, ResultSet rs,Class<T> tClass,QueryResultData<T> resultData) throws Exception {
        int id = rs.getInt(dbTable.getIdColumName());
        if(id == 0){
            PrintTool.error(dbTable.getName()+":idKey is 0 maybe is error!!!please not use idKey = 0");
        }

        T t = resultData.getReultById(id);
        final boolean isNew = t == null;
        if(isNew){
            t = tClass.newInstance();
            resultData.put(id,t);
            resultData.add(t);
        }

        Object obj;
        String columName;
        RelationData relationData;
        Object reationObj;
        DBTable reationTable;

        Set<String> tmpSet;
        Set<String> objMap = new HashSet<>(columsArray.length);
        for (int i = 0, size = columsArray.length; i < size; ++i) {
            columName = get(i);

            ColumInit columInit = dbTable.getColumInit(columName);
            if(columInit!= null && isNew){
                obj = rs.getObject(i + 1);
                columInit.set(t,obj);
                continue;
            }

            relationData = dbTable.getRelationMap(columName);
            if(relationData == null){
                PrintTool.error(dbTable.getName()+":columName:"+columName+" not find from config");
                continue;
            }else if(objMap.contains(relationData.getFieldName())){
                continue;
            }

            objMap.add(relationData.getFieldName());

            reationTable = ScanEntitysTool.getDBTable(relationData.getFieldClass());
            if(reationTable == null){
                PrintTool.error(dbTable.getName()+":relation--class"+relationData.getFieldClass().getName()+" not find from config");
                continue;
            }
            //可以对一对多的对象也做缓存，这里暂时不做了，以后再扩展
          //  tmpMap = resultData.getFieldReultByFile(relationData.getFieldName());
            reationObj = relationData.createNew();

            tmpSet = relationFieldNameMap.get(relationData.getFieldName());
            for(String colum:tmpSet){
                ScanEntitysTool.doExute(reationTable,relationData.getTargetColum(colum),reationObj,rs.getObject(colum));
            }

            if(relationData.isOneToMany()){
                obj = relationData.getRelationGetIntace().get(t);
                if(obj == null){
                    obj = relationData.getNewInstance().create();
                    relationData.getColumInit().set(t,obj);
                }

                relationData.getNewInstance().add(obj,reationObj);
            }else {
                relationData.getColumInit().set(t,reationObj);
            }

        }

        return t;
    }

}
