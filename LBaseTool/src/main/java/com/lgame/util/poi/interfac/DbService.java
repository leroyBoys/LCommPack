package com.lgame.util.poi.interfac;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/16.
 */
public interface DbService {
    public List<String> queryExistIds(String sql);
    public int excute(List<String> sqls);
    public int insertBatchs(String tableName, List<Map<String,String>> datas, String[] columNames, String[] columValues, int commitLimitCount);
}
