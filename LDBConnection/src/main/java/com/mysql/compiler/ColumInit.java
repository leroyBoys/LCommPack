package com.mysql.compiler;

import com.lgame.util.PrintTool;
import com.mysql.entity.SqlTypeToJava;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/2.
 */
public class ColumInit{
    private SqlTypeToJava sqlTypeToJava;
    public void setSqlTypeToJava(SqlTypeToJava sqlTypeToJava) {
        this.sqlTypeToJava = sqlTypeToJava;
    }

    public void set(Object obj, ResultSet rs, String colum) throws SQLException {
        try {
            this.set(obj,sqlTypeToJava.get(rs,colum));
        }catch (Exception ex){
            PrintTool.error("class:"+obj.getClass().getSimpleName()+"  colum:"+colum+" not find match sqlTypeToJava",ex);
        }
    }

    public void set(Object obj, ResultSet rs, int index) throws SQLException {
        this.set(obj,sqlTypeToJava.get(rs,index));
    }
    public void set(Object obj,Object v){}
}
