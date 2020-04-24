package objects;

import cache.CacheKey;
import cache.MethodParams;
import cache.ObjectCache;

public final class FuelCost {

    final FxRate rate;
    final LocalRate localRate;
    final double cost;

    private FuelCost(FxRate rate, LocalRate localRate){
        this.rate = rate;
        this.localRate = localRate;
        cost = rate.getRate() * localRate.getRate();
    }

    public Double getCost(){
        return cost;
    }

    @Override
    public int hashCode() {
        Long hashSum = Long.valueOf(rate.hashCode()) + Long.valueOf(localRate.hashCode());
        return hashSum.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        FuelCost fuelCostObj = (FuelCost) obj;
        return rate.equals(fuelCostObj.rate) && localRate.equals(fuelCostObj.localRate);
    }

    @Override
    public String toString() {
        return "FxRate: "+ rate+" ; LocalRate: "+ localRate;
    }

    public static FuelCost getFuelCostFrom(FxRate fxRate, LocalRate localRate, boolean useCache) {
        {
            String methodName = "getFuelCostFrom";  //todo update this to get from the method name itself
            FuelCost cost = null;

            if (useCache && ObjectCache.getInstance() != null) {

                ObjectCache cache = ObjectCache.getInstance();

                MethodParams params = new MethodParams.Builder()
                        .addParam(fxRate)
                        .addParam(localRate)
                        .build();

                CacheKey key = new CacheKey(methodName, params);
                cost = (FuelCost) cache.getCached(key);

                if (cost == null) { //cache miss
                    System.out.println("Creating new FuelCost object with fxRate: " + fxRate + " and localRate: " + localRate);
                    cost = new FuelCost(fxRate, localRate);
                    cache.offer(key, cost);
                }

            } else {
                System.out.println("Creating new FuelCost object with rate: " + fxRate + " and localRate: " + localRate);
                cost = new FuelCost(fxRate, localRate);
            }

            return cost;
        }
    }
}
