package com.lgame.util.poi.interfac;

import com.lgame.util.poi.module.ExcelDbData;

/**
 * Created by leroy:656515489@qq.com
 * 2018/4/20.
 */
public class DataCheckService {
    public String getValue(ExcelDbData.DataTypeEnum dataTypeEnum, String value){
        try {
            if(dataTypeEnum.getDataMatch().isMatch(value)){
                return dataTypeEnum.getDataMatch().value(value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ExcelDbData.getError();
    }

    public static class DataNoCheckService extends DataCheckService{
        public String getValue(ExcelDbData.DataTypeEnum dataTypeEnum, String value){
            try {
                return dataTypeEnum.getDataMatch().value(value);
            }catch (Exception e){
                e.printStackTrace();
            }
            return ExcelDbData.getError();
        }
    }
}
