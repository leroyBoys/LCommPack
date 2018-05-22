package com.lgame.mysql.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.lgame.util.file.PropertiesTool;
import com.lgame.mysql.SqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/13.
 */
public class DruidDataSourceImpl implements SqlDataSource{
    private DruidDataSource dds = null;

    /**
     * 默认配置文件名字：druid_db.properties
     */
    protected DruidDataSourceImpl(){
        init(PropertiesTool.loadProperty("druid_db.properties"));
    }

    protected DruidDataSourceImpl(Properties properties){
        init(properties);
    }

    private void init(Properties properties){

        try {
            dds = (DruidDataSource) DruidDataSourceFactory
                    .createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dds.getConnection();
    }
}
