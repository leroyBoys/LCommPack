package com.lgame.util.excel;

import com.lgame.util.comm.FormatDataTool;
import com.lgame.util.comm.StringTool;
import com.lgame.util.excel.bigdata.ReadBigExcel;
import com.lgame.util.exception.AppException;
import com.lgame.util.exception.TransformationException;
import com.lgame.util.json.JsonUtil;

import java.io.File;
import java.util.*;

/**
 * Created by Administrator on 2018/4/16.
 */
public class ExcelImportTool {
    private final static long maxSize = 1024*1024*50;
    public static void main(String[] args) throws Exception {
    }


    /**
     *
     * @param fileName 数据文件名字带路径
     * @param excelTmpFileName 模板名字带路径
     * @return
     */
    public static String importDBFromExcel(ExcelService excelService,boolean isCheckBeforeUpdate,int batchUpdateCount,SuplerExcelData suplerExcelData,String fileName,String excelTmpFileName) throws Exception {
        ExcelTempConfig excelTempConfig = getExcelTempConfig(excelTmpFileName);
        if(excelTempConfig == null){
            throw new AppException("模板不存在："+excelTmpFileName);
        }

        File file = new File(fileName);
        if(!file.exists()){
            throw new AppException("文件不存在："+fileName);
        }

        Map<String,SuplerExcelData> tmpMap = new HashMap<>();
        final ExcelDbDesc dbHead = new ExcelDbDesc();

        RowListener listener = (row, rowNum) -> {
            if(rowNum >=  excelTempConfig.getDataLineNum()){
               // suplerExcelData.Instance();
                if(dbHead.getDbArray() == null){
                    throw new TransformationException("模板:"+excelTmpFileName+" 配置选项第一行第二列不匹配，检查是否模板正确");
                }

                Map<String,String> map = new HashMap<>();
                for(int i = 0;i<row.length;i++){
                    String dbColum = dbHead.getDbArray()[i];
                    if(StringTool.isEmpty(dbColum)){
                        continue;
                    }
                    map.put(dbColum,row[i]);
                }
                SuplerExcelData entity = suplerExcelData.Instance(map,excelTempConfig);
                tmpMap.put(entity.getUniqueId(),entity);

                if(tmpMap.size() == batchUpdateCount){

                    _importDBFromExcel(tmpMap,isCheckBeforeUpdate,excelService,excelTempConfig);
                    tmpMap.clear();
                }
                return;
            }else if(rowNum == excelTempConfig.getHeadDataLineNum()){//
                if(!excelTempConfig.getHeadDataMap().isEmpty()){
                    dbHead.setDbArray(getDbArray(excelTempConfig.getHeadDataMap(),row));
                }else{
                    dbHead.setDbArray(row);
                }
                return;
            }

        };

        if(file.length()>maxSize){
            if(fileName.endsWith(".xls")){
                throw new AppException(fileName+"请另存为:"+fileName+"x");
            }

            new ReadBigExcel().processOneSheet(fileName,listener,0);
        }else {
            new POIReadData().read(fileName,listener,0);
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

    private static String[] getDbArray(Map<String, String> headDataMap, String[] row) {
        String[] tmpArray = new String[row.length];
        for(int i = 0;i<row.length;i++){
            String dbColum = headDataMap.get(getHeadDesc(row[i]));
            if(StringTool.isEmpty(dbColum)){
                continue;
            }
            tmpArray[i] = dbColum;
        }

        return tmpArray;
    }


    static ExcelTempConfig getExcelTempConfig(String fileName){
        List<String[]> readList =  new LinkedList<>();
        new POIReadData().read(fileName, (row, rowNum) -> readList.add(row), 3);
        if(readList.isEmpty()){
            return null;
        }

        String[] headDescv = readList.get(0);
        ExcelTempConfig tempConfig = new ExcelTempConfig();
        try {
            tempConfig.setTableName(headDescv[0]);
            tempConfig.setHeadDataLineNum(Integer.valueOf(headDescv[1]));
            tempConfig.setDataLineNum(Integer.valueOf(headDescv[2]));
            if(readList.size() == 1){//如果只有一行配置数据说明原始数据行就是db行
                return tempConfig;
            }
        }catch (Exception e){
            System.out.println(fileName+"模板数据第一行数据不正确:"+ JsonUtil.getJsonFromBean(headDescv));
            return null;
        }
        try {
            String[] headList = readList.get(1);
            String[] dbHeadColum = readList.get(2);
            Map<String,String> map = new HashMap<>();
            for(int i=0;i<headList.length;i++){
                if(StringTool.isEmpty(dbHeadColum[i])){
                    continue;
                }
                map.put(getHeadDesc(headList[i]),dbHeadColum[i]);
            }

            tempConfig.setHeadDataMap(map);
        }catch (Exception e){
            System.out.println(fileName+"模板数据第二，三行数据不正确:"+ e.getMessage());
            return null;
        }

        return tempConfig;
    }

    public static String getHeadDesc(String desc){
        int idex = desc.indexOf("（");
        if(idex >= 0){
            String bracket = desc.substring(desc.indexOf("（"),desc.indexOf("）")+1);
            return desc.replace(bracket, "");
        }
        return desc;
    }

    private static class ExcelDbDesc{
        private String[] dbArray = null;
        private boolean isEmpty =true;

        public String[] getDbArray() {
            return dbArray;
        }

        public void setDbArray(String[] dbArray) {
            this.dbArray = dbArray;
        }

        public boolean isEmpty() {
            return isEmpty;
        }

        public void setEmpty(boolean empty) {
            isEmpty = empty;
        }
    }
}
