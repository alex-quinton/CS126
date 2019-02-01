package com.example;

public class Shrub extends Thing {
    private double survivalOdds;
    private boolean isAlive;

    private static final double BASE_SURVIVAL_ODDS = 0.7;

    // Represents the tolerance range for how the conditions are
    private static final double CONDITIONS_LOWER_BOUND = 150;
    private static final double CONDITIONS_UPPER_BOUND = 150;

    // Survival modifier for when conditions are too harsh
    private static final double CONDITION_FAILURE_MODIFIER = -0.5;


    public Shrub() {
        survivalOdds = BASE_SURVIVAL_ODDS;
    }

    public boolean nextEpoch(double humidity, double temperature, double water, double plants, double survivalMod) {
        // Represents how harsh the conditions are
        double conditions = humidity + temperature + water + plants;

        // If the condition harshness is not within the acceptable boundaries, then the shrub dies
        if (conditions < CONDITIONS_LOWER_BOUND || conditions > CONDITIONS_UPPER_BOUND) {
            survivalOdds += CONDITION_FAILURE_MODIFIER;
        }

        // If false, it will be removed from the shrub list
        isAlive = Math.random() < survivalOdds;
        return isAlive;
    }

    public boolean getIsAlive() {
        return isAlive;
    }
}
