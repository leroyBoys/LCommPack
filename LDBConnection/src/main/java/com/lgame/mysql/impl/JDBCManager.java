package com.lgame.mysql.impl;

import com.lgame.entity.NodeManger;

import java.util.Properties;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/24.
 */
public class JDBCManager extends NodeManger<DataSourceImpl> {

    @Override
    protected DataSourceImpl initRedisConnection(JDBCInitCache jdbcInitCache, Properties properties) {
        return new DataSourceImpl(properties,jdbcInitCache);
    }

    @Override
    public DataSourceImpl[] createArray(int size) {
        return new DataSourceImpl[size];
    }
}
