package com.lgame.core;

import com.lgame.entity.NodeManger;
import com.lgame.mysql.compiler.ScanEntitysTool;
import com.lgame.mysql.impl.JDBCInitCache;
import com.lgame.mysql.impl.JDBCManager;
import com.lgame.redis.impl.RedisConnectionManager;

import java.util.*;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/2.
 */
public class LQStart {
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
    public static void initConnectionManager(Properties properties) throws Exception {
        Map<String,MasterSlaveGlobalConfig> globalConfigMap = new HashMap<>();
        for(String dbType:dbTypeArray){
            globalConfigMap.put(dbType,new MasterSlaveGlobalConfig(dbType));
        }
        JDBCInitCache jdbcInitCache = new JDBCInitCache();

        Map<String,MasterSlaveConfig> node_configMap = new HashMap<>();
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

            String[] array = k.split("\\.");
            if(array.length < 3){
                System.err.println("数据格式配置错误:"+k);
                continue;
            }

            String dbType = getDbType(array[1]);

            if(array.length == 3){
                globalConfigMap.get(dbType).addMaster(propertiesKey(k),v);
                globalConfigMap.get(dbType).addSlave(propertiesKey(k),v);
                continue;
            }

            if(array.length == 4){
                if(array[2].equals("master")){
                    globalConfigMap.get(dbType).addMaster(propertiesKey(k),v);
                }else {
                    globalConfigMap.get(dbType).addSlave(propertiesKey(k),v);
                }
                continue;
            }

            String node = array[2];
            MasterSlaveConfig masterSlaveConfig= node_configMap.get(node);
            if(masterSlaveConfig == null){
                masterSlaveConfig = new MasterSlaveConfig(dbType);
                node_configMap.put(node,masterSlaveConfig);
            }

            if(array[3].equals("master")){
                masterSlaveConfig.addMaster(propertiesKey(k),v);
            }else {
                masterSlaveConfig.addSlave(array[3],propertiesKey(k),v);
            }
        }

        if(node_configMap.isEmpty()){
            System.err.println("error:配置数据源格式错误，为初始化数据源");
            return;
        }

        Set<String> dbTypes = new HashSet<>();
        for(Map.Entry<String,MasterSlaveConfig> entry:node_configMap.entrySet()){
            MasterSlaveConfig masterSlaveConfig = entry.getValue();
            MasterSlaveGlobalConfig config = globalConfigMap.get(masterSlaveConfig.getDbType());
            inintDataSourceManger(masterSlaveConfig.getDbType(),jdbcInitCache,entry.getKey(),config.getMaster(masterSlaveConfig.getMaster()),config.getSlave(masterSlaveConfig.getSlaves()));
            dbTypes.add(masterSlaveConfig.getDbType());
        }

        for(String dbType:dbTypeArray){
           if(dbTypes.contains(dbType)){
               continue;
           }
            MasterSlaveGlobalConfig config = globalConfigMap.get(dbType);
            inintDataSourceManger(dbType,jdbcInitCache,null,config.getMaster(null),null);
        }
    }

    private static void inintDataSourceManger(String dbType,JDBCInitCache jdbcInitCache,String nodeName,Properties master,Properties... slaves) throws Exception {

       NodeManger nodeManger = null;
       if(dbType.equals("redis")){
           if(redisConnectionManager == null){
               redisConnectionManager =  new RedisConnectionManager();
           }
           nodeManger = redisConnectionManager;

       }else if(dbType.equals("db")){
           if(jdbcManager == null){
               jdbcManager =  new JDBCManager();
           }
           nodeManger = jdbcManager;
       }else {
           System.out.println("warn: not find match dbType:"+dbType);
           return;
       }

        nodeManger.initProperties(jdbcInitCache,nodeName,master,slaves);
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

    public static RedisConnectionManager getRedisConnectionManager(){
        return redisConnectionManager;
    }

    public static JDBCManager getJdbcManager() {
        return jdbcManager;
    }
}
