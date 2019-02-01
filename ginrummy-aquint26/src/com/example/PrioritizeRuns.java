package com.example;

import java.util.ArrayList;
import java.util.List;

public class PrioritizeRuns implements PlayerStrategy {
    private ArrayList<Card> deadwood;
    private ArrayList<Meld> melds;

    // If the player's deadwood count goes under this amount, knock
    private static int THRESHOLD = 5;

    public PrioritizeRuns() {
        deadwood = new ArrayList<>();
        melds = new ArrayList<>();
    }

    /**
     * Called by the game engine for each player at the beginning of each round to receive and
     * process their initial hand dealt.
     *
     * @param hand The initial hand dealt to the player
     */
    public void receiveInitialHand(List<Card> hand) {
        deadwood.addAll(hand);
        checkMelds();
    }

    /**
     * Called by the game engine to prompt the player on whether they want to take the top card
     * from the discard pile or from the deck.
     *
     * @param card The card on the top of the discard pile
     * @return whether the user takes the card on the discard pile
     */
    public boolean willTakeTopDiscard(Card card) {
        for (Meld meld : melds) {
            if (meld.canAppendCard(card)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Called by the game engine to prompt the player to take their turn given a
     * dealt card (and returning their card they've chosen to discard).
     *
     * @param drawnCard The card the player was dealt
     * @return The card the player has chosen to discard
     */
    public Card drawAndDiscard(Card drawnCard) {
        deadwood.add(drawnCard);
        checkMelds();
        if (deadwood.size() == 0) {
            return drawnCard;
        }
        Card temp = deadwood.get(0);
        deadwood.remove(0);
        return temp;
    }

    /**
     * Called by the game engine to prompt the player is whether they would like to
     * knock.
     *
     * @return True if the player has decided to knock
     */
    public boolean knock() {
        int currentPoints = 0;
        for (Card deadwoodCard : deadwood) {
            currentPoints += deadwoodCard.getPointValue();
        }

        return currentPoints <= THRESHOLD;
    }

    /**
     * Called by the game engine when the opponent has finished their turn to provide the player
     * information on what the opponent just did in their turn.
     *
     * @param drewDiscard        Whether the opponent took from the discard
     * @param previousDiscardTop What the opponent could have drawn from the discard if they chose to
     * @param opponentDiscarded  The card that the opponent discarded
     */
    public void opponentEndTurnFeedback(boolean drewDiscard, Card previousDiscardTop, Card opponentDiscarded) {

    }

    /**
     * Called by the game engine when the round has ended to provide this player strategy
     * information about their opponent's hand and selection of Melds at the end of the round.
     *
     * @param opponentHand  The opponent's hand at the end of the round
     * @param opponentMelds The opponent's Melds at the end of the round
     */
    public void opponentEndRoundFeedback(List<Card> opponentHand, List<Meld> opponentMelds) {
        melds.addAll(opponentMelds);
        checkMelds();
    }

    /**
     * Called by the game engine to allow access the player's current list of Melds.
     *
     * @return The player's list of melds.
     */
    public List<Meld> getMelds() {
        return melds;
    }

    /**
     * Called by the game engine to allow this player strategy to reset its internal state before
     * competing it against a new opponent.
     */
    public void reset() {
        melds.clear();
        deadwood.clear();
    }

    /**
     * Checks the current deadwood list to see of any of the cards form a meld or fit into a pre-existing meld.
     * If so, remove them from deadwood and put them into a new meld or add them to an existing meld
     */
    private void checkMelds() {

        // First check to see if any deadwood fit into existing melds
        for (Card deadwoodCard : deadwood) {
            for (Meld meld : melds) {
                try {
                    meld.appendCard(deadwoodCard);
                } catch (IllegalMeldModificationException e) {
                    // Card doesn't fit into the meld, so do nothing
                }
            }
        }

        /*
         * Run meld check:
         * Same process, but this time with a RunMeld instead of a SetMeld
         */
        for (int i = 0; i < deadwood.size(); i++) {
            Card meldBase = deadwood.get(i);
            // Creates a meld that contains only one card
            Card[] initialRunMeldCard = {meldBase};
            Meld potentialRunMeld = new RunMeld(initialRunMeldCard);

            // checks all other cards for meld compatibility
            for (Card potentialMeldCard : deadwood) {
                if (meldBase != potentialMeldCard) {
                    try {
                        potentialRunMeld.appendCard(potentialMeldCard);
                    } catch (IllegalMeldModificationException e) {
                        // Card doesn't fit into the meld, so do nothing
                    }
                }
            }

            // If it's a good meld, add it to the player's melds.
            // Otherwise, put potential meld cards back into the deadwood list
            if (Meld.buildRunMeld(potentialRunMeld.getCards()) != null) {
                melds.add(potentialRunMeld);

                for (Card meldedCard : potentialRunMeld.getCards()) {
                    deadwood.remove(meldedCard);
                }
            }
        } // End of RunMeld check

        /*
         * Now check to see if any deadwood cards can form a new meld
         * Set meld check:
         * creates a "meld base", to which all other cards will be compared to in order to check for meld compatibility
         */
        for (int i = 0; i < deadwood.size(); i++) {
            Card meldBase = deadwood.get(i);
            // Creates a meld that contains only one card
            Card[] initialSetMeldCard = {meldBase};
            Meld potentialSetMeld = new SetMeld(initialSetMeldCard);

            // checks all other cards for meld compatibility with the meld that had one card
            for (Card potentialMeldCard : deadwood) {
                if (meldBase != potentialMeldCard) {
                    try {
                        // If it's compatible with the meld, add it to the meld and remove it from deadwood
                        potentialSetMeld.appendCard(potentialMeldCard);
                    } catch (IllegalMeldModificationException e) {
                        // If it's not compatible, do nothing
                    }
                }
            }

            // If it's a good meld, add it to the player's melds.
            // Otherwise, put the potential meld cards back into the deadwood list
            if (Meld.buildSetMeld(potentialSetMeld.getCards()) != null) {
                melds.add(potentialSetMeld);

                for (Card meldedCard : potentialSetMeld.getCards()) {
                    deadwood.remove(meldedCard);
                }
            }
        } //End of SetMeld check

    } // End of MeldCheck


}
