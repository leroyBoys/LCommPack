package com.lgame.util.poi;

import com.lgame.util.PrintTool;
import com.lgame.util.comm.StringTool;
import com.lgame.util.exception.AppException;
import com.lgame.util.exception.TransformationException;
import com.lgame.util.json.JsonUtil;
import com.lgame.util.poi.even.EvenExcelReader;
import com.lgame.util.poi.interfac.DbEntity;
import com.lgame.util.poi.interfac.DbService;
import com.lgame.util.poi.interfac.PoiReader;
import com.lgame.util.poi.module.*;
import com.lgame.util.poi.user.UserExcelReader;

import java.io.File;
import java.util.*;

/**
 * Created by leroy:656515489@qq.com
 * 2018/4/20.
 */
public class ExcelHelper {
    private final static int maxSize = 1024*1024*1;
    protected final static Map<String,ExcelProcess> excelProcessMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Map<String, ExcelConfig.ExcelColumConverter> excelColumConverterMap = new HashMap<>();
        excelColumConverterMap.put("type",new ExcelConfig.ExcelColumConverter(){
            @Override
            public String converter(String value) {
                if("天猫".equals(value)){
                    value="11";
                }else {
                    value="12";
                }

                return super.converter(value);
            }
        });

        ExcelHelper.importDBFromExcel(new DbService() {
            @Override
            public List<String> queryExistIds(String sql) {
                return null;
            }

            @Override
            public boolean excute(String s) {
                return false;
            }

            @Override
            public boolean excute(List<String> list) {
                return false;
            }

            @Override
            public boolean insertBatchs(String tableName, List<Map<String, String>> list, String[] strings, int i) {
                return false;
            }
        },"D:\\shop.xls","D:\\test.xls",null);


    }

    public static ExcelProcess importDBFromExcel(DbService dbService, String fileName) throws AppException {
        return importDBFromExcel(dbService,null, new DefaultDbEntity(),fileName,null,null);
    }

    /**
     *
     * @param dbService 数据库操作接口
     * @param fileName 读取的excel
     * @param excelTmpFileName  db配置映射 excel
     * @return
     * @throws AppException
     */
    public static ExcelProcess importDBFromExcel(DbService dbService, String fileName, String excelTmpFileName) throws AppException {
        return importDBFromExcel(dbService,new DefaultDbEntity(),fileName,excelTmpFileName,null);
    }

    /**
     *
     * @param dbService 数据库操作接口
     * @param fileName 读取的excel
     * @param personalityConfig 特殊化配置
     * @return
     * @throws AppException
     */
    public static ExcelProcess importDBFromExcel(DbService dbService, String fileName,PersonalityConfig personalityConfig) throws AppException {
        return importDBFromExcel(dbService,new DefaultDbEntity(),fileName,null,personalityConfig);
    }

    /**
     *
     * @param dbService 数据库操作接口
     * @param fileName 读取的excel
     * @param excelTmpFileName db配置映射 excel
     * @param personalityConfig 特殊化配置
     * @return
     * @throws AppException
     */
    public static ExcelProcess importDBFromExcel(DbService dbService, String fileName, String excelTmpFileName, PersonalityConfig personalityConfig) throws AppException {
        return importDBFromExcel(dbService,new DefaultDbEntity(),fileName,excelTmpFileName,personalityConfig);
    }

    /**
     *
     * @param dbService 数据库操作接口
     * @param dbEntity 对应的数据库类（可使用null，自动生成）
     * @param fileName 读取的excel
     * @param excelTmpFileName db配置映射 excel
     * @param personalityConfig 特殊化配置
     * @return
     * @throws AppException
     */
    public static synchronized ExcelProcess importDBFromExcel(DbService dbService,DbEntity dbEntity, String fileName, String excelTmpFileName,PersonalityConfig personalityConfig) throws AppException{
        return importDBFromExcel(dbService,null,dbEntity,fileName,excelTmpFileName,personalityConfig);
    }

    /**
     *
     * @param dbService 数据库操作接口
     * @param config db配置（从excelTmpFileName或者config sheet读取的内容）
     * @param dbEntity 对应的数据库类（可使用null，自动生成）
     * @param fileName 读取的excel
     * @param excelTmpFileName db配置映射 excel
     * @param personalityConfig 特殊化配置
     * @return
     * @throws AppException
     */
    private static synchronized ExcelProcess importDBFromExcel(DbService dbService, ExcelConfig config, DbEntity dbEntity, String fileName, String excelTmpFileName,PersonalityConfig personalityConfig) throws AppException {
        if(!validateExcel(fileName)){
            throw new AppException("文件名不是excel格式"+fileName);
        }
        ExcelProcess excelProcess = excelProcessMap.get(fileName);
        if(excelProcess != null){
            return excelProcess;
        }

        PrintTool.outTime("1","loadHead");

        ExcelConfig autoTempConfig = getAutoExcelTempConfig(fileName);
        if(autoTempConfig != null){
            config = autoTempConfig;
        }

        if(config == null){
            config = getExcelTempConfig(excelTmpFileName);
        }

        if(config == null){
            throw new AppException("模板数据找不到："+excelTmpFileName);
        }

        PrintTool.outTime(ExcelProcess.class.getName(),"loadHead fileName:"+fileName);

        File file = new File(fileName);
        if(!file.exists()){
            throw new AppException("文件不存在："+fileName);
        }

        PoiReader poiReader = new UserExcelReader();
        System.out.println(file.length());
        if(file.length()>maxSize){
            poiReader = new EvenExcelReader();
        }

        excelProcess = new ExcelProcess();
        excelProcessMap.put(fileName,excelProcess);
        PoiReader finalPoiReader = poiReader;
        if(personalityConfig != null){
            config.setExcelColumConverterMap(personalityConfig.getExcelColumConverterMap());
            config.setColumArray(personalityConfig.checkModifyColumArray(config.getColumArray()));
        }
        ExcelConfig finalConfig = config;
        ExcelProcess finalExcelProcess = excelProcess;
        new Thread(
                () -> finalExcelProcess.excute(dbService, finalConfig,dbEntity,fileName,excelTmpFileName,finalPoiReader,personalityConfig)
        ).start();

        return excelProcess;
    }


    /**
     * 根据模板获得配置信息
     * @param tempFileName
     * @return
     */
    public static ExcelConfig getExcelTempConfig(String tempFileName){
        if(StringTool.isEmpty(tempFileName)){
            return null;
        }

        System.out.println("read:tempFileName:"+tempFileName);
        List<String[]> readList =  new LinkedList<>();
        new UserExcelReader().read(tempFileName,new DefaultRowListener(){
            @Override
            public boolean read(String[] row, int rowNum) {
                readList.add(row);
                return true;
            }
        },ExcelConfig.LineMaxCount);

        if(readList.isEmpty()){
            return null;
        }

        return initExcelTempConfig(tempFileName,readList);
    }

    /**
     *
     * @param fileName
     * @return
     */
    static ExcelConfig getAutoExcelTempConfig(String fileName){
        List<String[]> readList =  new LinkedList<>();
        new EvenExcelReader().read(fileName,ExcelConfig.sheetName,new DefaultRowListener(){
            @Override
            public boolean read(String[] row, int rowNum) {
                readList.add(row);
                return true;
            }
        }, ExcelConfig.LineMaxCount);

        if(readList.isEmpty()){
            return null;
        }

        return initExcelTempConfig(fileName,readList);
    }

    /**
     *
     * @param fileName
     * @param readList
     * @return
     */
    private static ExcelConfig initExcelTempConfig(String fileName,List<String[]> readList){
        if(readList.isEmpty()){
            return null;
        }

        String[] headDescv = readList.get(0);
        ExcelConfig tempConfig = new ExcelConfig();
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
            String[] dbTypes = readList.size()>3? readList.get(3):null;
            Map<String,ExcelDbData> map = new HashMap<>();

            ExcelConfig.ExcelColumConverter defaultConverter = new ExcelConfig.ExcelColumConverter();
            Set<String> columSet = new HashSet<>(headList.length);
            for(int i=0;i<headList.length;i++){
                if(dbHeadColum.length - 1 < i || StringTool.isEmpty(dbHeadColum[i])){
                    continue;
                }

                ExcelDbData.DataTypeEnum excelDataTypeEnum = ExcelDbData.DataTypeEnum.Str;
                if(dbTypes != null && dbTypes.length-1>=i && !StringTool.isEmpty(dbTypes[i])){
                    excelDataTypeEnum = ExcelDbData.DataTypeEnum.getDataTypeEnum(dbTypes[i].trim());
                }

                ExcelConfig.ExcelColumConverter columConverter = tempConfig.getExcelColumConverter(dbHeadColum[i]);
                columConverter = columConverter == null?defaultConverter: columConverter;
                map.put(getHeadDesc(headList[i]),new ExcelDbData(dbHeadColum[i],excelDataTypeEnum,columConverter));
                columSet.add(dbHeadColum[i]);
            }

            tempConfig.setHeadDataMap(map);

            String[] dbColumArray = new String[columSet.size()];
            columSet.toArray(dbColumArray);
            tempConfig.setColumArray(dbColumArray);
        }catch (Exception e){
            e.printStackTrace();
            throw new TransformationException(fileName+"模板数据第二，三,四行数据不正确:"+ e.getMessage());
        }

        return tempConfig;
    }


    protected static String getHeadDesc(String desc){
        return desc.replaceAll("\\([^}]*\\)","");
    }

    /**
     * @描述：验证excel文件
     * @参数：@param fileName
     * @参数：@return
     * @返回值：boolean
     */
    private static boolean validateExcel(String fileName) {
        /**
         * 检查文件名是否为空或者是否是Excel格式的文件
         */
        if (fileName == null || !(ExcelHelper.isExcel2003(fileName) || ExcelHelper.isExcel2007(fileName))) {
            return false;
        }
        return true;
    }

    /**
     * @描述：是否是2003的excel，返回true是2003
     * @时间：2011-8-9 下午03:20:49
     * @参数：@param fileName
     * @参数：@return
     * @返回值：boolean
     */
    public static boolean isExcel2003(String fileName) {
        return fileName.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * @描述：是否是2007的excel，返回true是2007
     * @参数：@param fileName
     * @参数：@return
     * @返回值：boolean
     */
    public static boolean isExcel2007(String fileName) {
        return fileName.matches("^.+\\.(?i)(xlsx)$");
    }
}
