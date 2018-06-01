package com.lgame.util.poi.module;

import java.util.HashMap;
import java.util.Map;

/**
 *  个性化配置
 * Created by leroy:656515489@qq.com
 * 2018/6/1.
 */
public abstract class PersonalityConfig {
    /** colum的数据类型强制格式化转换器 */
    private final Map<String,ExcelConfig.ExcelColumConverter> excelColumConverterMap = new HashMap<>();
    public final void addExcelColumConverter(String columName,ExcelConfig.ExcelColumConverter excelColumConverter){
        excelColumConverterMap.put(columName,excelColumConverter);
    }

    public final Map<String, ExcelConfig.ExcelColumConverter> getExcelColumConverterMap() {
        return excelColumConverterMap;
    }

    public final Map<String,String> checkModifyRowData(Map<String,String> rowData){
        Map<String,String> rowData2 = modifyRowData(rowData);
        if(rowData2 == null||rowData2.isEmpty()){
            return rowData;
        }
        return rowData2;
    }

    public final String[] checkModifyColumArray(String[] columArray){
        String[] columArray2 = modifyColumArray(columArray);
        if(columArray2 == null){
            return columArray;
        }
        return columArray2;
    }

    /**
     * 对原row修改，返回null表示不修改
     * @param rowData
     * @return
     */
    public abstract Map<String,String> modifyRowData(Map<String,String> rowData);

    /**
     * 对原columArray修改，返回null表示不修改
     * @param columArray
     * @return
     */
    public abstract String[] modifyColumArray(String[] columArray);
}
