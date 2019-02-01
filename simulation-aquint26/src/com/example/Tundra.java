package com.example;

import java.util.ArrayList;

public class Tundra extends Location {
    private double humidity;
    private double temperature;
    private double water;
    private double plants;
    private double survivalModifier;
    private double disasterChance;

    // These are the status under normal conditions, i.e. no rain or disaster
    private static final double BASE_HUMIDITY = 25.0;
    private static final double BASE_TEMPERATURE = 30.0;
    private static final double BASE_WATER = 40.0;
    private static final double BASE_PLANTS = 40.0;
    private final static double BASE_SURVIVAL_MODIFIER = 1;

    private static final double SNOW_CHANCE = 0.3;
    private static final double BASE_DISASTER_CHANCE = 0.1;

    // Amount that water increases when it rains/a disaster occurs
    private static final double SNOW_TEMPERATURE_DECREASE = -10.0;
    private static final double DISASTER_TEMPERATURE_DECREASE = -20.0;

    // Change in survival modifier when a disaster occurs
    private static final double DISASTER_SURVIVAL_CHANGE = -0.3;

    // Increase in chance of disaster if it rains
    private static final double SNOW_DISASTER_CHANCE_INCREASE = 0.2;

    public Tundra() {
        humidity = BASE_HUMIDITY;
        temperature = BASE_TEMPERATURE;
        water = BASE_WATER;
        plants = BASE_PLANTS;
        disasterChance = BASE_DISASTER_CHANCE;
        survivalModifier = BASE_SURVIVAL_MODIFIER;
    }

    /**
     * Advances the epoch for each animal, and rolls for rain and disaster
     */
    public void nextEpoch() {
        if (Math.random() < SNOW_CHANCE) {
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
     * Decreases temperature for that epoch, increases odds of disaster
     * Rain in the tundra is represented through snow
     */
    public void rain() {
        temperature += SNOW_TEMPERATURE_DECREASE;
        disasterChance += SNOW_DISASTER_CHANCE_INCREASE;
    }

    /**
     * modifies stats in some way for that specific epoch
     * Represents a blizzard
     */
    public void disaster() {
        temperature += DISASTER_TEMPERATURE_DECREASE;
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
        return super.toString() + ", Tundra";
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
