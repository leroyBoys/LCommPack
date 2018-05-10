package com.mysql.impl;

import com.lgame.util.file.PropertiesTool;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.cfg.C3P0Config;
import com.mchange.v2.c3p0.jboss.C3P0PooledDataSource;
import com.mysql.SqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/13.
 */
public class C3P0DataSourceImpl implements SqlDataSource{
    private DataSource  dds = null;

    /**
     * 默认配置文件名字：hikari_db.properties
     */
    protected C3P0DataSourceImpl(){
        init(PropertiesTool.loadProperty("c3p0.properties"));
    }

    protected C3P0DataSourceImpl(Properties properties){
        init(properties);
    }

    private void init(Properties properties){

        try {
            //设置连接数据库的配置信息
            DataSource ds_unpooled = DataSources
                    .unpooledDataSource(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));

            dds = DataSources.pooledDataSource(ds_unpooled, properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dds.getConnection();
    }
}
