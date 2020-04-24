package cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// | Method Name | Method Params |
//  MethodParam - Array of Objects (parameters)

public class ObjectCache {

    private static ObjectCache cache;
    Map<CacheKey, Object> cacheMap;

    private ObjectCache(){
        cacheMap = new ConcurrentHashMap<>();
    }

    public static synchronized ObjectCache getInstance(){
        if(cache!=null){
            return cache;
        }
        System.out.println("Creating cache");
        cache = new ObjectCache();
        return cache;
    }

    public Object getCached(CacheKey key){
        return cacheMap.get(key);
    }

    public void offer(CacheKey key, Object obj){
        cacheMap.put(key, obj);
        System.out.println("Cache size: "+ cacheMap.size());
    }


}
