package com.lgame.util.excel;

import com.lgame.util.comm.StringTool;
import com.lgame.util.excel.bigdata.ReadBigExcel;
import com.lgame.util.exception.AppException;
import com.lgame.util.exception.TransformationException;
import com.lgame.util.json.JsonUtil;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *  日期更是全转化为时间戳
 * Created by Administrator on 2018/4/16.
 */
public class ExcelImportTool {
    private final static long maxSize = 1024*1024*50;

    public static void main(String[] args) throws Exception {
        String smg = "abc";
        System.out.println(smg.replaceAll("\\([^}]*\\)",""));

/*
        importDBFromExcel(new ExcelService() {
            @Override
            public List<String> query(String sql)
            {
                System.out.println(sql);
                return null;
            }

            @Override
            public int excute(List sqls) {
                System.out.println(sqls);
                return 0;
            }
        },null,"D:\\ww.xls","");*/
    }

    public static String importDBFromExcel(ExcelService excelService,ExcelTempConfig tempConfig,String fileName,String excelTmpFileName) throws Exception{
        return importDBFromExcel(excelService,true,tempConfig,50,new DefaultExcelData(),fileName,excelTmpFileName);
    }

    public static String importDBFromExcel(ExcelService excelService,String fileName,String excelTmpFileName) throws Exception{
        return importDBFromExcel(excelService,true,null,50,new DefaultExcelData(),fileName,excelTmpFileName);
    }
    /**
     *
     * @param fileName 数据文件名字带路径
     * @param excelTmpFileName 模板名字带路径
     * @return
     */
    public static String importDBFromExcel(ExcelService excelService,boolean isCheckBeforeUpdate,ExcelTempConfig tempConfig,int batchUpdateCount,SuplerExcelData suplerExcelData,String fileName,String excelTmpFileName) throws Exception {
        ExcelTempConfig autoTempConfig = getAutoExcelTempConfig(fileName);
        if(autoTempConfig != null){
            tempConfig = autoTempConfig;
        }

        if(tempConfig == null){
            tempConfig = getExcelTempConfig(excelTmpFileName);
        }

        if(tempConfig == null){
            throw new AppException("模板不存在："+excelTmpFileName);
        }

        final ExcelTempConfig excelTempConfig = tempConfig;
        File file = new File(fileName);
        if(!file.exists()){
            throw new AppException("文件不存在："+fileName);
        }

        final Map<String,SuplerExcelData> tmpMap = new HashMap<>();
        final ExcelDbDesc dbHead = new ExcelDbDesc();
        final List<String> errorRows = new LinkedList<>();

        RowListener listener = (row, rowNum) -> {
            if(rowNum >=  excelTempConfig.getDataLineNum()){
                if(dbHead.getDbArray() == null){
                    errorRows.add("模板:"+excelTmpFileName+" 配置选项第一行第二列不匹配，检查是否模板正确");
                    return false;
                }else if(errorRows.size() > 20){
                    return false;
                }

                try {
                    Map<String,String> map = new HashMap<>();
                    ExcelTempConfig.ExcelDbData excelDbData;

                    StringBuilder errorMsg = null;
                    for(int i = 0;i<row.length;i++){
                        excelDbData = dbHead.getDbArray()[i];
                        if(excelDbData == null){
                            continue;
                        }

                        if(excelTempConfig.isCheckColumValueRight() && !excelDbData.getExcelDataTypeEnum().getDataType().isMatch(row[i])){
                            if(errorMsg == null){
                                errorMsg = new StringBuilder("第"+rowNum+"行出错，详细列数:");
                            }
                            errorMsg.append(i+1).append(",");
                            continue;
                        }
                        map.put(excelDbData.getColumNum(),excelDbData.getExcelDataTypeEnum().getDataType().value(row[i]));
                    }

                    if(errorMsg != null){
                        errorMsg.deleteCharAt(errorMsg.length()-1);
                        errorRows.add(errorMsg.toString());
                    }
                    SuplerExcelData entity = suplerExcelData.Instance(map,excelTempConfig);
                    tmpMap.put(entity.getUniqueId(),entity);


                }catch (Exception ex){
                    ex.printStackTrace();
                    errorRows.add("第"+rowNum+"行出错："+ex.getMessage());
                    return true;
                }

                if(tmpMap.size() == batchUpdateCount){
                    _importDBFromExcel(tmpMap,isCheckBeforeUpdate,excelService,excelTempConfig);
                    tmpMap.clear();
                }
                return true;
            }else if(rowNum == excelTempConfig.getHeadDataLineNum()){//
                dbHead.setDbArray(getDbArray(excelTempConfig,row));
                return true;
            }

            return true;
        };

        try {
            if(file.length()>maxSize){
                if(fileName.endsWith(".xls")){
                    throw new AppException(fileName+"请另存为:"+fileName+"x");
                }

                new ReadBigExcel().processOneSheet(fileName,listener,0);
            }else {
                new POIReadData().read(fileName,listener,0);
            }
        }catch (TransformationException ex){
            ex.printStackTrace();
        }


        if(!tmpMap.isEmpty()){
            _importDBFromExcel(tmpMap,isCheckBeforeUpdate,excelService,excelTempConfig);
        }
        return null;
    }

