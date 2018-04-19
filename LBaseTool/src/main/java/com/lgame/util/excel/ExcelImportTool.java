package com.lgame.util.excel;

import com.lgame.util.comm.StringTool;
import com.lgame.util.excel.bigdata.ReadBigExcel;
import com.lgame.util.exception.AppException;
import com.lgame.util.exception.TransformationException;
import com.lgame.util.json.JsonUtil;
import com.lgame.util.thread.TaskPools;

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
    private final static long maxSize = 1;//1024*1024*50;

    public static void main(String[] args) throws Exception {
        importDBFromExcel(new ExcelService() {
            @Override
            public List<String> query(String sql)
            {
              //  System.out.println(sql);
                return null;
            }

            @Override
            public int excute(List sqls) {
                System.out.println("==>"+sqls.size());
                return 0;
            }
        },null,"D:\\ww.xlsx","");
    }

    public static ExcelProcess importDBFromExcel(ExcelService excelService,ExcelTempConfig tempConfig,String fileName,String excelTmpFileName) throws Exception{
        return importDBFromExcel(excelService,tempConfig,new DefaultExcelData(),fileName,excelTmpFileName);
    }

    public static ExcelProcess importDBFromExcel(ExcelService excelService,String fileName,String excelTmpFileName) throws Exception{
        return importDBFromExcel(excelService,null,new DefaultExcelData(),fileName,excelTmpFileName);
    }
    /**
     *
     * @param fileName 数据文件名字带路径
     * @param excelTmpFileName 模板名字带路径
     * @return
     */
    public static ExcelProcess importDBFromExcel(ExcelService excelService,ExcelTempConfig tempConfig,SuplerExcelData suplerExcelData,String fileName,String excelTmpFileName) throws Exception {
        long ss = System.currentTimeMillis();
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

        System.out.println("--->"+(System.currentTimeMillis()-ss)+" ms");
        ss = System.currentTimeMillis();
        System.out.println("=====>>>>>>>>");

        File file = new File(fileName);
        if(!file.exists()){
            throw new AppException("文件不存在："+fileName);
        }
        ExcelReadWrite excelReadWrite = new POIReadData();
        if(file.length()>maxSize){
            if(!fileName.endsWith(".xlsx")){
                throw new AppException(fileName+"请另存为:xlsx格式");
            }
            excelReadWrite = new ReadBigExcel();
        }
        ExcelProcess excelProcess = new ExcelProcess();
        TaskPools.addTask(excelProcess,excelService,tempConfig,suplerExcelData,fileName,excelTmpFileName,excelReadWrite);

        while (!excelProcess.isOver()){
            Thread.sleep(100);
        }

        System.out.println("=====>>>>>>>>"+excelProcess.getMsg());
        System.out.println(System.currentTimeMillis()-ss+" ms");
        return excelProcess;
    }

    /**
     * 根据模板获得配置信息
     * @param tempFileName
     * @return
     */
    public static ExcelTempConfig getExcelTempConfig(String tempFileName){
        if(StringTool.isEmpty(tempFileName)){
            return null;
        }

        List<String[]> readList =  new LinkedList<>();
        new POIReadData().read(tempFileName, (row, rowNum) -> {
            readList.add(row);
            return true;
        }, ExcelTempConfig.LineMaxCount);
        if(readList.isEmpty()){
            return null;
        }

        return initExcelTempConfig(tempFileName,readList);
    }

    static ExcelTempConfig getAutoExcelTempConfig(String fileName){
        List<String[]> readList =  new LinkedList<>();
        new POIReadData().read(fileName,"config", (row, rowNum) -> {
            readList.add(row);
            return true;
        }, ExcelTempConfig.LineMaxCount);

        if(readList.isEmpty()){
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
            tempConfig.setConfigHeadRow(headDescv);
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

    static class ExcelDbDesc{
        private ExcelTempConfig.ExcelDbData[] dbArray = null;

        public ExcelTempConfig.ExcelDbData[] getDbArray() {
            return dbArray;
        }

        public void setDbArray(ExcelTempConfig.ExcelDbData[] dbArray) {
            this.dbArray = dbArray;
        }
    }
}
