package com.redis.entity;

import com.lgame.util.comm.TimeCacheManager;
import com.lgame.util.json.FastJsonTool;
import com.lgame.util.json.JsonUtil;
import com.mysql.compiler.FieldGetProxy;
import com.mysql.entity.DBTable;
import com.redis.impl.RedisConnectionImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/17.
 */
public class MapRedisSerializer extends RedisSerializer{
    /**  colum--getMethod */
    private Map<String,FieldGetProxy.FieldGet> alias;
    private String[] aliasArray;
    public MapRedisSerializer(Map<String,FieldGetProxy.FieldGet> alias){
        this.alias = alias;
        aliasArray = new String[alias.size()];
        alias.keySet().toArray(aliasArray);
    }

    @Override
    public void serializer(RedisConnectionImpl redisConnection, DBTable table, Object entity) {

        String keys = table.redisKey(table.getRedisKeyGetInace().formatToDbData(entity));
        try {
            Map<String, String> map = new HashMap<>(aliasArray.length);
            Object object;
            for(String columName:aliasArray){
                object = alias.get(columName).formatToDbData(entity);
                if(object == null){
                    continue;
                }
                map.put(columName,object.toString());
            }
            redisConnection.hmset(keys,map);

            if(table.getRedisCache().expire() > 0){
                if(table.getRedisCache().expireAt() > 0){
                    long endTime = TimeCacheManager.getInstance().getCurTime()+table.getRedisCache().expire()*1000;
                    endTime = Math.min(endTime,table.getRedisCache().expireAt());
                    redisConnection.expireAt(keys,endTime);
                    return;
                }
                redisConnection.expire(keys,table.getRedisCache().expire());
            }else  if(table.getRedisCache().expireAt() > 0){
                redisConnection.expireAt(keys,table.getRedisCache().expireAt());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Object mergeFrom(RedisConnectionImpl redisConnection,DBTable table,Class instance,Object uniqueId) {
        try {
            String keys = table.redisKey(uniqueId);
            Map<String, String> map = redisConnection.hgetAll(keys);
            if(map == null){
                return null;
            }

            Object entity = instance.newInstance();
            FieldGetProxy.FieldGet fieldGet;
            for(String columName:aliasArray){
                fieldGet = alias.get(columName);
                if(fieldGet == null){
                    continue;
                }
                table.getColumInit(columName).setFromRedis(entity, fieldGet.formatFromDb(map.get(columName)));
            }

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
