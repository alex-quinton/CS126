package com.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Simulation {
    private static ArrayList<Location> locations = new ArrayList<>();
    private static Location currentLocation;
    private static final double NEW_ANIMAL_STAT_MIN = 30;
    private static final double NEW_ANIMAL_STAT_MAX = 70;

    public static void main(String[] args) {

        Scanner userInputReader = new Scanner(System.in);

        System.out.println("Welcome to the evolution simulator.\n" +
                "Type \"make locationName\" to make a location.\n" +
                "The available locations are : Forest, Tundra, Jungle, Savannah\n" +
                "Type \"moveto n\" to move to the nth location (location numbers can be found using worldinfo)\n" +
                "Type \"make animal n\" to make n animals\n" +
                "You can also make shrubs\n" +
                "Once you've made a location and some animals, type \"next n\" to advance n epochs" +
                "(will advance one epoch if n is blank)\n" +
                "Type \"rain\" or \"disaster\" to make there be rain or a disaster in the next epoch\n" +
                "There is also a base chance of there being either each epoch\n" +
                "Type \"locationinfo\" to get the info of the current location\n" +
                "Type \"worldinfo\" to get the list of all existing locations\n" +
                "Type \"info\" to get the info of the things in your current location\n" +
                "Type \"exit\" at any time to exit the simulation.");

    /*
     Main simulation loop starts here
     */
        while (true) {
            String input = userInputReader.nextLine();

            // Console output for empty inputs
            if (input.length() == 0) {
                System.out.println("Input must contain characters");
            }

            // Quit the game
            else if (input.equalsIgnoreCase("exit")) {
                System.exit(0); // 0 to show nothing went wrong
            }

            // Move to a different location
            else if (input.length() >= 6
                    && input.substring(0, 6).equalsIgnoreCase("moveto")
                    && input.trim().indexOf(' ') != -1
                    && Character.isDigit(input.trim().charAt(input.length() - 1))) {

                // Gets the index of the location to move to from input
                int locationIndex = Integer.parseInt(input.substring(input.indexOf(' ')).trim());

                if (locationIndex < 0 || locationIndex > locations.size() - 1) {
                    System.out.println("You must enter a valid location index");
                } else {
                    currentLocation = locations.get(locationIndex);
                    System.out.println("New location info: " + currentLocation.toString());
                }
            }

            // Print info about the current location
            else if (input.trim().equalsIgnoreCase("locationinfo")) {
                if (currentLocation == null) {
                    System.out.println("You are not currently in a location");
                } else {
                    System.out.println(currentLocation.toString());
                }
            }

            // List current existing locations
            else if (input.trim().equalsIgnoreCase("worldinfo")) {
                printWorldInfo();
            }

            // List current existing locations
            else if (input.trim().equalsIgnoreCase("info")) {
                if (currentLocation == null) {
                    System.out.println("You must be in a location to use this command");
                } else {
                    currentLocation.printAnimalInfo();
                }
            }

            // Creates rain in the current location for the next epoch
            else if (input.trim().equalsIgnoreCase("rain")) {
                if (currentLocation == null) {
                    System.out.println("You must be in a location for it to rain");
                } else {
                    currentLocation.rain();
                }
            }

            // Creates a disaster in the current location for the next epoch
            else if (input.trim().equalsIgnoreCase("disaster")) {
                if (currentLocation == null) {
                    System.out.println("You must be in a location for there to be a disaster");
                } else {
                    currentLocation.disaster();
                }
            }

            // Handle make command, 4 is the length of the word "make"
            else if (input.length() >= 4
                    && input.substring(0, 4).equalsIgnoreCase("make")
                    && !input.equalsIgnoreCase("make")) {
                makeThing(input.substring(input.indexOf(' ')));
            }

            // Handle make command, 4 is the length of the word "next"
            else if (input.length() >= 4 &&
                    input.substring(0, 4).equalsIgnoreCase("next")) {
                nextEpoch(input);
            } else {
                System.out.println("I don't understand '" + input + "'");
            }

        } // End of main game loop
    }

    /**
     * Makes either a location or an animal
     * Animals are placed in the current location
     */
    public static void makeThing(String input) {
        String[] inputWords = input.split("[ ]+");

        if (inputWords.length < 1) {
            System.out.println("Please specify something to make");
            return;
        }

        String thingToMake = inputWords[1];
        int numberOfThings;

        // If number parameter is left blank
        if (inputWords.length == 2) {
            numberOfThings = 1;
        } else {
            try {
                numberOfThings = Integer.parseInt(inputWords[2].trim());
            } catch (NumberFormatException e) {
                System.out.println("Input must follow the format \"make thing_name number\"");
                numberOfThings = 1;
            }
        }

        // If the player wants to make a location
        if (thingToMake.equalsIgnoreCase("forest")) {
            locations.add(new Forest());
        } else if (thingToMake.equalsIgnoreCase("tundra")) {
            locations.add(new Tundra());
        } else if (thingToMake.equalsIgnoreCase("jungle")) {
            locations.add(new Jungle());
        } else if (thingToMake.equalsIgnoreCase("savannah")) {
            locations.add(new Savannah());

        // If the player wants to make an animal
        } else if (thingToMake.equalsIgnoreCase("animal")) {
            if (currentLocation == null) {
                System.out.println("You cannot make an animal until you are in a location");
            } else {

                for (int i = 0; i < numberOfThings; i++) {
                    // Randomizes all stats between the max & min of new animal stats
                    currentLocation.getAnimals().add(new Animal(
                            Math.round(NEW_ANIMAL_STAT_MIN + Math.random() * (NEW_ANIMAL_STAT_MAX - NEW_ANIMAL_STAT_MIN)),
                            Math.round(NEW_ANIMAL_STAT_MIN + Math.random() * (NEW_ANIMAL_STAT_MAX - NEW_ANIMAL_STAT_MIN)),
                            Math.round(NEW_ANIMAL_STAT_MIN + Math.random() * (NEW_ANIMAL_STAT_MAX - NEW_ANIMAL_STAT_MIN)),
                            Math.round(NEW_ANIMAL_STAT_MIN + Math.random() * (NEW_ANIMAL_STAT_MAX - NEW_ANIMAL_STAT_MIN))
                    ));
                }
            }

        // If the player wants to make a shrub
        } else if (thingToMake.equalsIgnoreCase("shrub")) {
            if (currentLocation == null) {
                System.out.println("You must be in a location to make a shrub");
            } else {

                for (int i = 0; i < numberOfThings; i++) {
                    currentLocation.getShrubs().add(new Shrub());
                }
            }
        } else {
            System.out.println("You can't make" + thingToMake);
        }
    }

    /**
     * Advances the epoch in the current location
     */
    public static void nextEpoch(String input) {
        if (currentLocation == null) {
            System.out.println("You must be in a location to advance an epoch");
        }

        // Get the number of epochs to advance through from the input
        int epochsToAdvance;
        if (input.trim().indexOf(' ') == -1) {
            epochsToAdvance = 1;
        } else {
            epochsToAdvance = Integer.parseInt(input.substring(input.indexOf(' ')).trim());
        }

        // Calls nextEpoch on the current location for the amount of times specified by the player
        for (int i = 0; i < epochsToAdvance; i++) {
            if (currentLocation != null) {
                currentLocation.nextEpoch();
            }
        }
    }

    private static void printWorldInfo() {
        // Used to number all of the worlds
        int worldNumber = 0;

        // Prints each location's toString
        for (Location location : locations) {
            System.out.println("World " + worldNumber + ": " + location.toString());
            worldNumber++;
        }

        if (locations.size() == 0) {
            System.out.println("This world currently has no locations");
        }
    }

}
