package com.example;

import java.util.ArrayList;

public class Savannah extends Location {
    private double humidity;
    private double temperature;
    private double water;
    private double plants;
    private double survivalModifier;
    private double disasterChance;

    // These are the status under normal conditions, i.e. no rain or disaster
    private static final double BASE_HUMIDITY = 15.0;
    private static final double BASE_TEMPERATURE = 75.0;
    private static final double BASE_WATER = 15.0;
    private static final double BASE_PLANTS = 20.0;
    private final static double BASE_SURVIVAL_MODIFIER = 1.4;

    private static final double RAIN_CHANCE = 0.1;
    private static final double BASE_DISASTER_CHANCE = 0.2;

    // Amount that water increases when it rains/a disaster occurs
    private static final double RAIN_WATER_INCREASE = 10.0;

    // Change in survival modifier when a disaster occurs
    private static final double DISASTER_SURVIVAL_CHANGE = -0.5;

    public Savannah() {
        humidity = BASE_HUMIDITY;
        temperature = BASE_TEMPERATURE;
        water = BASE_WATER;
        plants = BASE_PLANTS;
        survivalModifier = BASE_SURVIVAL_MODIFIER;
        disasterChance = BASE_DISASTER_CHANCE;
    }

    /**
     * Advances the epoch for each animal, and rolls for rain and disaster
     */
    public void nextEpoch() {
        if (Math.random() < RAIN_CHANCE) {
            rain();
        }

        if (Math.random() < disasterChance) {
            disaster();
        }

        ArrayList<Animal> animals = super.getAnimals();
        ArrayList<Shrub> shrubs = super.getShrubs();

        // Advances epoch for all shrubs & animals
        for (Animal animal : animals) {
            animal.nextEpoch(humidity, temperature, water, plants, survivalModifier);
        }
        for (Shrub shrub : shrubs) {
            shrub.nextEpoch(humidity, temperature, water, plants, survivalModifier);
        }

        // Tests for survival for each animal and shrub
        for (int i = 0; i < animals.size(); i++) {
            if (!animals.get(i).getIsAlive()) {
                animals.remove(animals.get(i));
                i--;
            }
        }
        for (int i = 0; i < shrubs.size(); i++) {
            if (!shrubs.get(i).getIsAlive()) {
                shrubs.remove(shrubs.get(i));
                i--;
            }
        }

        // Finds a mate for each animal
        for (int i = 0; i < animals.size(); i++) {
            animals.get(i).findMate(animals);
        }

        reset();

    }

    /**
     * Increases water for that epoch, increases odds of disaster
     */
    public void rain() {
        water += RAIN_WATER_INCREASE;
    }

    /**
     * Greatly decreases survival chance
     * Represents a fire
     */
    public void disaster() {
        survivalModifier += DISASTER_SURVIVAL_CHANGE;
    }

    /**
     * Resets all stats back to their normal value
     */
    public void reset() {
        humidity = BASE_HUMIDITY;
        temperature = BASE_TEMPERATURE;
        water = BASE_WATER;
        plants = BASE_PLANTS;
        disasterChance = BASE_DISASTER_CHANCE;
        for (Animal animal : getAnimals()) {
            animal.setHasMated(false);
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", Savannah";
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

    public double getSurvivalModifier() {
        return survivalModifier;
    }
}
