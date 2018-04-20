package com.lgame.util.poi.interfac;

import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */
public interface DbService {
    public List<String> queryExistIds(String sql);
    public int excute(List<String> sqls);
}
