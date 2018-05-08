package com.mysql.entity;

import com.mysql.compiler.ScanEntitysTool;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/4.
 */
public class JdbcColumsArray {
    protected String[] columsArray = null;
    protected Map<String,Set<String>> relationFieldNameMap;


    public JdbcColumsArray(String[] array) {
        columsArray = array;
    }

    public String get(int idex){
        return columsArray[idex];
    }

    public int size() {
        return columsArray.length;
    }

    public Map<String, Set<String>> getRelationFieldNameMap() {
        return relationFieldNameMap;
    }

    public <T> T doExute(DBTable dbTable, ResultSet rs, Class<T> tClass,QueryResultData<T> resultData) throws Exception {
        T t = tClass.newInstance();
        resultData.add(t);
        for (int i = 0, size = columsArray.length; i < size; ++i) {
            ScanEntitysTool.doExute(dbTable,get(i),t,rs.getObject(i + 1));
        }
        return t;
    }

    public <T> T doExuteOnlyOne(DBTable dbTable, ResultSet rs, Class<T> tClass) throws Exception {
        T t = tClass.newInstance();
        for (int i = 0, size = columsArray.length; i < size; ++i) {
            ScanEntitysTool.doExute(dbTable,get(i),t,rs.getObject(i + 1));
        }
        return t;
    }
}
