package com.mysql.impl;

import java.sql.ResultSet;

/**
 * Created by Administrator on 2017/4/20.
 */
public interface DbFactory<T> {
    public T create(ResultSet rs);
}
