package com.lgame.util.excel;

import java.util.HashMap;
import java.util.Map;

/**
 * excel模板配置
 * Created by Administrator on 2018/4/16.
 */
public class ExcelTempConfig {
    private String tableName;//第一行首列
    private int headDataLineNum;//第一行第二列；头部参照行号;
    private int dataLineNum;//第一行第三列：数据开始行号
    private String idColumName;//第一行第四列：唯一值列名
    /**
     *  头部数据与数据库对应字段映射，如果为空表示头部数据即数据库字段
     */
    private Map<String,String> headDataMap = new HashMap<>();

    /**
     * 如果为0表示数据原始数据与数据库字段一致；
     * @return
     */
    public int getHeadDataLineNum() {
        return headDataLineNum;
    }

    public void setHeadDataLineNum(int headDataLineNum) {
        this.headDataLineNum = headDataLineNum;
    }

    public int getDataLineNum() {
        return dataLineNum;
    }

    public void setDataLineNum(int dataLineNum) {
        this.dataLineNum = dataLineNum;
    }

    public Map<String, String> getHeadDataMap() {
        return headDataMap;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIdColumName() {
        return idColumName;
    }

    public void setIdColumName(String idColumName) {
        this.idColumName = idColumName;
    }

    public void setHeadDataMap(Map<String, String> headDataMap) {
        this.headDataMap = headDataMap;
    }
}
