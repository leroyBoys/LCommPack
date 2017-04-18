package com.mysql.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.lgame.util.file.PropertiesTool;
import com.mysql.SqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/13.
 */
public class DruidDataSourceImpl implements SqlDataSource{
    private static DruidDataSource dds = null;

    /**
     * 默认配置文件名字：druid_db.properties
     */
    protected DruidDataSourceImpl(){
        init(null);
    }

    protected DruidDataSourceImpl(String propertieFileName){
        init(propertieFileName);
    }

    private void init(String propertieFileName){
        if(propertieFileName == null || propertieFileName.trim().isEmpty()){
            propertieFileName = "druid_db.properties";
        }

        Properties properties = PropertiesTool.loadProperty(propertieFileName);
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
