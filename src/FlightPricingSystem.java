import objects.*;

import java.util.Random;
import java.util.concurrent.*;

public class FlightPricingSystem {

    private boolean useCache = true;
    double localrate = 70.0;

    public static void main(String[] args) {
        FlightPricingSystem fps = new FlightPricingSystem();
        //fps.start();
        testCaching();
    }


    private void start() {
        RateSimulator sim = new RateSimulator(this, 8.0);
        ScheduledExecutorService rateChangeExecutor = Executors.newSingleThreadScheduledExecutor();
        rateChangeExecutor.scheduleAtFixedRate(sim, 0, 10, TimeUnit.MILLISECONDS);

    }

    class RateSimulator implements Runnable{
        FlightPricingSystem priceSystem;
        double avgPrice;
        Random randomNumGen = new Random();

        public RateSimulator(FlightPricingSystem pricingSystem, double avgPrice){
            this.avgPrice = avgPrice;
            this.priceSystem = pricingSystem;
        }

        @Override
        public void run() {
            double increment = randomNumGen.nextInt(100)/100.0;
            priceSystem.generateFlightPrices(avgPrice + increment);

        }
    }

    public void generateFlightPrices(double price){
        FxRate fxRate = FxRate.getFxRateFrom(price, useCache);

        LocalRate localRate = LocalRate.getLocalRateFrom(localrate, useCache);

        OccupancyScore score = OccupancyScore.getOccupancyScoreFrom(fxRate, useCache);

        FuelCost fuelCost = FuelCost.getFuelCostFrom(fxRate, localRate, useCache);

        Flight flight = Flight.getFlightFrom("OptimusPrime", fuelCost, 1000.0, score, useCache);
        System.out.println(flight.toString());
    }

    private static void testCaching() {
        FxRate rate = FxRate.getFxRateFrom(8.13, true);
        FxRate rate2 = FxRate.getFxRateFrom(8.13, true);
        System.out.println(rate==rate2);

        LocalRate rate3 = LocalRate.getLocalRateFrom(8.22, true);
        LocalRate rate4 = LocalRate.getLocalRateFrom(8.22, true);
        System.out.println(rate3==rate4);

        OccupancyScore score = OccupancyScore.getOccupancyScoreFrom(rate, true);
        OccupancyScore score1 = OccupancyScore.getOccupancyScoreFrom(rate2, true);
        System.out.println(score==score1);

        FuelCost fc1 = FuelCost.getFuelCostFrom(rate, rate3, true);
        FuelCost fc2 = FuelCost.getFuelCostFrom(rate2, rate4, true);
        System.out.println(fc1==fc2);

        Flight fl1 = Flight.getFlightFrom("F22", fc1, 1000.0, score, true);
        Flight fl2 = Flight.getFlightFrom("F22", fc2, 1000.0, score1, true);
        System.out.println(fl1==fl2);
    }


}
