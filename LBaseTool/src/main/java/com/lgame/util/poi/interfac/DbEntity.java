package com.lgame.util.poi.interfac;

import com.lgame.util.poi.module.ExcelConfig;
import com.lgame.util.poi.module.PersonalityConfig;

import java.util.Map;

/**
 * Created by leroy:656515489@qq.com
 * 2018/4/20.
 */
public interface DbEntity {

    /**
     * 数据库字段---字段对应的value
     * @param dataEntity
     * @return
     */
    public DbEntity Instance(int row, Map<String,String> dataEntity, ExcelConfig config,PersonalityConfig personalityConfig);
    public void setNew(boolean isNew);
    public boolean isNew();
    public int getRow();
    public Map<String, String> getDataEntity();
    public String getUniqueId();
    public String getUpdateSql();
}
