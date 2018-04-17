package com.lgame.util.excel;

import java.util.Map;

/**
 * Created by Administrator on 2018/4/16.
 */
public interface SuplerExcelData {
    /**
     * 数据库字段---字段对应的value
     * @param dataEntity
     * @return
     */
    public SuplerExcelData Instance(Map<String,String> dataEntity, ExcelTempConfig config);
    public void setNew(boolean aNew);
    public String getUniqueId();
    public String getUpdateSql();
}
