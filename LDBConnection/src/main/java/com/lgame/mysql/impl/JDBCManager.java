package com.lgame.mysql.impl;

import com.lgame.entity.NodeManger;

import java.util.Properties;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/24.
 */
public class JDBCManager extends NodeManger<LQDataSource> {

    @Override
    protected LQDataSource initRedisConnection(Properties properties) {
        return new LQDataSource(properties);
    }

    @Override
    public LQDataSource[] createArray(int size) {
        return new LQDataSource[size];
    }
}
