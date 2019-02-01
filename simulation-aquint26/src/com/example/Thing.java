package com.example;

public abstract class Thing {

    /**
     * Makes whatever changes necessary for that epoch based on given conditions
     */
    public abstract boolean nextEpoch(double humidity, double temperature, double water, double plants, double survivalMod);
}
