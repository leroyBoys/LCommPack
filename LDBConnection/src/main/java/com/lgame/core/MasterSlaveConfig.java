package com.lgame.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/23.
 */
public class MasterSlaveConfig {
    private String dbType;
    private Map<String,String> master = new HashMap<>();

    private Map<String,Map<String,String>> slaves = new HashMap<>();

    public MasterSlaveConfig(String dbType) {
        this.dbType = dbType;
    }

    public void addMaster(String key, String value) {
        master.put(key,value);
    }

    public void addSlave(String uniqueKey, String key, String value) {
        Map<String,String> map = slaves.get(uniqueKey);
        if(map == null){
            map = new HashMap<>();
            slaves.put(uniqueKey,map);
        }
        map.put(key,value);
    }

    public String getDbType() {
        return dbType;
    }

    public Map<String, String> getMaster() {
        return master;
    }

    public Map<String, Map<String, String>> getSlaves() {
        return slaves;
    }
}
