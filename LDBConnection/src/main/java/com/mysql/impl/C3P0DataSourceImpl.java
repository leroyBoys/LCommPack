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
    private ComboPooledDataSource dds = null;

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
            dds = new ComboPooledDataSource();

            dds.setDriverClass(properties.getProperty("driverClassName"));
            dds.setJdbcUrl(properties.getProperty("url"));      // test为mysql中的数据库
            dds.setUser(properties.getProperty("username"));
            dds.setPassword(properties.getProperty("password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dds.getConnection();
    }
}
