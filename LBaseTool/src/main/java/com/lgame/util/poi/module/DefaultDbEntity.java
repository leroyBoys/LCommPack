package com.lgame.util.poi.module;

import com.lgame.util.poi.interfac.DbEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leroy:656515489@qq.com
 * 2018/4/20.
 */
public class DefaultDbEntity implements DbEntity {
    private Map<String,String> dataEntity = new HashMap<>();
    private int row;
    private String tableName;
    private boolean isNew = true;
    private String uniqueColumName;
    private String sql;

    public Map<String, String> getDataEntity() {
        return dataEntity;
    }

    public boolean isNew() {
        return isNew;
    }

    @Override
    public DbEntity Instance(int row, Map<String, String> dataEntity, ExcelConfig config,PersonalityConfig personalityConfig) {
        DefaultDbEntity defaultExcelData = new DefaultDbEntity();
        defaultExcelData.setTableName(config.getTableName());
        if(personalityConfig != null){
            dataEntity = personalityConfig.checkModifyRowData(dataEntity);
        }
        defaultExcelData.setDataEntity(dataEntity);
        defaultExcelData.setUniqueColumName(config.getIdColumName());
        defaultExcelData.setRow(row);
        return defaultExcelData;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public void setDataEntity(Map<String, String> dataEntity) {
        this.dataEntity = dataEntity;
    }

    public String getTableName() {
        return tableName;
    }

    public String getUniqueColumName() {
        return uniqueColumName;
    }

    public void setUniqueColumName(String uniqueColumName) {
        this.uniqueColumName = uniqueColumName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public String getUpdateSql() {
        if(sql == null){
            sql = isNew?getAddSql(dataEntity,tableName):getUpdateSql(dataEntity,tableName);
        }
        return sql;
    }

    @Override
    public String getUniqueId() {
        return dataEntity.get(uniqueColumName);
    }

    /**
     * 根据map和表名自动组装update-sql
     * @param obj
     * @param tableName
     * @return
     */
    protected String getUpdateSql(Map<String, String> obj, String tableName){
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(tableName);
        sql.append("  set ");
        int i = 0;
        for(String str :obj.keySet()){
            if(str.equals("id")){
                continue;
            }
            if(i > 0){
                sql.append(" , ");
            }
            sql.append(str).append("=");
            if(obj.get(str).equals("null")){
                sql.append("null");
            }else{
                sql.append("'").append(obj.get(str)).append("'");
            }
            i++;
        }
        sql.append("  where id = ").append(obj.get("id"));
        return sql.toString();
    }
    /**
     * 根据map和表名自动组装add-sql
     * @param obj
     * @param tableName
     * @return
     */
    public static String getAddSql(Map<String, String> obj, String tableName){
        StringBuilder sql = new StringBuilder();
        StringBuilder names = new StringBuilder();
        StringBuilder values = new StringBuilder();
        sql.append("insert into ").append(tableName);
        names.append("  ( ");
        values.append("  ( ");
        int i = 0;
        for(String str :obj.keySet()){
            if(str.equals(":dataError")){
                continue;
            }
            if(i > 0){
                names.append(" , ");
                values.append(" , ");
            }
            names.append("`").append(str).append("`");
            if(obj.get(str).equals("null")){
                values.append("null");
            }else{
                values.append("'").append(obj.get(str)).append("'");
            }
            i++;
        }
        names.append("  ) ");
        values.append("  ) ");
        sql.append(names).append("  values ").append(values);
        return sql.toString();
    }
}
