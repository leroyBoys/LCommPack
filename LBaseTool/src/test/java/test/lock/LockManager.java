package test.lock;

import java.util.concurrent.ConcurrentHashMap;

public class LockManager {
    final static int maxSize = 50;
    final static ConcurrentHashMap<String,RedisClubLock> cache = new ConcurrentHashMap<>(maxSize);
    public final static RedisClubLock getLock(final String str){
        synchronized (str){
            RedisClubLock v =cache.get(str);
            if(v != null){
                return v;
            }

            return cache.computeIfAbsent(str,key->{
                return new RedisClubLock(key);
            });
        }
    }

   protected static void finsh(String key) {
       cache.remove(key);
    }







}
