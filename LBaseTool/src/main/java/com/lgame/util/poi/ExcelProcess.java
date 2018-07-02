package com.lgame.util.poi;

import com.lgame.util.PrintTool;
import com.lgame.util.comm.StringTool;
import com.lgame.util.poi.interfac.*;
import com.lgame.util.poi.module.DefaultRowListener;
import com.lgame.util.poi.module.ExcelConfig;
import com.lgame.util.poi.module.ExcelDbData;
import com.lgame.util.poi.module.PersonalityConfig;
import com.lgame.util.thread.BaseThreadPools;
import com.lgame.util.thread.TaskIndieThread;

import java.util.*;
import java.util.concurrent.BlockingQueue;
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
    private List<String> sqlErrorRows = new LinkedList<>();//错误信息
    private String importMsg;//结束提示信息
    private int process;//进度百分比10000为100%
    private Set<String> uniqueKeys = new HashSet<>();
    private Set<String> errorUniqueKeys = new HashSet<>();
    private Map<String,DbEntity> repeateContainsMap = new HashMap<>();

    protected ExcelProcess(){}

    private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
    public final BaseThreadPools user_save_pools = new BaseThreadPools(4, 4,
            0L, TimeUnit.MILLISECONDS,workQueue , (r, executor) -> {
                try {
                    workQueue.put(r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });//线程池

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
    public void excute(DbService dbService, ExcelConfig config, DbEntity dbEntity, String fileName, String excelTmpFileName, PoiReader reader,PersonalityConfig personalityConfig){

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
                        Map<String,String> map = new HashMap<>(row.length);
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
                                    errorMsg = new StringBuilder(rowNum+":");
                                }
                                errorMsg.append(i+1).append(",");
                                continue;
                            }
                            map.put(excelDbData.getColumName(),excelDbData.getValue(value));
                        }

                        if(errorMsg != null){
                            errorMsg.deleteCharAt(errorMsg.length()-1);
                            addErrorMsg(errorMsg.toString());
                            faileCount.getAndAdd(1);
                            if(errorRows.size() > 2000){
                                return false;
                            }
                        }

                        if(map.isEmpty()){
                            return true;
                        }

                        DbEntity entity = dbEntity.Instance(rowNum,map,config,personalityConfig);
                        if(uniqueKeys.contains(entity.getUniqueId())){
                            dataAllCount--;
                            repeateContainsMap.put(entity.getUniqueId(),entity);
                            repeateCount.getAndAdd(1);
                        }else {
                            uniqueKeys.add(entity.getUniqueId());
                            tmpMap.put(entity.getUniqueId(),entity);
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
                initProcess();
            }
        };

        try {
            reader.read(fileName,listener,0);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        user_save_pools.shutdown();
        short i = 0;
        while (!user_save_pools.isTerminated()) {
            if(i++ == 127){
                System.out.println("入库进程>>队列剩余："+user_save_pools.getQueue().size()+".. 正在入库:"+user_save_pools.getActiveCount());
                i=0;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        doRepeateData(dbService);

        importMsg = getMsg();
        System.out.println(importMsg);
        ExcelHelper.excelProcessMap.remove(fileName);

        PrintTool.outTime(ExcelProcess.class.getName(),"over");
    }

    private void doRepeateData(DbService dbService) {
        if(repeateContainsMap.isEmpty()){
            return;
        }

        List<String> updateSqls = new LinkedList<>();
        for(Map.Entry<String,DbEntity> entityEntry:repeateContainsMap.entrySet()){
            if(errorUniqueKeys.contains(entityEntry.getKey())){
                continue;
            }

            entityEntry.getValue().setNew(false);
            updateSqls.add(entityEntry.getValue().getUpdateSql());
        }

        if(!dbService.excute(updateSqls)){
            updateExceuteWithOutError(getSqlMapLine(tmpMap,updateSqls.size()),updateSqls,dbService);
        }
    }

    private void initProcess() {
        int i = dataAllCount-faileCount.get();
        if(i != 0){
            process = ((sucUpdateCount.get()+sucInsertCount.get())*10000)/i;
        }
    }

    public double getProcess(){
        if(process == 0){
            return 2.0;
        }
        this.initProcess();
        return process;
    }
    private void addErrorMsg(String msg){
        synchronized (errorRows){
            errorRows.add(msg);
        }
    }

    private void addSqlErrorMsg(String msg){
        synchronized (sqlErrorRows){
            sqlErrorRows.add(msg);
        }
    }

    private void addErrorUniqueKeys(String uniqueId){
        synchronized (errorUniqueKeys){
            errorUniqueKeys.add(uniqueId);
        }
    }

    private void _importDBFromExcel(final int rowNum,final Map<String, DbEntity> _tmpMap,final DbService dbService,final ExcelConfig config) {

        addTask(objects -> {
            final int all = _tmpMap.size();
            int update = 0;
            StringBuilder sql = new StringBuilder();
            if(config.isCheckDbBeforeUpdate()){
                sql.append("select ").append(config.getIdColumName()).append("  from  ").append(config.getTableName());
                sql.append("  where ").append(config.getIdColumName()).append("  in(").append(StringTool.getStringFromCollection(_tmpMap.keySet())).append(")");
                List<String> uniqueIds = null;
                try {
                    uniqueIds = dbService.queryExistIds(sql.toString());
                }catch (Exception ex){
                    ex.printStackTrace();
                    addErrorMsg("第"+(rowNum-all)+"-"+rowNum+" 查询数据库出错;"+ex.getMessage());
                    addSqlErrorMsg(sql.toString());
                    faileCount.getAndAdd(all);
                    return;
                }
                if(uniqueIds != null && !uniqueIds.isEmpty()){
                    for(String uniqueId:uniqueIds){
                        _tmpMap.get(uniqueId).setNew(false);
                        update++;
                    }
                }
            }

            List<String> updateSqls = new ArrayList<>(update);
            List<Map<String,String>> insertMapList = new ArrayList<>(all-update);
            for(DbEntity data:_tmpMap.values()){
                if(data.isNew()){
                    insertMapList.add(data.getDataEntity());
                    continue;
                }
                updateSqls.add(data.getUpdateSql());
            }
            try {
                if(!insertMapList.isEmpty()){
                    if(!dbService.insertBatchs(config.getTableName(),insertMapList,config.getColumArray(),insertMapList.size())){
                        Map<String,DbEntity> map = new HashMap<>();
                        List<String> insertSqls = new ArrayList<>();
                        for(DbEntity data:_tmpMap.values()){
                            if(data.isNew()){
                                map.put(data.getUpdateSql(), data);
                                insertSqls.add(data.getUpdateSql());
                            }
                        }
                        updateExceuteWithOutError(map,insertSqls,dbService,sucInsertCount);
                    }else {
                        sucInsertCount.getAndAdd(insertMapList.size());
                    }
                }

                if(!updateSqls.isEmpty()){
                    if(!dbService.excute(updateSqls)){
                        updateExceuteWithOutError(getSqlMapLine(_tmpMap,update),updateSqls,dbService,sucUpdateCount);
                    }else {
                        sucUpdateCount.getAndAdd(update);
                    }
                }

            }catch (Exception ex){
                ex.printStackTrace();
                addErrorMsg("第"+(rowNum-all)+"-"+rowNum+" 列导入数据库出错;"+ex.getMessage());
                faileCount.getAndAdd(all);
            }
        });
    }

    private Map<String,DbEntity> getSqlMapLine(Map<String, DbEntity> tmpMap,int initSize){
        Map<String,DbEntity> map = new HashMap<>(initSize);
        for(DbEntity data:tmpMap.values()){
            if(!data.isNew()){
                map.put(data.getUpdateSql(), data);
            }
        }
        return map;
    }

    /**
     *  更新快速过滤掉 错误数据
     * @param tmpMap sql-rowNum
     * @param dbService
     */
    private void updateExceuteWithOutError(Map<String,DbEntity> tmpMap,List<String> updateSqls, DbService dbService,AtomicInteger updateCount) {
        addTask(objects -> {
            final int size = updateSqls.size()>>1;
            if(size < 10){
                for(String sql:updateSqls){
                    if(!dbService.excute(sql)){
                        faileCount.getAndAdd(1);
                        DbEntity dbEntity = tmpMap.get(sql);
                        addErrorMsg("第"+dbEntity.getRow()+"行出错");
                        addSqlErrorMsg(sql);
                        addErrorUniqueKeys(dbEntity.getUniqueId());
                    }else {
                        updateCount.getAndAdd(1);
                    }
                }
                return;
            }

            List<String> sqls = updateSqls.subList(0,size);
            if(!dbService.excute(sqls)){
                updateExceuteWithOutError(tmpMap,sqls,dbService,updateCount);
            }else {
                updateCount.getAndAdd(sqls.size());
            }

            sqls = updateSqls.subList(size,updateSqls.size());
            if(!dbService.excute(sqls)){
                updateExceuteWithOutError(tmpMap,sqls,dbService,updateCount);
            }else {
                updateCount.getAndAdd(sqls.size());
            }

        });

    }

    private void updateExceuteWithOutError(Map<String,DbEntity> tmpMap,List<String> updateSqls, DbService dbService) {
        if(tmpMap == null || tmpMap.isEmpty()){
            return;
        }

        final int size = updateSqls.size()>>1;
        if(size < 10){
            for(String sql:updateSqls){
                if(!dbService.excute(sql)){
                    DbEntity dbEntity = tmpMap.get(sql);
                    addErrorMsg("第"+dbEntity.getRow()+"行更新出错");
                    addSqlErrorMsg(sql);
                }
            }
            return;
        }

        List<String> sqls = updateSqls.subList(0,size);
        if(!dbService.excute(sqls)){
            updateExceuteWithOutError(tmpMap,sqls,dbService);
        }

        sqls = updateSqls.subList(size,updateSqls.size());
        if(!dbService.excute(sqls)){
            updateExceuteWithOutError(tmpMap,sqls,dbService);
        }
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
        StringBuffer sb = new StringBuffer("终止行号:");
        sb.append(excuteLineNum).append(";完成总数据:").append(dataAllCount).append(",更新:").append(sucUpdateCount).append(",新增：").append(sucInsertCount);
        sb.append(",失败：").append(faileCount).append("("+errorRows.size()+")").append(" 重复数据:").append(repeateCount);
        if(errorRows.size() > 10){
            sb.append(Arrays.toString(errorRows.subList(0,10).toArray())).append(" ....省略数据条目:").append(errorRows.size()-10);
        }else {
            sb.append(Arrays.toString(errorRows.toArray()));
        }

        if(sqlErrorRows.size() > 10){
            sb.append("errorSql:");
            sb.append(Arrays.toString(sqlErrorRows.subList(0,10).toArray())).append(" ....省略数据条目:").append(sqlErrorRows.size()-10);
        }else {
            sb.append(Arrays.toString(sqlErrorRows.toArray()));
        }

        return sb.toString();
    }

    public boolean isOver() {
        return importMsg != null;
    }
}
