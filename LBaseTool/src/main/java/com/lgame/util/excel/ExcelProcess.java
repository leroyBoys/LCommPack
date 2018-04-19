package com.lgame.util.excel;

import com.lgame.util.comm.StringTool;
import com.lgame.util.excel.bigdata.ReadBigExcel;
import com.lgame.util.exception.AppException;
import com.lgame.util.exception.TransformationException;
import com.lgame.util.thread.TaskIndieThread;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/4/16.
 */
public class ExcelProcess implements TaskIndieThread{
    private final static long maxSize = 1024*1024*50;
    private AtomicInteger faileCount = new AtomicInteger();//失败数量
    private AtomicInteger sucInsertCount = new AtomicInteger();//新增数量
    private AtomicInteger sucUpdateCount = new AtomicInteger();//更新数量
    private int dataAllCount;//总数据量
    private int excuteLineNum;//进行到当前行号
    private List<String> errorRows = new LinkedList<>();//错误信息
    private Status status = Status.Default;

    public ExcelProcess excute(ExcelService excelService,ExcelTempConfig excelTempConfig,SuplerExcelData suplerExcelData,String fileName,String excelTmpFileName,ExcelReadWrite excelReadWrite){

        final Map<String,SuplerExcelData> tmpMap = new HashMap<>();
        final ExcelImportTool.ExcelDbDesc dbHead = new ExcelImportTool.ExcelDbDesc();


        RowListener listener = (row, rowNum) -> {
            if(rowNum >=  excelTempConfig.getDataLineNum()){
                if(dbHead.getDbArray() == null){
                    errorRows.add("模板:"+excelTmpFileName+" 配置选项第一行第二列不匹配，检查是否模板正确");
                    return false;
                }
                dataAllCount++;
                excuteLineNum = rowNum;

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
                        if(errorRows.size() > 20){
                            return false;
                        }
                    }

                    if(map.isEmpty()){
                        return true;
                    }
                    SuplerExcelData entity = suplerExcelData.Instance(rowNum,map,excelTempConfig);
                    boolean iscontain = tmpMap.put(entity.getUniqueId(),entity) != null;
                    if(iscontain){
                        dataAllCount--;
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    faileCount.getAndAdd(1);
                    errorRows.add("第"+rowNum+"行出错："+ex.getMessage());
                    return true;
                }

                if(tmpMap.size() == excelTempConfig.getUpdateBatchCount()){
                    _importDBFromExcel(rowNum,tmpMap,excelService,excelTempConfig);
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
            excelReadWrite.read(fileName,listener,0);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

        if(!tmpMap.isEmpty()){
            _importDBFromExcel(dataAllCount,tmpMap,excelService,excelTempConfig);
        }

        synchronized (this){
            int all = sucInsertCount.get()+sucUpdateCount.get()+faileCount.get();
            if(all == dataAllCount){
                status = Status.Over;
            }else{
                status = Status.endRead;
            }
        }


        return null;
    }

    private void updateStatus() {
        synchronized (this){
            if(status == Status.endRead){
                this.status = Status.Over;
            }
        }

        System.out.println("====>终止行号:"+excuteLineNum+";完成总数据:"+dataAllCount+",更新:"+sucUpdateCount+",新增："+sucInsertCount+",失败："+faileCount+"  "+errorRows.size());
    }

    private void _importDBFromExcel(int rowNum,Map<String, SuplerExcelData> tmpMap, ExcelService excelService, ExcelTempConfig excelTempConfig) {

        int all = tmpMap.size();
        int update = 0;
        StringBuilder sql = new StringBuilder();
        if(excelTempConfig.isCheckDbBeforeUpdate()){
            sql.append("select ").append(excelTempConfig.getIdColumName()).append("  from  ").append(excelTempConfig.getTableName());
            sql.append("  where ").append(excelTempConfig.getIdColumName()).append("  in(").append(StringTool.getStringFromCollection(tmpMap.keySet())).append(")");
            List<String> uniqueIds = null;
            try {
                uniqueIds = excelService.query(sql.toString());
            }catch (Exception ex){
                ex.printStackTrace();
                errorRows.add("第"+(all-rowNum)+"-"+rowNum+" 列导入数据库出错;"+ex.getMessage());
                faileCount.getAndAdd(all);
                updateStatus();
                return;
            }
            if(uniqueIds != null && !uniqueIds.isEmpty()){
                for(String uniqueId:uniqueIds){
                    tmpMap.get(uniqueId).setNew(false);
                    update++;
                }
            }
        }

        List<String> sqls = new LinkedList<>();
        for(SuplerExcelData data:tmpMap.values()){
            sqls.add(data.getUpdateSql());
        }
        try {
            excelService.excute(sqls);
            if(update != 0){
                sucUpdateCount.getAndAdd(update);
            }
            if(update != all){
                sucInsertCount.getAndAdd(all-update);
            }
            updateStatus();
        }catch (Exception ex){
            ex.printStackTrace();
            errorRows.add("第"+(all-rowNum)+"-"+rowNum+" 列导入数据库出错;"+ex.getMessage());
            faileCount.getAndAdd(all);
            updateStatus();
        }

    }

    private ExcelTempConfig.ExcelDbData[] getDbArray(ExcelTempConfig config, String[] row) {
        ExcelTempConfig.ExcelDbData[] tmpArray = new ExcelTempConfig.ExcelDbData[row.length];
        for(int i = 0;i<row.length;i++){
            ExcelTempConfig.ExcelDbData data = config.getHeadDataMap().get(ExcelImportTool.getHeadDesc(row[i]));
            if(data == null){
                continue;
            }
            tmpArray[i] = data;
        }

        return tmpArray;
    }

    @Override
    public void doExcute(Object... objects) {
        excute((ExcelService)objects[0],(ExcelTempConfig)objects[1],(SuplerExcelData)objects[2],String.valueOf(objects[3]),String.valueOf(objects[4]),(ExcelReadWrite)objects[5]);
    }

    public boolean isOver() {
        return status == Status.Over;
    }

    public String getMsg() {
        return "终止行号:"+excuteLineNum+";完成总数据:"+dataAllCount+",更新:"+sucUpdateCount+",新增："+sucInsertCount+",失败："+faileCount;
    }

    public static enum Status{
        Default,endRead,Over
    }
}
