package objects;

import cache.CacheKey;
import cache.MethodParams;
import cache.ObjectCache;

public final class LocalRate {

    final double rate;

    private LocalRate(double rate) {
        this.rate = rate;
    }

    public double getRate(){
        return rate;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(rate);
    }

    @Override
    public boolean equals(Object obj) {
        return Double.valueOf(rate).equals((Double)obj);
    }

    @Override
    public String toString() {
        return "LocalRate: "+rate;
    }

    public static LocalRate getLocalRateFrom(double rate, boolean useCache) {
        String methodName = "getLocalRateFrom";
        LocalRate localRate = null;
        if (useCache && ObjectCache.getInstance() != null) {
            ObjectCache cache = ObjectCache.getInstance();

            MethodParams params = new MethodParams.Builder()
                    .addParam(rate)
                    .build();

            CacheKey key = new CacheKey(methodName, params);
            localRate = (LocalRate) cache.getCached(key);

            if (localRate == null) { //cache miss
                System.out.println("Creating new LocalRate object with rate: " + rate);
                localRate = new LocalRate(rate);
                cache.offer(key, localRate);
            }

        } else {
            System.out.println("Creating new LocalRate object with rate: " + rate);
            localRate = new LocalRate(rate);
        }

        return localRate;
    }

}
