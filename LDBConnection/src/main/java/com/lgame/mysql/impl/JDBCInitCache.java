package com.lgame.mysql.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/24.
 */
public class JDBCInitCache {
    private Map<String,MethodCache> methodCacheMap = new HashMap<>();

    public MethodCache getMethodCache(String classSourceName) throws ClassNotFoundException {
        MethodCache methodCache = methodCacheMap.get(classSourceName);
        if(methodCache != null){
            return methodCache;
        }

        methodCache = new MethodCache();

        Class cls = Class.forName(classSourceName);
        Method[] methods = cls.getMethods();
        Map<String,Method> fieldMap = new HashMap<>(methods.length);
        for(Method method:methods){
            if(!method.getName().startsWith("set")){
                continue;
            }
            String str = method.getName().substring(3);
            char[] cs=str.toCharArray();
            cs[0]+=32;
            str = new String(cs);
            fieldMap.put(str,method);
        }

        methodCache.cls = cls;
        methodCache.methodMap = fieldMap;
        methodCacheMap.put(classSourceName,methodCache);
        return methodCache;
    }


    class MethodCache{
        Class cls;
        Map<String,Method> methodMap = new HashMap<>();
    }
}
