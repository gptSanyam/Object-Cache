package objects;

import cache.CacheKey;
import cache.MethodParams;
import cache.ObjectCache;

public class Flight {
    final FuelCost cost;
    final double fuelQuantity;
    final OccupancyScore score;
    final String name;

    final double flightPrice;

    private Flight(String name, FuelCost cost, double fuelQuantity, OccupancyScore score){
        this.name = name;
        this.fuelQuantity = fuelQuantity;
        this.cost = cost;
        this.score = score;
        flightPrice = fuelQuantity * this.cost.getCost() + 100 - score.getScore();
    }

    public Double getFlightPrice(){
        return flightPrice;
    }

    @Override
    public int hashCode() {
        Long hashSum = 0l;
        hashSum+=(name.hashCode()+Double.hashCode(fuelQuantity));
        hashSum = Long.valueOf(hashSum.hashCode());
        hashSum+=(cost.hashCode()+score.hashCode());
        return hashSum.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Flight fuelCostObj = (Flight) obj;
        return name.equals(fuelCostObj.name) &&
                Double.valueOf(fuelQuantity).equals(fuelCostObj.fuelQuantity) &&
                cost.equals(fuelCostObj.cost) &&
                score.equals(fuelCostObj.score);
    }

    @Override
    public String toString() {
        return "Name: "+ name+" ; FlightPrice: "+ flightPrice;
    }

    public static Flight getFlightFrom(String name, FuelCost cost, double fuelQuantity, OccupancyScore score, boolean useCache) {
        {
            String methodName = "getFlightFrom";  //todo update this to get from the method name itself
            Flight flight = null;

            if (useCache && ObjectCache.getInstance() != null) {

                ObjectCache cache = ObjectCache.getInstance();

                MethodParams params = new MethodParams.Builder()
                        .addParam(name)
                        .addParam(cost)
                        .addParam(fuelQuantity)
                        .addParam(score)
                        .build();

                CacheKey key = new CacheKey(methodName, params);
                flight = (Flight) cache.getCached(key);

                if (flight == null) { //cache miss
                    System.out.println("Creating new Flight object with Name: " + name + " , FuelCost: " + cost.getCost()+
                            ", FuelQty: "+fuelQuantity+ " and OS: "+ score);
                    flight = new Flight(name, cost, fuelQuantity, score);
                    cache.offer(key, flight);
                }

            } else {
                System.out.println("Creating new Flight object with Name: " + name + " , FuelCost: " + cost.getCost()+
                        ", FuelQty: "+fuelQuantity+ " and OS: "+ score);
                flight = new Flight(name, cost, fuelQuantity, score);
            }

            return flight;
        }
    }

}
