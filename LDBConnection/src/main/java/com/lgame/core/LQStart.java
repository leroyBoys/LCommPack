package com.lgame.core;

import com.lgame.entity.*;
import com.lgame.mysql.compiler.ScanEntitysTool;
import com.lgame.mysql.impl.JDBCManager;
import com.lgame.redis.impl.RedisConnectionManager;
import com.lgame.util.LqUtil;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/2.
 */
public class LQStart {
    private static WeakReference<StartInitCache> tmpCache = new WeakReference<>(new StartInitCache());

    public static ScanEntitysTool instance;
    private static RedisConnectionManager redisConnectionManager;
    private static JDBCManager jdbcManager;

    public static StartInitCache getMethodCache(){
        StartInitCache startInitCache = tmpCache.get();
        if(startInitCache != null){
            return startInitCache;
        }
        startInitCache = new StartInitCache();
        tmpCache = new WeakReference<>(startInitCache);
        return startInitCache;
    }

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

    public static void init(Properties properties) throws Exception {
        Map<String,MasterSlaveGlobalConfig> globalConfigMap = new HashMap<>();
        for(DBType dbType:DBType.values()){
            globalConfigMap.put(dbType.name(),new MasterSlaveGlobalConfig(dbType.name()));
        }
        StartInitCache startInitCache = getMethodCache();

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

        startInitCache.setGlobalConfigMap(globalConfigMap);
        startInitCache.setNode_configMap(node_configMap);

        Set<String> dbTypes = new HashSet<>();
        for(Map.Entry<String,MasterSlaveConfig> entry:node_configMap.entrySet()){
            MasterSlaveConfig masterSlaveConfig = entry.getValue();
            MasterSlaveGlobalConfig config = globalConfigMap.get(masterSlaveConfig.getDbType());
            inintDataSourceManger(masterSlaveConfig.getDbType(),entry.getKey(),config.getMaster(masterSlaveConfig.getMaster()),config.getSlave(masterSlaveConfig.getSlaves()));
            dbTypes.add(masterSlaveConfig.getDbType());
        }

        for(DBType dbTypeEnum:DBType.values()){
            String dbType = dbTypeEnum.name();
           if(dbTypes.contains(dbType)){
               continue;
           }
            MasterSlaveGlobalConfig config = globalConfigMap.get(dbType);
            inintDataSourceManger(dbType,null,config.getMaster(null),null);
        }
    }

    private static Properties getNewProperties(DBType dbType,LQNewNode newNode,LQConnConfig connConfig,boolean isMaster){
        if(dbType == DBType.db){
        }else if(dbType == DBType.redis){
        }else {
            System.out.println("not config manager for dbType:"+dbType);
            return null;
        }

        StartInitCache startInitCache = getMethodCache();
        MasterSlaveGlobalConfig masterSlaveGlobalConfig = startInitCache.getGlobalConfigMap().get(dbType);

        Map<String,String> globlmap = new HashMap<>(50);
        if(masterSlaveGlobalConfig != null){
            Map<String,String> map = null;
            if(isMaster){
                globlmap = masterSlaveGlobalConfig.getMaster();
            }else {
                globlmap = masterSlaveGlobalConfig.getSlave();
            }
            globlmap.putAll(map);
        }

        if(newNode != null && newNode.getReadNodeName() != null){
            MasterSlaveConfig masterSlaveConfig = startInitCache.getNode_configMap().get(newNode.getReadNodeName());
            if(masterSlaveConfig != null){
                Map<String,String> map = null;
                if(isMaster){
                    map = masterSlaveConfig.getMaster();
                }else {
                    map = masterSlaveConfig.getSlaves().isEmpty()?masterSlaveConfig.getMaster():masterSlaveConfig.getSlaves().values().iterator().next();
                }
                globlmap.putAll(map);
            }
        }
        if(dbType == DBType.db){
            LQConnConfig.LQDBConnConfig lqRedisConnConfig = (LQConnConfig.LQDBConnConfig) connConfig;
            globlmap.put("url",lqRedisConnConfig.getUrl());
            globlmap.put("jdbcUrl",lqRedisConnConfig.getUrl());
            globlmap.put("username",lqRedisConnConfig.getUserName());
            globlmap.put("user",lqRedisConnConfig.getUserName());
            globlmap.put("password",lqRedisConnConfig.getPassword());
        }else if(dbType == DBType.redis){
            LQConnConfig.LQRedisConnConfig lqRedisConnConfig = (LQConnConfig.LQRedisConnConfig) connConfig;
            globlmap.put("url",lqRedisConnConfig.getUrl());
        }
        return LqUtil.createProperties(globlmap);
    }

    public static void addNewDataSource(DBType dbType,LQConnConfig master, LQConnConfig... slaves) throws Exception {
        addNewDataSource(dbType,null,master,slaves);
    }

    public static void addNewDataSource(DBType dbType, LQNewNode newNode, LQConnConfig master, LQConnConfig... slaves) throws Exception {
        Properties masterProperties = getNewProperties(dbType,newNode,master,true);

        if(slaves != null && slaves.length>0){
            Properties[] properties = new Properties[slaves.length];
            for(int i = 0;i<slaves.length;i++){
                properties[i] = getNewProperties(dbType,newNode,slaves[i],false);
            }
            inintDataSourceManger(dbType.name(),newNode==null?null:newNode.getNewNodeName(),masterProperties,properties);
            return;
        }

        inintDataSourceManger(dbType.name(),newNode==null?null:newNode.getNewNodeName(),masterProperties);
    }

    private static void inintDataSourceManger(String dbType,String nodeName,Properties master,Properties... slaves) throws Exception {
        if(master == null){
            return;
        }

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

        nodeManger.initProperties(nodeName,master,slaves);
    }

    private static String getDbType(String str){
        DBType dbType = DBType.valueOf(str);
        if(dbType == null){
            dbType = DBType.db;
        }
        return dbType.name();
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
