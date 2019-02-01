package com.example;

import com.sun.tools.javac.code.Attribute;

import java.util.ArrayList;

public class GameEngine {
    private static final int HAND_SIZE = 10;
    private static final int POINTS_TO_WIN = 50;
    private static final int GIN_POINT_REWARD = 25;

    private ArrayList<Card> deck;
    private ArrayList<Card> discardPile = new ArrayList<>();
    private int playerOnePoints;
    private int playerTwoPoints;

    private PlayerStrategy startingPlayer = null;
    private PlayerStrategy notStartingPlayer = null;
    private ArrayList<Card> startingPlayerHand;
    private ArrayList<Card> notStartingPlayerHand;

    /*
     * These are used to keep track of what cards the players have since the interface
     * doesn't have a method that shows the game engine the players hand.
     *
     * Both will have to be continuously modified and maintained throughout each step of the game
     * as opposed to having a small method defined in the interface that could just return a single arrayList of their cards...
     */
    private ArrayList<Card> playerOneHand = new ArrayList<>();
    private ArrayList<Card> playerTwoHand = new ArrayList<>();

    public PlayerStrategy playGame(PlayerStrategy playerOne, PlayerStrategy playerTwo) {
        playerOnePoints = 0;
        playerTwoPoints = 0;

        while (playerOnePoints < POINTS_TO_WIN && playerTwoPoints < POINTS_TO_WIN) {
            playRound(playerOne, playerTwo);
        }

        if (playerOnePoints >= POINTS_TO_WIN) {
            System.out.println("Final Score:\n" +
                    "Player One Points: " + playerOnePoints + "\n" +
                    "Player Two Points: " + playerTwoPoints);
            return playerOne;
        } else {
            System.out.println("Final Score:\n" +
                    "Player One Points: " + playerOnePoints + "\n" +
                    "Player Two Points: " + playerTwoPoints);
            return playerTwo;
        }
    }

