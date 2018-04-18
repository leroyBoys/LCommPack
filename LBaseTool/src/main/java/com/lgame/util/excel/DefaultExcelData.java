package com.lgame.util.excel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/16.
 */
public class DefaultExcelData implements SuplerExcelData {
    private Map<String,String> dataEntity = new HashMap<>();
    private String tableName;
    private boolean isNew = true;
    private String uniqueColumName;

    public Map<String, String> getDataEntity() {
        return dataEntity;
    }

    public boolean isNew() {
        return isNew;
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

    @Override
    public SuplerExcelData Instance(Map<String,String> dataEntity, ExcelTempConfig config) {
        DefaultExcelData defaultExcelData = new DefaultExcelData();
        defaultExcelData.setTableName(config.getTableName());
        defaultExcelData.setDataEntity(dataEntity);
        defaultExcelData.setUniqueColumName(config.getIdColumName());
        return defaultExcelData;
    }

    @Override
    public String getUpdateSql() {
        return isNew?getAddSql(dataEntity,tableName):getUpdateSql(dataEntity,tableName);
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
    protected String getAddSql(Map<String, String> obj, String tableName){
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
