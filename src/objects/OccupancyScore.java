package objects;

import cache.CacheKey;
import cache.MethodParams;
import cache.ObjectCache;

public final class OccupancyScore {

    final FxRate rate;
    final double score;

    private OccupancyScore(FxRate rate){
        this.rate = rate;
        score = Math.max(10 - rate.getRate(), 0);
    }

    public Double getScore() {
        return Double.valueOf(score);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(score);
    }

    @Override
    public boolean equals(Object obj) {
        return Double.valueOf(score).equals(((OccupancyScore)obj).getScore());
    }

    @Override
    public String toString() {
        return "OccupancyScore: "+score;
    }

    public static OccupancyScore getOccupancyScoreFrom(FxRate fxRate, boolean useCache) {
        String methodName = "getOccupancyScoreFrom";  //todo update this to get from the method name itself
        OccupancyScore score = null;

        if (useCache && ObjectCache.getInstance() != null) {

            ObjectCache cache = ObjectCache.getInstance();

            MethodParams params = new MethodParams.Builder()
                    .addParam(fxRate)
                    .build();

            CacheKey key = new CacheKey(methodName, params);
            score = (OccupancyScore) cache.getCached(key);

            if (score == null) { //cache miss
                System.out.println("Creating new OccupancyScore object with rate: " + fxRate);
                score = new OccupancyScore(fxRate);
                cache.offer(key, score);
            }

        } else {
            System.out.println("Creating new OccupancyScore object with rate: " + fxRate);
            score = new OccupancyScore(fxRate);
        }

        return score;
    }
}