    /**
     * Plays a round of Gin Rummy between the two player strategies.
     * Increases the winning player's corresponding point sum by a certain amount depending on the win condition
     *
     * @param playerOne First player in the game
     * @param playerTwo Second player in the game
     */
    private void playRound(PlayerStrategy playerOne, PlayerStrategy playerTwo) {
        //System.out.println("Played a new round");

        // Initial setup for the round
        deck = new ArrayList<>(Card.getAllCards());
        shuffleDeck();
        playerOneHand.clear();
        playerTwoHand.clear();
        discardPile.clear();
        playerOne.reset();
        playerTwo.reset();

        for (int i = 0; i < HAND_SIZE; i++) {
            playerOneHand.add(deck.remove(0));
            playerTwoHand.add(deck.remove(0));
        }

        playerOne.receiveInitialHand(playerOneHand);
        playerTwo.receiveInitialHand(playerTwoHand);
        discard();

        Card discardPileTop = discardPile.get(discardPile.size() - 1);
        Card deckTop = deck.get(deck.size() - 1);
        Card cardPlayerDiscarded;

        // Randomly selects the starting player
        if (startingPlayer == null || notStartingPlayer == null) {
            if (Math.random() >= 0.5) {
                startingPlayer = playerOne;
                notStartingPlayer = playerTwo;

                startingPlayerHand = playerOneHand;
                notStartingPlayerHand = playerTwoHand;
            } else {
                startingPlayer = playerTwo;
                notStartingPlayer = playerOne;

                startingPlayerHand = playerTwoHand;
                notStartingPlayerHand = playerOneHand;
            }
        }

        PlayerStrategy playerThatWentFirst;
        PlayerStrategy playerThatWentSecond;
        /*
         * First turn: player one decides whether to take top discard.
         * If not, player two then decides whether to take the top discard.
         * If neither want to, then player one draws from the deck.
         * Adds discarded card to the discard pile.
         */
        if (startingPlayer.willTakeTopDiscard(discardPileTop)) {

            cardPlayerDiscarded = startingPlayer.drawAndDiscard(discardPileTop);
            discardPile.remove(discardPileTop);
            discardPile.add(cardPlayerDiscarded);
            startingPlayerHand.add(discardPileTop);
            startingPlayerHand.remove(cardPlayerDiscarded);

            notStartingPlayer.opponentEndTurnFeedback(true, discardPileTop, cardPlayerDiscarded);

            playerThatWentFirst = startingPlayer;
            playerThatWentSecond = notStartingPlayer;

        } else if (notStartingPlayer.willTakeTopDiscard(discardPileTop)) {

            cardPlayerDiscarded = notStartingPlayer.drawAndDiscard(discardPileTop);
            discardPile.remove(discardPileTop);
            discardPile.add(cardPlayerDiscarded);
            notStartingPlayerHand.add(discardPileTop);
            notStartingPlayerHand.remove(cardPlayerDiscarded);

            startingPlayer.opponentEndTurnFeedback(true, discardPileTop, cardPlayerDiscarded);

            playerThatWentFirst = notStartingPlayer;
            playerThatWentSecond = startingPlayer;

        } else {

            cardPlayerDiscarded = startingPlayer.drawAndDiscard(deckTop);
            discardPile.add(cardPlayerDiscarded);
            deck.remove(deckTop);
            startingPlayerHand.add(deckTop);
            startingPlayerHand.remove(cardPlayerDiscarded);

            notStartingPlayer.opponentEndTurnFeedback(false, discardPileTop, cardPlayerDiscarded);

            playerThatWentFirst = startingPlayer;
            playerThatWentSecond = notStartingPlayer;
        }

        // Set up list variables that point to the hands of the players that went first/second
        ArrayList<Card> playerWentFirstHand;
        ArrayList<Card> playerWentSecondHand;

        if (playerThatWentFirst == playerOne) {
            playerWentFirstHand = playerOneHand;
            playerWentSecondHand = playerTwoHand;
        } else {
            playerWentFirstHand = playerTwoHand;
            playerWentSecondHand = playerOneHand;
        }

        // Game setup is done, main turn loop begins here
        boolean playersTakingTurns = true;

        while (playersTakingTurns) {

            //System.out.println("Took a new turn");

            discardPileTop = discardPile.get(discardPile.size() - 1);
            deckTop = deck.get(deck.size() - 1);

            /*
             * Added to avoid situation where players might use cards from previous turns,
             * since that would otherwise be hard to detect
             */
            cardPlayerDiscarded = null;

            /*
             * Player that went second takes the next turn after the first turn.
             * Each player decides whether to draw and discard from the deck or the discard pile.
             */

            // Handle the turn of the player that didn't start
            if (playerThatWentSecond.willTakeTopDiscard(discardPileTop)) {
                cardPlayerDiscarded = playerThatWentSecond.drawAndDiscard(discardPileTop);
                discardPile.add(cardPlayerDiscarded);
                discardPile.remove(discardPileTop);
                playerWentSecondHand.add(discardPileTop);
                playerWentSecondHand.remove(cardPlayerDiscarded);

                playerThatWentFirst.opponentEndTurnFeedback(true, discardPileTop, cardPlayerDiscarded);

            } else {
                cardPlayerDiscarded = playerThatWentSecond.drawAndDiscard(deckTop);
                discardPile.add(cardPlayerDiscarded);
                deck.remove(deckTop);

                playerWentSecondHand.add(deckTop);
                playerWentSecondHand.remove(cardPlayerDiscarded);

                playerThatWentFirst.opponentEndTurnFeedback(false, discardPileTop, cardPlayerDiscarded);
            }

            discardPileTop = discardPile.get(discardPile.size() - 1);
            if (deck.size() > 0) {
                deckTop = deck.get(deck.size() - 1);
            } else {
                return;
            }

            // Handle the turn of the player that started
            if (playerThatWentFirst.willTakeTopDiscard(discardPileTop)) {
                cardPlayerDiscarded = playerThatWentFirst.drawAndDiscard(discardPileTop);
                discardPile.add(cardPlayerDiscarded);
                discardPile.remove(discardPileTop);

                playerWentFirstHand.add(discardPileTop);
                playerWentFirstHand.remove(cardPlayerDiscarded);

                playerThatWentSecond.opponentEndTurnFeedback(true, discardPileTop, cardPlayerDiscarded);

            } else {
                cardPlayerDiscarded = playerThatWentFirst.drawAndDiscard(deckTop);
                discardPile.add(cardPlayerDiscarded);
                deck.remove(deckTop);

                playerWentFirstHand.add(deckTop);
                playerWentFirstHand.remove(cardPlayerDiscarded);

                playerThatWentSecond.opponentEndTurnFeedback(false, discardPileTop, cardPlayerDiscarded);
            }

            // Check to see if the game ties at this point
            if (deck.size() <= 0) {
                return;
            }

            // Check for knocks
            if (calculateDeadwoodPoints(playerOne, playerOneHand) <= 10 && playerOne.knock()) {
                playersTakingTurns = false;
            }
            if (calculateDeadwoodPoints(playerTwo, playerTwoHand) <= 10 && playerTwo.knock()) {
                playersTakingTurns = false;
            }
        } // End of turn loop

        // Sets up objects pointing to who did/didn't knock
        PlayerStrategy knockingPlayer;
        PlayerStrategy notKnockingPlayer;

        // Set up yet another pair of player hand references because playerStrategies can't tell us what cards they have...
        ArrayList<Card> knockingPlayerHand;
        ArrayList<Card> notKnockingPlayerHand;

        if (playerOne.knock()) {
            knockingPlayer = playerOne;
            notKnockingPlayer = playerTwo;
            knockingPlayerHand = playerOneHand;
            notKnockingPlayerHand = playerTwoHand;
        } else {
            knockingPlayer = playerTwo;
            notKnockingPlayer = playerOne;
            knockingPlayerHand = playerTwoHand;
            notKnockingPlayerHand = playerOneHand;
        }

        // Calculate deadwood for players to determine the win conditions
        int knockingPlayerPoints = calculateDeadwoodPoints(knockingPlayer, knockingPlayerHand);
        int notKnockingPlayerPoints = calculateDeadwoodPoints(notKnockingPlayer, notKnockingPlayerHand);


        // Decide win conditions
        if (knockingPlayerPoints == 0) {
            // Give player with Gin feedback, calculate deadwood values for players
            knockingPlayer.opponentEndRoundFeedback(playerTwoHand, playerTwo.getMelds());

            // give that player 25 + other player's deadwood points
            if (knockingPlayer == playerOne) {
                playerOnePoints += GIN_POINT_REWARD + notKnockingPlayerPoints;
            } else {
                playerTwoPoints += GIN_POINT_REWARD + notKnockingPlayerPoints;
            }
        } else if (knockingPlayerPoints > notKnockingPlayerPoints) {
            // Give players feedback
            knockingPlayer.opponentEndRoundFeedback(playerTwoHand, playerTwo.getMelds());
            notKnockingPlayer.opponentEndRoundFeedback(playerOneHand, playerOne.getMelds());

            // Recalculate player points
            knockingPlayerPoints = calculateDeadwoodPoints(knockingPlayer, knockingPlayerHand);
            notKnockingPlayerPoints = calculateDeadwoodPoints(notKnockingPlayer, notKnockingPlayerHand);

            // grant knocking player the difference of points
            if (knockingPlayer == playerOne) {
                playerOnePoints += knockingPlayerPoints + notKnockingPlayerPoints;
            } else {
                playerTwoPoints += GIN_POINT_REWARD + notKnockingPlayerPoints;
            }
        } else if (knockingPlayerPoints < notKnockingPlayerPoints) {
            // Give players feedback
            knockingPlayer.opponentEndRoundFeedback(playerTwoHand, playerTwo.getMelds());
            notKnockingPlayer.opponentEndRoundFeedback(playerOneHand, playerOne.getMelds());

            // Recalculate player points
            knockingPlayerPoints = calculateDeadwoodPoints(knockingPlayer, knockingPlayerHand);
            notKnockingPlayerPoints = calculateDeadwoodPoints(notKnockingPlayer, notKnockingPlayerHand);

            // grant not knocking player 25 + difference in points
            if (knockingPlayer == playerOne) {
                playerTwoPoints += GIN_POINT_REWARD + notKnockingPlayerPoints - knockingPlayerPoints;
            } else {
                playerOnePoints += GIN_POINT_REWARD + notKnockingPlayerPoints - knockingPlayerPoints;
            }
        }

        // Reverse starting player and non-starting player
        PlayerStrategy tempPlayer = notStartingPlayer;
        notStartingPlayer = startingPlayer;
        startingPlayer = tempPlayer;

        ArrayList<Card> tempPlayerHand = notStartingPlayerHand;
        notStartingPlayerHand = startingPlayerHand;
        startingPlayerHand = tempPlayerHand;
    } // end of playRound method

