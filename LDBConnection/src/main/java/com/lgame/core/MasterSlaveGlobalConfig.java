package com.lgame.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/23.
 */
public class MasterSlaveGlobalConfig {
    private String dbType;
    private Map<String,String> master = new HashMap<>();
    private Map<String,String> slave = new HashMap<>();

    public MasterSlaveGlobalConfig(String dbType) {
        this.dbType =dbType;
    }

    public void addMaster(String key, String value) {
        master.put(key,value);
    }

    public void addSlave(String key, String value) {
        slave.put(key,value);
    }

    public Properties getMaster(Map<String,String> masterMap) {
        return createProperties(masterMap,master);
    }

    public Properties[] getSlave(Map<String,Map<String,String>> slaves) {
        if(slaves == null || slaves.isEmpty()){
            if(master.isEmpty()){
                return null;
            }
            return new Properties[]{createProperties(null,slave)};
        }

        Properties[] arary = new Properties[slaves.size()];
        int i = 0;
        for(Map<String,String> map:slaves.values()){
            arary[i++] = createProperties(map,slave);
        }
        return arary;
    }

    private static Properties createProperties(Map<String,String> map,Map<String,String> globalMap){
        if((map == null || map.isEmpty()) && globalMap.isEmpty()){
            return null;
        }

        Properties properties = new Properties();

        if(map != null && !map.isEmpty()){
            for(Map.Entry<String,String> entry:map.entrySet()){
                properties.setProperty(entry.getKey(),entry.getValue());
            }
        }

        if(!globalMap.isEmpty()){
            for(Map.Entry<String,String> entry:globalMap.entrySet()){
                if(properties.containsKey(entry.getKey())){
                    continue;
                }
                properties.setProperty(entry.getKey(),entry.getValue());
            }
        }
        return properties;
    }
}
