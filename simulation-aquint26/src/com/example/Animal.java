package com.example;

import java.util.ArrayList;

public class Animal extends Thing {

    // Represents base chance of survival. Gets diminished according to differences between animal affinities and location stats
    private static final int BASE_AFFINITY = 400;

    // Variance in stats that an animal's child might have
    private static final double TRAIT_VARIANCE = 10;

    // Innate modifier on base affinity after stat differences are subtracted
    private static final double INNATE_SURVIVAL_MOD = 0.9;

    private boolean hasMated;
    private boolean isAlive = true;

    // Each of these variables represent affinities towards certain traits in climates
    private double humidity;
    private double temperature;
    private double water;
    private double plants;

    public Animal(double humidity, double temperature, double water, double plants) {
        this.humidity = humidity;
        this.temperature = temperature;
        this.water = water;
        this.plants = plants;

        // Prevents newly created animals from mating, which would result in infinite population increase
        this.hasMated = true;
    }

    /**
     * Determines whether this animal will survive, and if so, then this animal will search for a mate
     *
     * @param humidity    Humidity of animal's current location
     * @param temperature Temperature of animal's current location
     * @param water       Water level of animal's current location
     * @param plants      Plant density of animal's current location
     * @param survivalMod Base survival modifier of animal's current location
     */
    public boolean nextEpoch(double humidity, double temperature, double water, double plants, double survivalMod) {

        // represents how different this animal is from their climate
        double climateAffinity = BASE_AFFINITY -
                (Math.abs(this.humidity - humidity) +
                        Math.abs(this.temperature - temperature) +
                        Math.abs(this.water - water) +
                        Math.abs(this.plants - plants));

        double survivalOdds = ((INNATE_SURVIVAL_MOD * survivalMod * climateAffinity) / BASE_AFFINITY);

        // If false, it will be removed from the animal pool
        isAlive = Math.random() < survivalOdds;
        return isAlive;
    }

    /**
     * Searches randomly for a mate in the current location
     *
     * @param matingPool animals in the current location
     */
    public void findMate(ArrayList<Animal> matingPool) {
        Animal mate = null;

        // Find an animal from the mating pool that hasn't mated yet by sampling randomly from the mating pool
        // Fails after a number of attempts equal to the size of the mating pool
        for (int i = 0; i < matingPool.size(); i++) {
            Animal potentialMate = matingPool.get((int) (matingPool.size() * Math.random()));
            if (!potentialMate.getHasMated()) {
                mate = potentialMate;
            }
        }

        if (mate != null) {
            // The child's stats will be an average of its parent's stats, plus a randomized element n,
            // where -TRAIT_VARIANCE <= n <= TRAIT_VARIANCE
            double childHumidity = ((humidity + mate.getHumidity()) / 2) + (TRAIT_VARIANCE -
                    Math.round(Math.random() * (TRAIT_VARIANCE * 2)));
            double childTemperature = ((temperature + mate.getTemperature()) / 2) + (TRAIT_VARIANCE -
                    Math.round(Math.random() * (TRAIT_VARIANCE * 2)));
            double childWater = ((water + mate.getWater()) / 2) + (TRAIT_VARIANCE -
                    Math.round(Math.random() * (TRAIT_VARIANCE * 2)));
            double childPlants = ((plants + mate.getPlants()) / 2) + (TRAIT_VARIANCE -
                    Math.round(Math.random() * (TRAIT_VARIANCE * 2)));

            matingPool.add(new Animal(childHumidity, childTemperature, childWater, childPlants));

            // Makes sure an animal only mates once during each epoch
            hasMated = true;
            mate.setHasMated(true);
        }

        // If no mate is found, do nothing
    }

    public String toString() {
        return "Temperature affinity: " + temperature +
                ", Humidity affinity: " + humidity +
                ", Water affinity: " + water +
                ", Plants affinity: " + plants;
    }

    public boolean getHasMated() {
        return hasMated;
    }

    public void setHasMated(boolean hasMated) {
        this.hasMated = hasMated;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWater() {
        return water;
    }

    public double getPlants() {
        return plants;
    }
}