    /**
     * Randomizes the order of the elements in the deck instance variable
     */
    private void shuffleDeck() {
        ArrayList<Card> deckAsList = new ArrayList<>(deck);
        ArrayList<Card> shuffledDeck = new ArrayList<>();

        // While the deckList has cards, continually remove random cards from deckList and add them to the card set
        while (deckAsList.size() > 0) {
            shuffledDeck.add(deckAsList.remove((int) (Math.random() * deckAsList.size())));
        }

        deck = shuffledDeck;
    }

    /**
     * Moves the first card in the deck to the top of the discard pile
     */
    private void discard() {
        discardPile.add(deck.remove(0));
    }

    /**
     * Given a player and their hand, this method will return the point value of all their deadwood
     * <p>
     * The player and their hand have to be passed in as separate parameters because the playerStrategy interface
     * does not allow the engine to directly access the player's hand...
     *
     * @param player     player whose hand to look through
     * @param playerHand hand of the player
     * @return list of all deadwood cards they have
     */
    public int calculateDeadwoodPoints(PlayerStrategy player, ArrayList<Card> playerHand) {
        int points = 0;

        // Starts off as a copy of the players hand, and has meld cards removed until only deadwood is left
        ArrayList<Card> deadwood = new ArrayList<>(playerHand);

        // Iterates through all cards in the player's melds, and removes them from the copy of their hand
        for (Meld meld : player.getMelds()) {
            for (Card card : meld.getCards()) {
                deadwood.remove(card);
            }
        }

        for (Card c : deadwood) {
            points += c.getPointValue();
        }

        return points;
    }

}
