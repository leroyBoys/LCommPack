package com.lgame.mysql;

import com.lgame.mysql.impl.DbFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leroy:656515489@qq.com
 * 2017/5/5.
 */
public class DbFactoryCache {
    private static DbFactoryCache ourInstance = new DbFactoryCache();

    public static DbFactoryCache getInstance() {
        return ourInstance;
    }

    private DbFactoryCache() {
    }
    private static final Map<String,DbFactory> dbFactorys = new HashMap<>(10);

    public <T extends DbFactory> T getDbFactory(Class<T> cls){
       try {
           DbFactory dbFactory = dbFactorys.get(cls.getName());
           if(dbFactory == null){
               dbFactory = cls.newInstance();
               dbFactorys.put(cls.getName(),dbFactory);
           }

           return (T) dbFactory;
       }catch (Exception e){
           e.printStackTrace();
           try {
               Thread.sleep(10);
           } catch (InterruptedException e1) {
               e1.printStackTrace();
           }
           return (T) dbFactorys.get(cls);
       }
    }

    public synchronized  <T extends DbFactory> void addDbFactory(Class<T> cls){
        DbFactory dbFactory =  dbFactorys.get(cls.getName());
        if(dbFactory != null){
            return;
        }

        try {
            dbFactorys.put(cls.getName(),cls.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public synchronized <T extends DbFactory> void addDbFactory(T cls){
        DbFactory dbFactory =  dbFactorys.get(cls.getClass().getName());
        if(dbFactory != null){
            return;
        }

        dbFactorys.put(cls.getClass().getName(),cls);
    }
}
