package com.lgame.mysql.impl;

import com.lgame.util.file.PropertiesTool;
import com.lgame.mysql.SqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/13.
 */
public class HikariDataSourceImpl implements SqlDataSource{
    private HikariDataSource dds = null;

    /**
     * 默认配置文件名字：hikari_db.properties
     */
    protected HikariDataSourceImpl(){
        init(PropertiesTool.loadProperty("hikari_db.properties"));
    }

    protected HikariDataSourceImpl(Properties properties){
        init(properties);
    }

    private void init(Properties properties){

        try {
            dds = new HikariDataSource(new HikariConfig(properties));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dds.getConnection();
    }
}
