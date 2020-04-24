package cache;

import java.util.Arrays;

public final class CacheKey{  // has to generate same hash for a method name and params
    final String methodName;
    final MethodParams methodParams;

    public CacheKey(String methodName, MethodParams params){
        this.methodName = methodName;
        this.methodParams = params;
    }

    @Override
    public int hashCode() {
        Long hashSum = Long.valueOf(methodName.hashCode())+Long.valueOf(methodParams.hashCode());
        return hashSum.hashCode();
    }

    @Override
    public boolean equals(Object ob){
        CacheKey other = (CacheKey) ob;
        return other.methodName.equals(this.methodName) &&
                Arrays.equals(((CacheKey) ob).methodParams.args, methodParams.args);
    }

}