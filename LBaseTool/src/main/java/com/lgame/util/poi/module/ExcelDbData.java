package com.lgame.util.poi.module;

import com.lgame.util.comm.RegexUtils;
import com.lgame.util.comm.StringTool;
import com.lgame.util.time.DateTimeTool;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leroy:656515489@qq.com
 * 2018/4/20.
 */
public class ExcelDbData {
    private static final String errorData = "!_e";
    private String columName;
    private DataTypeEnum dataTypeEnum;
    private ExcelConfig.ExcelColumConverter columConverter;

    public String getColumName() {
        return columName;
    }

    public ExcelDbData(String columName, DataTypeEnum dataTypeEnum,ExcelConfig.ExcelColumConverter columConverter) {
        this.columName = columName;
        this.dataTypeEnum = dataTypeEnum;
        this.columConverter = columConverter;
    }

    public DataTypeEnum getDataTypeEnum() {
        return dataTypeEnum;
    }

    public final static boolean isError(String msg){
        return errorData == msg;
    }

    public final static String getError(){
        return errorData;
    }

    public String getValue(String value) {
        return columConverter.converter(value);
    }

    public abstract static class DataMatch{
        public abstract boolean isMatch(String value);
        public String value(String value) {return value; }
    }

    public enum DataTypeEnum{
        Int("int", new DataMatch() {
            @Override
            public boolean isMatch(String value) {
                return RegexUtils.isInt(value);
            }
        }),
        Float("float", new DataMatch() {
            @Override
            public boolean isMatch(String value) {
                return RegexUtils.isNumeric(value);
            }
        }),
        Bool("boolean", new DataMatch() {
            @Override
            public boolean isMatch(String value) {
                return RegexUtils.isBoolean(value);
            }
        }),
        Str("string", new DataMatch() {
            @Override
            public boolean isMatch(String value) {
                return true;
            }

            @Override
            public String value(String value) {
                return value == null?value:value.replace('\'','\"');
            }
        }),
        Date("date", new DataMatch() {
            @Override
            public boolean isMatch(String value) {
                return !StringTool.isEmpty(value);
            }

            @Override
            public String value(String value) {
              /*  if(RegexUtils.isNumeric(value)){
                    return super.value(value);
                }
                return String.valueOf(DateTimeTool.getDateTimes(value));*/
                if(RegexUtils.isNumeric(value)){
                    return DateTimeTool.getDateTime(new Date(Long.valueOf(value)));
                }
                return value;
            }
        }),
        ;

        private String typeName;
        private DataMatch dataMatch;

        DataTypeEnum(String typeName, DataMatch dataMatch) {
            this.typeName = typeName;
            this.dataMatch = dataMatch;
        }

        public String getTypeName() {
            return typeName;
        }

        public DataMatch getDataMatch() {
            return dataMatch;
        }

        public static DataTypeEnum getDataTypeEnum(String typeName){
            DataTypeEnum dataTypeEnum = dataTypeEnumMap.get(typeName);
            return dataTypeEnum == null?DataTypeEnum.Str:dataTypeEnum;
        }
    }

    protected static final Map<String,DataTypeEnum> dataTypeEnumMap = new HashMap<>();
    static {
        for(DataTypeEnum excelDataTypeEnum: DataTypeEnum.values()){
            dataTypeEnumMap.put(excelDataTypeEnum.getTypeName(),excelDataTypeEnum);
        }
    }
}
