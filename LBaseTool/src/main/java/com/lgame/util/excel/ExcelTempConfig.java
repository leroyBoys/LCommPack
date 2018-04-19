package com.lgame.util.excel;

import com.lgame.util.comm.RegexUtils;
import com.lgame.util.comm.StringTool;
import com.lgame.util.time.DateTimeTool;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 行
 * sheetname:config
 * 第一行 table_name,headDescLine(行号),DbStartLine(行号),id_columName(唯一值的列名)，是否检查数据库存在这个记录,是否检测数据正确性,批量导入数据数量
 * 第二行 headDescLine
 * 第三行 DbColumLine
 * 第四行 DataTypeLine(数据类型)
 * excel模板配置
 * Created by Administrator on 2018/4/16.
 */
public class ExcelTempConfig {
    /** 配置模板最大行 */
    public static final int LineMaxCount = 4;
    private String tableName;//第一行首列
    private int headDataLineNum;//第一行第二列；头部参照行号;
    private int dataLineNum;//第一行第三列：数据开始行号
    private String idColumName;//第一行第四列：唯一值列名
    private boolean isCheckDbBeforeUpdate = true;//第一行第五列：导入时候是否检测数据库是否存在这个记录
    private boolean isCheckColumValueRight = true;//第一行第六列：导入时候是否检测数值是否正确
    private int updateBatchCount = 30;//第一行第7列：批量插入数量

    /**
     *  头部数据与数据库对应字段映射，如果为空表示头部数据即数据库字段
     */
    private Map<String,ExcelDbData> headDataMap = new HashMap<>();

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIdColumName() {
        return idColumName;
    }

    public boolean isCheckColumValueRight() {
        return isCheckColumValueRight;
    }

    public boolean isCheckDbBeforeUpdate() {
        return isCheckDbBeforeUpdate;
    }

    public int getUpdateBatchCount() {
        return updateBatchCount;
    }

    public void setCheckColumValueRight(boolean checkColumValueRight) {
        isCheckColumValueRight = checkColumValueRight;
    }

    public void setIdColumName(String idColumName) {
        this.idColumName = idColumName;
    }

    public Map<String, ExcelDbData> getHeadDataMap() {
        return headDataMap;
    }

    public void setHeadDataMap(Map<String, ExcelDbData> headDataMap) {
        this.headDataMap = headDataMap;
    }

    public void setConfigHeadRow(String[] headDescv) {
        this.setTableName(headDescv[0]);
        this.setHeadDataLineNum(Integer.valueOf(headDescv[1]));
        this.setDataLineNum(Integer.valueOf(headDescv[2]));
        this.setIdColumName(headDescv[3]);

        if(headDescv.length > 4){
            this.isCheckDbBeforeUpdate = Boolean.valueOf(headDescv[4].trim());
        }

        if(headDescv.length > 5){
            this.isCheckColumValueRight = Boolean.valueOf(headDescv[5].trim());
        }
        if(headDescv.length > 6){
            this.updateBatchCount = Math.max(Integer.valueOf(headDescv[6].trim()),updateBatchCount);
        }
    }

    protected static class ExcelDbData{
        private String columNum;
        private ExcelDataTypeEnum excelDataTypeEnum;

        public ExcelDbData(String columNum, ExcelDataTypeEnum excelDataTypeEnum) {
            this.columNum = columNum;
            this.excelDataTypeEnum = excelDataTypeEnum;
        }

        public String getColumNum() {
            return columNum;
        }

        public ExcelDataTypeEnum getExcelDataTypeEnum() {
            return excelDataTypeEnum;
        }
    }

    protected enum  ExcelDataTypeEnum{
        Int("int", new ExcelDataType() {
            @Override
            public boolean isMatch(String value) {
                return RegexUtils.isInt(value);
            }
        }),
        Float("float", new ExcelDataType() {
            @Override
            public boolean isMatch(String value) {
                return RegexUtils.isNumeric(value);
            }
        }),
        Bool("boolean", new ExcelDataType() {
            @Override
            public boolean isMatch(String value) {
                return RegexUtils.isBoolean(value);
            }
        }),
        Str("string", new ExcelDataType() {
            @Override
            public boolean isMatch(String value) {
                return true;
            }
        }),
        Date("date", new ExcelDataType() {
            @Override
            public boolean isMatch(String value) {
                return !StringTool.isEmpty(value);
            }

            @Override
            public String value(String value) {
                if(RegexUtils.isNumeric(value)){
                    return super.value(value);
                }
                return String.valueOf(DateTimeTool.getDateTime(value).getTime());
            }
        }),
        ;

        private String columName;
        private ExcelDataType dataType;

        ExcelDataTypeEnum(String columName, ExcelDataType dataType) {
            this.columName = columName;
            this.dataType = dataType;
        }

        public String getColumName() {
            return columName;
        }

        public ExcelDataType getDataType() {
            return dataType;
        }
    }

    protected static final Map<String,ExcelDataTypeEnum> excelDataTypeEnumMap = new HashMap<>();
    static {
        for(ExcelDataTypeEnum excelDataTypeEnum:ExcelDataTypeEnum.values()){
            excelDataTypeEnumMap.put(excelDataTypeEnum.getColumName(),excelDataTypeEnum);
        }
    }

    public static ExcelDataTypeEnum getExcelDataTypeEnum(String name){
        return excelDataTypeEnumMap.get(name.toLowerCase());
    }

    protected abstract static class ExcelDataType{
        public abstract boolean isMatch(String value);
        public String value(String value){return value;}
    }
}