    private static void _importDBFromExcel(Map<String, SuplerExcelData> tmpMap, boolean isCheckBeforeUpdate, ExcelService excelService, ExcelTempConfig excelTempConfig) {
        StringBuilder sql = new StringBuilder();
        if(isCheckBeforeUpdate){
            sql.append("select ").append(excelTempConfig.getIdColumName()).append("  from  ").append(excelTempConfig.getTableName());
            sql.append("  where ").append(excelTempConfig.getIdColumName()).append("  in(").append(StringTool.getStringFromCollection(tmpMap.keySet())).append(")");
            List<String> uniqueIds = excelService.query(sql.toString());
            if(uniqueIds != null && !uniqueIds.isEmpty()){
                for(String uniqueId:uniqueIds){
                    tmpMap.get(uniqueId).setNew(false);
                }
            }
        }

       List<String> sqls = new LinkedList<>();
        for(SuplerExcelData data:tmpMap.values()){
            sqls.add(data.getUpdateSql());
        }
        excelService.excute(sqls);
    }

    private static ExcelTempConfig.ExcelDbData[] getDbArray(ExcelTempConfig config, String[] row) {
        ExcelTempConfig.ExcelDbData[] tmpArray = new ExcelTempConfig.ExcelDbData[row.length];
        for(int i = 0;i<row.length;i++){
            ExcelTempConfig.ExcelDbData data = config.getHeadDataMap().get(getHeadDesc(row[i]));
            if(data == null){
                continue;
            }
            tmpArray[i] = data;
        }

        return tmpArray;
    }

    /**
     * 根据模板获得配置信息
     * @param tempFileName
     * @return
     */
    public static ExcelTempConfig getExcelTempConfig(String tempFileName){
        List<String[]> readList =  new LinkedList<>();
        new POIReadData().read(tempFileName, (row, rowNum) -> {
            readList.add(row);
            return true;
        }, 5);
        if(readList.isEmpty()){
            return null;
        }

        return initExcelTempConfig(tempFileName,readList);
    }

    static ExcelTempConfig getAutoExcelTempConfig(String fileName){
        List<String[]> readList =  new LinkedList<>();
        if(!new POIReadData().read(fileName,"config", (row, rowNum) -> {
            readList.add(row);
            return true;
        }, 5)){
            return null;
        }
        return initExcelTempConfig(fileName,readList);
    }
    private static ExcelTempConfig initExcelTempConfig(String fileName,List<String[]> readList){
        if(readList.isEmpty()){
            return null;
        }

        String[] headDescv = readList.get(0);
        ExcelTempConfig tempConfig = new ExcelTempConfig();
        try {
            tempConfig.setTableName(headDescv[0]);
            tempConfig.setHeadDataLineNum(Integer.valueOf(headDescv[1]));
            tempConfig.setDataLineNum(Integer.valueOf(headDescv[2]));
            tempConfig.setIdColumName(headDescv[3]);
            tempConfig.setCheckColumValueRight(headDescv.length < 5?true:Boolean.valueOf(headDescv[4].trim()));
        }catch (Exception e){
            throw new TransformationException(fileName+"模板数据第一行数据不正确:"+ JsonUtil.getJsonFromBean(headDescv)+"  "+e.getMessage());
        }

        if(readList.size() < 4){
            throw new TransformationException(fileName+"模板数据不完整，必须超过四行具体数据类型：基准数据，头部数据，数据库数据，数据格式:");
        }

        try {
            String[] headList = readList.get(1);
            String[] dbHeadColum = readList.get(2);
            String[] dbTypes = readList.get(3);
            Map<String,ExcelTempConfig.ExcelDbData> map = new HashMap<>();
            for(int i=0;i<headList.length;i++){
                if(StringTool.isEmpty(dbHeadColum[i])){
                    continue;
                }

                ExcelTempConfig.ExcelDataTypeEnum excelDataTypeEnum = ExcelTempConfig.ExcelDataTypeEnum.Str;
                if(!StringTool.isEmpty(dbTypes[i])){
                    excelDataTypeEnum = ExcelTempConfig.getExcelDataTypeEnum(dbTypes[i].trim());
                    excelDataTypeEnum = excelDataTypeEnum==null? ExcelTempConfig.ExcelDataTypeEnum.Str:excelDataTypeEnum;
                }
                map.put(getHeadDesc(headList[i]),new ExcelTempConfig.ExcelDbData(dbHeadColum[i],excelDataTypeEnum));
            }

            tempConfig.setHeadDataMap(map);
        }catch (Exception e){
            throw new TransformationException(fileName+"模板数据第二，三,四行数据不正确:"+ e.getMessage());
        }

        return tempConfig;
    }


    public static String getHeadDesc(String desc){
        return desc.replaceAll("\\([^}]*\\)","");
    }

    private static class ExcelDbDesc{
        private ExcelTempConfig.ExcelDbData[] dbArray = null;

        public ExcelTempConfig.ExcelDbData[] getDbArray() {
            return dbArray;
        }

        public void setDbArray(ExcelTempConfig.ExcelDbData[] dbArray) {
            this.dbArray = dbArray;
        }
    }
}
