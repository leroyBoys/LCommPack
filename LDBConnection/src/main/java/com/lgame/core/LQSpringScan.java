package com.lgame.core;

import com.lgame.mysql.compiler.ScanEntitysTool;
import com.lgame.mysql.impl.JDBCManager;
import com.lgame.redis.impl.RedisConnectionManager;

import java.util.*;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/2.
 */
public class LQSpringScan {
    private final static String[] dbTypeArray = new String[]{"redis","db"};
    public static ScanEntitysTool instance;
    private static RedisConnectionManager redisConnectionManager;
    private static JDBCManager jdbcManager;

    public static void scan(String... packs) throws Exception {
        if(instance != null){
            return;
        }
        synchronized (ScanEntitysTool.class){
            if(instance != null){
                return;
            }
            instance = new ScanEntitysTool(packs);
        }
    }
///*redis.master.01.name  redis.01.master.mame
    public static void initConnectionManager(Properties properties){
        Map<String,MasterSlaveGlobalConfig> globalConfigMap = new HashMap<>();
        for(String dbType:dbTypeArray){
            globalConfigMap.put(dbType,new MasterSlaveGlobalConfig(dbType));
        }


        Map<String,MasterSlaveConfig> configMap = new HashMap<>();
        for (Enumeration<?> e = properties.keys(); e.hasMoreElements() ;) {
            Object ko = e.nextElement();
            if (!(ko instanceof String)) {
                continue;
            }

            String k = (String) ko;
            String v = properties.get(k).toString();

            if(k.indexOf("datasource")<0){
               continue;
            }

            if(k.indexOf("master") >= 0){
                String masterKey = masterKey(k);
                if(masterKey.endsWith("master")){
                    globalConfigMap.get(getDbType(masterKey)).addMaster(propertiesKey(k),v);
                    continue;
                }

                String msConfigKey = masterKey.replaceFirst("master","-");
                MasterSlaveConfig masterSlaveConfig= configMap.get(msConfigKey);
                if(masterSlaveConfig == null){
                    masterSlaveConfig = new MasterSlaveConfig(getDbType(msConfigKey));
                    configMap.put(msConfigKey,masterSlaveConfig);
                }
                masterSlaveConfig.addMaster(propertiesKey(k),v);
            }else if(k.indexOf("slave") >= 0){

                String key = k.replaceFirst("slave","-");
                int splitLength = key.split("\\.").length;
                if(splitLength <4){
                    System.err.println("数据格式配置错误:"+k);
                    continue;
                }else if(splitLength==4){//slave 公共
                    globalConfigMap.get(getDbType(key)).addSlave(propertiesKey(k),v);
                    continue;
                }

                String uniqueKey = masterKey(k);
                String msConfigKey = key.substring(0,key.indexOf(".",key.indexOf("-")+2));
                MasterSlaveConfig masterSlaveConfig= configMap.get(msConfigKey);
                if(masterSlaveConfig == null){
                    masterSlaveConfig = new MasterSlaveConfig(getDbType(msConfigKey));
                    configMap.put(msConfigKey,masterSlaveConfig);
                }

                masterSlaveConfig.addSlave(uniqueKey,propertiesKey(k),v);
            }else {
                String dbType = dbTypeArray[dbTypeArray.length-1];
                for(String dt:dbTypeArray){
                    if(k.contains(dt)){
                        dbType = dt;
                        break;
                    }
                }

                MasterSlaveGlobalConfig globalConfig= globalConfigMap.get(dbType);
                if(globalConfig == null){
                    globalConfig = new MasterSlaveGlobalConfig(dbType);
                    globalConfigMap.put(dbType,globalConfig);
                }
                globalConfig.addMaster(propertiesKey(k),v);
                globalConfig.addSlave(propertiesKey(k),v);
            }
        }

        if(configMap.isEmpty()){
            System.err.println("error:配置数据源格式错误，为初始化数据源");
            return;
        }

        Set<String> dbTypes = new HashSet<>();
        for(MasterSlaveConfig masterSlaveConfig:configMap.values()){
            MasterSlaveGlobalConfig config = globalConfigMap.get(masterSlaveConfig.getDbType());
            inintDataSourceManger(masterSlaveConfig.getDbType(),config.getMaster(masterSlaveConfig.getMaster()),config.getSlave(masterSlaveConfig.getSlaves()));
            dbTypes.add(masterSlaveConfig.getDbType());
        }

        for(String dbType:dbTypeArray){
           if(dbTypes.contains(dbType)){
               continue;
           }
            MasterSlaveGlobalConfig config = globalConfigMap.get(dbType);
            inintDataSourceManger(dbType,config.getMaster(null),null);
        }
    }

    private static void inintDataSourceManger(String dbType,Properties master,Properties... slaves){
       if(dbType.equals("redis")){
           if(slaves == null){
               initRedisConnectionManager(master);
           }else {
               initRedisConnectionManager(master,slaves);
           }
       }else if(dbType.equals("db")){
           if(slaves == null){
               initJdbcConnectionManager(master);
           }else {
               initJdbcConnectionManager(master,slaves);
           }
       }else {
           System.out.println("warn: not find match dbType:"+dbType);
       }
    }

    private static String getDbType(String str){
        for(String dbType:dbTypeArray){
            if(str.contains(dbType)){
                return dbType;
            }
        }
        return dbTypeArray[dbTypeArray.length-1];
    }

    private static String masterKey(String configKey){
        int idex = configKey.lastIndexOf(".");
        if(idex == -1){
            return configKey;
        }
        return configKey.substring(0,idex);
    }

    private static String propertiesKey(String configKey){
        return configKey.substring(configKey.lastIndexOf(".")+1);
    }

    private static void initRedisConnectionManager(Properties masterConfig, Properties... slavesConfig){
        if(redisConnectionManager != null){
            return;
        }

        synchronized (ScanEntitysTool.class){
            if(redisConnectionManager != null){
                return;
            }
            redisConnectionManager = new RedisConnectionManager(masterConfig, slavesConfig);
        }
        return;
    }

    private static void initJdbcConnectionManager(Properties masterConfig, Properties... slavesConfig){
        if(jdbcManager != null){
            return;
        }

        synchronized (ScanEntitysTool.class){
            if(jdbcManager != null){
                return;
            }
            jdbcManager = new JDBCManager(masterConfig, slavesConfig);
        }
        return;
    }

    public static RedisConnectionManager getRedisConnectionManager(){
        return redisConnectionManager;
    }

    public static JDBCManager getJdbcManager() {
        return jdbcManager;
    }

    public static void instance(String com) throws Exception {
        scan(com);
    }
}
