package com.lgame.mysql.impl;

import java.util.Properties;
import java.util.Random;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/23.
 */
public class JDBCManager {
    private final Random random = new Random();
    private DataSourceImpl master;
    private DataSourceImpl[] slaves;

    public JDBCManager(Properties masterConfig, Properties... slavesConfig) {
        JDBCInitCache jdbcInitCache = new JDBCInitCache();

        master = initRedisConnection(masterConfig,jdbcInitCache);
        if (slavesConfig == null || slavesConfig.length == 0) {
            return;
        }

        slaves = new DataSourceImpl[slavesConfig.length];
        for (int i = 0; i < slavesConfig.length; i++) {
            slaves[i] = initRedisConnection(slavesConfig[i],jdbcInitCache);
        }
    }

    private DataSourceImpl initRedisConnection(Properties config,JDBCInitCache jdbcInitCache) {
        return new DataSourceImpl(config,jdbcInitCache);
    }

    public DataSourceImpl getMaster() {
        return master;
    }

    public DataSourceImpl getRandomSlave() {
        if (slaves == null) {
            return master;
        }

        if (slaves.length == 1) {
            return slaves[0];
        }

        return slaves[random.nextInt(slaves.length)];
    }

}