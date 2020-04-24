package objects;

import cache.CacheKey;
import cache.MethodParams;
import cache.ObjectCache;

final public class FxRate {

    final double rate;

    private FxRate(double rate) {
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
        return "FxRate: "+rate;
    }

    public static FxRate getFxRateFrom(double rate, boolean useCache) {
        String methodName = "getFxRateFrom";
        FxRate fxRate = null;
        if (useCache && ObjectCache.getInstance() != null) {
            ObjectCache cache = ObjectCache.getInstance();

            MethodParams params = new MethodParams.Builder()
                    .addParam(rate)
                    .build();

            CacheKey key = new CacheKey(methodName, params);
            fxRate = (FxRate) cache.getCached(key);

            if (fxRate == null) { //cache miss
                System.out.println("Creating new FxRate object with rate: " + rate);
                fxRate = new FxRate(rate);
                cache.offer(key, fxRate);
            }

        } else {
            System.out.println("Creating new FxRate object with rate: " + rate);
            fxRate = new FxRate(rate);
        }

        return fxRate;
    }

}
