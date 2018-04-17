package com.lgame.util.excel;

import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */
public interface ExcelService<T> {
    public List<String> query(String sql);
    public int excute(List<String> sqls);
}
