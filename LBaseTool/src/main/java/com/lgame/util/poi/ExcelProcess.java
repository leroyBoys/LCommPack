package com.lgame.util.poi;

import com.lgame.util.PrintTool;
import com.lgame.util.comm.StringTool;
import com.lgame.util.poi.interfac.*;
import com.lgame.util.poi.module.DefaultRowListener;
import com.lgame.util.poi.module.ExcelConfig;
import com.lgame.util.poi.module.ExcelDbData;
import com.lgame.util.thread.BaseThreadPools;
import com.lgame.util.thread.TaskIndieThread;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.lgame.util.poi.ExcelHelper.getHeadDesc;

/**
 * Created by leroy:656515489@qq.com
 * 2018/4/20.
 */
public class ExcelProcess {
    private AtomicInteger faileCount = new AtomicInteger();//失败数量
    private AtomicInteger sucInsertCount = new AtomicInteger();//新增数量
    private AtomicInteger sucUpdateCount = new AtomicInteger();//更新数量
    private AtomicInteger repeateCount = new AtomicInteger();//更新数量
    private int dataAllCount;//总数据量
    private int excuteLineNum;//进行到当前行号
    private List<String> errorRows = new LinkedList<>();//错误信息
    private String importMsg;//结束提示信息
    protected ExcelProcess(){}
    public final BaseThreadPools user_save_pools = new BaseThreadPools(4, 4,
            0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>());//线程池

    public void addTask(final TaskIndieThread st, final Object... objects) {
        user_save_pools.execute(() -> {
            try {
                st.doExcute(objects);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(st.getClass().getSimpleName() + "-ExcelProcess-TaskPools->" +ex.getMessage());
            }
        });
    }

    Map<String,DbEntity> tmpMap = new HashMap<>();
    public void excute(DbService dbService, ExcelConfig config, DbEntity dbEntity, String fileName, String excelTmpFileName, PoiReader reader){

        DataCheckService dataCheckService = config.isCheckColumValueRight()? new DataCheckService():new DataCheckService.DataNoCheckService();

        RowListener listener = new DefaultRowListener(){
            @Override
            public boolean read(String[] row, int rowNum) {
                if(rowNum >=  config.getDataLineNum()){
                    if(config.getHeadDataArray() == null){
                        addErrorMsg("模板:"+excelTmpFileName+" 配置选项第一行第二列不匹配，检查是否模板正确");
                        return false;
                    }
                    dataAllCount++;
                    excuteLineNum = rowNum;

                    try {
                        Map<String,String> map = new HashMap<>();
                        ExcelDbData excelDbData;

                        StringBuilder errorMsg = null;
                        String value;
                        for(int i = 0;i<row.length;i++){
                            excelDbData = config.getHeadDataArray()[i];
                            if(excelDbData == null){
                                continue;
                            }

                            value = dataCheckService.getValue(excelDbData.getDataTypeEnum(),row[i].trim());
                            if(ExcelDbData.isError(value)){
                                if(errorMsg == null){
                                    errorMsg = new StringBuilder("第"+rowNum+"行出错，详细列数:");
                                }
                                errorMsg.append(i+1).append(",");
                                continue;
                            }
                            map.put(excelDbData.getColumName(),value);
                        }

                        if(errorMsg != null){
                            errorMsg.deleteCharAt(errorMsg.length()-1);
                            addErrorMsg(errorMsg.toString());
                            if(errorRows.size() > 20){
                                return false;
                            }
                        }

                        if(map.isEmpty()){
                            return true;
                        }
                        DbEntity entity = dbEntity.Instance(rowNum,map,config);
                        if(tmpMap.put(entity.getUniqueId(),entity) != null){
                            dataAllCount--;
                            repeateCount.getAndAdd(1);
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                        faileCount.getAndAdd(1);
                        addErrorMsg("第"+rowNum+"行出错："+ex.getMessage());
                        return true;
                    }

                    if(tmpMap.size() == config.getUpdateBatchCount()){
                        _importDBFromExcel(rowNum,tmpMap,dbService,config);
                        tmpMap=new HashMap<>();
                    }
                    return true;
                }else if(rowNum == config.getHeadDataLineNum()){//
                    if(config.getHeadDataArray() == null){
                        config.setHeadDataArray(getDbArray(config,row));
                    }
                    return true;
                }

                return true;
            }

            @Override
            public void overDocument(int rowNum) {
                if(!tmpMap.isEmpty()){
                    _importDBFromExcel(dataAllCount,tmpMap,dbService,config);
                }
            }
        };

        try {
            reader.read(fileName,listener,0);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        user_save_pools.shutdown();
        while (!user_save_pools.isTerminated()) {
            System.out.println("入库进程>>队列剩余："+user_save_pools.getQueue().size()+".. 正在入库:"+user_save_pools.getActiveCount());
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        importMsg = getMsg();
        System.out.println(importMsg);
        ExcelHelper.excelProcessMap.remove(fileName);

        PrintTool.outTime("1","over");
    }

    private void addErrorMsg(String msg){
        synchronized (errorRows){
            errorRows.add(msg);
        }
    }

    private void _importDBFromExcel(final int rowNum,final Map<String, DbEntity> tmpMap,final DbService dbService,final ExcelConfig config) {

        addTask(objects -> {
            int all = tmpMap.size();
            int update = 0;
            StringBuilder sql = new StringBuilder();
            if(config.isCheckDbBeforeUpdate()){
                sql.append("select ").append(config.getIdColumName()).append("  from  ").append(config.getTableName());
                sql.append("  where ").append(config.getIdColumName()).append("  in(").append(StringTool.getStringFromCollection(tmpMap.keySet())).append(")");
                List<String> uniqueIds = null;
                try {
                    uniqueIds = dbService.queryExistIds(sql.toString());
                }catch (Exception ex){
                    ex.printStackTrace();
                    addErrorMsg("第"+(all-rowNum)+"-"+rowNum+" 列导入数据库出错;"+ex.getMessage());
                    faileCount.getAndAdd(all);
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
            for(DbEntity data:tmpMap.values()){
                sqls.add(data.getUpdateSql());
            }
            try {
                dbService.excute(sqls);
                if(update != 0){
                    sucUpdateCount.getAndAdd(update);
                }
                if(update != all){
                    sucInsertCount.getAndAdd(all-update);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                addErrorMsg("第"+(all-rowNum)+"-"+rowNum+" 列导入数据库出错;"+ex.getMessage());
                faileCount.getAndAdd(all);
            }
        });
    }

    private ExcelDbData[] getDbArray(ExcelConfig config, String[] row) {
        ExcelDbData[] tmpArray = new ExcelDbData[row.length];
        for(int i = 0;i<row.length;i++){
            ExcelDbData data = config.getHeadDataMap().get(getHeadDesc(row[i]));
            if(data == null){
                continue;
            }
            tmpArray[i] = data;
        }

        return tmpArray;
    }

    public String getMsg() {
        return "终止行号:"+excuteLineNum+";完成总数据:"+dataAllCount+",更新:"+sucUpdateCount+",新增："+sucInsertCount+",失败："+faileCount+" 重复数据:"+repeateCount;
    }

    public boolean isOver() {
        return importMsg != null;
    }
}
