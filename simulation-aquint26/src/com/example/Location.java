package com.example;

import java.util.ArrayList;

public abstract class Location {
    private double humidity;
    private double temperature;
    private double water;
    private double plants;
    private double survivalModifier;
    private double disasterSurvivalChange;
    private ArrayList<Animal> animals;
    private ArrayList<Shrub> shrubs;

    // Creates 1-10 shrubs by default
    public Location() {
        this.animals = new ArrayList<>();
        this.shrubs = new ArrayList<>();

        int shrubsToMake = (int) (10 * Math.random());
        for (int i = 0; i < shrubsToMake; i++) {
            shrubs.add(new Shrub());
        }
    }

    /**
     * Advances the epoch for each animal, and rolls for rain and disaster
     */
    public abstract void nextEpoch();

    public abstract void rain();

    public abstract void disaster();

    /**
     * Iterates through the ArrayList of animals, and prints each of their stats
     * At the end, it prints the average stats of all the animals
     */
    public void printAnimalInfo() {
        int animalsPrinted = 1;
        double totalTemperature = 0;
        double totalHumidity = 0;
        double totalWater = 0;
        double totalPlants = 0;

        for (Animal animal : animals) {
            System.out.println("Animal " + animalsPrinted + ":\n" + animal.toString());
            animalsPrinted++;

            // Used for calculating average stats
            totalTemperature += animal.getTemperature();
            totalHumidity += animal.getHumidity();
            totalWater += animal.getWater();
            totalPlants += animal.getPlants();
        }
        System.out.println("\nAverage temperature affinity: " + totalTemperature / (animalsPrinted - 1));
        System.out.println("Average humidity affinity: " + totalHumidity / (animalsPrinted - 1));
        System.out.println("Average water affinity: " + totalWater / (animalsPrinted - 1));
        System.out.println("Average plant affinity: " + totalPlants / (animalsPrinted - 1));

        if (shrubs != null) {
            System.out.println("There are " + shrubs.size() + " shrubs");
        }
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

    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    public ArrayList<Shrub> getShrubs() {
        return shrubs;
    }

    @Override
    public String toString() {
        return "animals: " + String.valueOf(animals.size() + " Shrubs: " + String.valueOf(shrubs.size()));
    }
}
