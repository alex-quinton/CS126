package com.example;

import java.util.Scanner;

public class GameRunner {
    public static void main(String[] args) {

        // Initial setup for players and win counts
        int playerOneWinCount = 0;
        int playerTwoWinCount = 0;
        GameEngine game = new GameEngine();
        Scanner inputReader = new Scanner(System.in);

        // Prompts user for number of games to play
        System.out.println("How many games would you like to play for each competition?");
        int numberOfGames = inputReader.nextInt();


        // First match
        PlayerStrategy playerOne = new PrioritizeRuns();
        PlayerStrategy playerTwo = new KnockUnderThreshold();
        for (int i = 0; i < numberOfGames; i++) {
            //System.out.println("Played game");
            PlayerStrategy winner = game.playGame(playerOne, playerTwo);

            if (winner == playerOne) {
                playerOneWinCount++;
                System.out.println("PrioritizeRuns won");
            }
            if (winner == playerTwo) {
                playerTwoWinCount++;
                System.out.println("KnockUnderThreshold won");
            }
        }

        System.out.println("PrioritizeRuns wins: " + playerOneWinCount + "\n" +
                "KnockUnderThreshold wins: " + playerTwoWinCount + "\n");


        // Second match
        playerOne = new PrioritizeRuns();
        playerTwo = new AlwaysKnocks();
        for (int i = 0; i < numberOfGames; i++) {
            //System.out.println("Played game");
            PlayerStrategy winner = game.playGame(playerOne, playerTwo);

            if (winner == playerOne) {
                playerOneWinCount++;
                System.out.println("PrioritizeRuns won");
            }
            if (winner == playerTwo) {
                playerTwoWinCount++;
                System.out.println("AlwaysKnocks won");
            }
        }

        System.out.println("PrioritizeRuns wins: " + playerOneWinCount + "\n" +
                "AlwaysKnocks wins: " + playerTwoWinCount + "\n");

        // Third match
        playerOne = new KnockUnderThreshold();
        playerTwo = new AlwaysKnocks();
        for (int i = 0; i < numberOfGames; i++) {
            //System.out.println("Played game");
            PlayerStrategy winner = game.playGame(playerOne, playerTwo);

            if (winner == playerOne) {
                playerOneWinCount++;
                System.out.println("KnockUnderThreshold won");
            }
            if (winner == playerTwo) {
                playerTwoWinCount++;
                System.out.println("AlwaysKnocks won");
            }
        }

        System.out.println("KnockUnderThreshold wins: " + playerOneWinCount + "\n" +
                "AlwaysKnocks wins: " + playerTwoWinCount + "\n");
    }
}
