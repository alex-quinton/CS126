import com.example.*;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class ginRummyTest {
    private PlayerStrategy testPlayerOne = new KnockUnderThreshold();
    private GameEngine testGame = new GameEngine();
    private ArrayList<Card> testDeck = new ArrayList<>(Card.getAllCards());

    /*
     * GameEngine tests
     */

    @Test
    public void testCalculateDeadwoodPoints() {
        ArrayList<Card> testInitialHand = new ArrayList<>();
        /*
         * Here, I must instantiate an entire testDeck and individually move 4 specific cards from it into an arrayList
         * instead of simply constructing a few cards that I have chosen by hand, because for some reason the
         * constructor for the Card class is private...
         */
        for (Card card : testDeck) {
            if (card.getRankValue() == 3) {
                testInitialHand.add(card);
            }
        }
        testInitialHand.add(pullCard(8));
        testInitialHand.add(pullCard(3));
        testPlayerOne.receiveInitialHand(testInitialHand);

        // 13 is the expected point value of the cards with rankValue of 8 and 10
        assertEquals(13, testGame.calculateDeadwoodPoints(testPlayerOne, testInitialHand));
    }

    @Test
    public void testPlayGame() {
        PlayerStrategy opponent = new PrioritizeRuns();

        assertEquals(true, testGame.playGame(testPlayerOne, opponent) != null);
    }

    /*
     * Player strategy tests
     */
    @Test
    public void testInitialHandMelds() {
        ArrayList<Card> testInitialHand = new ArrayList<>();
        ArrayList<Card> expectedCards = new ArrayList<>();
        /*
         * Here, I must instantiate an entire testDeck and individually move 4 specific cards from it into an arrayList
         * instead of simply constructing a few cards that I have chosen by hand, because for some reason the
         * constructor for the Card class is private...
         */
        for (Card card : testDeck) {
            if (card.getRankValue() == 3) {
                testInitialHand.add(card);
                expectedCards.add(card);
            }
        }
        testPlayerOne.receiveInitialHand(testInitialHand);
        ArrayList<Card> playerMeldCards = new ArrayList<>(Arrays.asList(testPlayerOne.getMelds().get(0).getCards()));
        assertEquals(true,
                expectedCards.containsAll(playerMeldCards) && playerMeldCards.containsAll(expectedCards));
    }

    @Test
    public void testInitialHandDeadwood() {
        ArrayList<Card> testInitialHand = new ArrayList<>();
        ArrayList<Card> expectedCards = new ArrayList<>();
        /*
         * Here, I must instantiate an entire testDeck and individually move 4 specific cards from it into an arrayList
         * instead of simply constructing a few cards that I have chosen by hand, because for some reason the
         * constructor for the Card class is private...
         */
        for (int i = 10; i < testDeck.size(); i++) {
            testInitialHand.add(testDeck.get(i));
            expectedCards.add(testDeck.get(i));
        }
        testPlayerOne.receiveInitialHand(testInitialHand);
        assertEquals(true,
                expectedCards.containsAll(testInitialHand) && testInitialHand.containsAll(expectedCards));
    }

    @Test
    public void testDrawAndDiscard() {
        ArrayList<Card> initialPlayerHand = new ArrayList<>();
        initialPlayerHand.add(pullCard(2));

        testPlayerOne.receiveInitialHand(initialPlayerHand);

        assertEquals(true, testPlayerOne.drawAndDiscard(pullCard(3)) != null);
    }

    @Test
    public void testWillTakeTopDiscard() {
        ArrayList<Card> testInitialHand = new ArrayList<>();
        Card cardToGivePlayer = null;
        /*
         * Here, I must instantiate an entire testDeck and individually move 4 specific cards from it into an arrayList
         * instead of simply constructing a few cards that I have chosen by hand, because
         * the constructor for the Card class is private...
         */
        int cardsAdded = 0; // Use this to make sure I only add 3 cards to the player's hand
        for (Card card : testDeck) {
            if (card.getRankValue() == 3) {
                if (cardsAdded < 3) {
                    testInitialHand.add(card);
                    cardsAdded++;
                } else {
                    cardToGivePlayer = card;
                }
            }
        }
        testPlayerOne.receiveInitialHand(testInitialHand);

        assertEquals(true, testPlayerOne.willTakeTopDiscard(cardToGivePlayer));
    }

    @Test
    public void testKnock() {
        ArrayList<Card> testInitialHand = new ArrayList<>();
        testInitialHand.add(pullCard(1));

        testPlayerOne.receiveInitialHand(testInitialHand);

        assertEquals(true, testPlayerOne.knock());
    }

    @Test
    public void testOpponentEndTurnFeedback() {
        PlayerStrategy opponent = new KnockUnderThreshold();
        ArrayList<Card> testInitialOpponentHand = new ArrayList<>();
        Card cardToGivePlayer = null;
        /*
         * Here, I must instantiate an entire testDeck and individually move 4 specific cards from it into an arrayList
         * instead of simply constructing a few cards that I have chosen by hand, because
         * the constructor for the Card class is private...
         */
        int cardsAdded = 0; // Use this to make sure I only add 3 cards to the player's hand
        for (Card card : testDeck) {
            if (card.getRankValue() == 3) {
                if (cardsAdded < 3) {
                    testInitialOpponentHand.add(card);
                    cardsAdded++;
                } else {
                    cardToGivePlayer = card;
                }
            }
        }
        ArrayList<Card> testInitialHand = new ArrayList<>();
        testInitialHand.add(cardToGivePlayer);

        opponent.receiveInitialHand(testInitialOpponentHand);
        testPlayerOne.receiveInitialHand(testInitialHand);

        testPlayerOne.opponentEndRoundFeedback(testInitialOpponentHand, opponent.getMelds());

        // Tests to see if the player has gained a meld based off of the opponents meld
        assertEquals(1, testPlayerOne.getMelds().size());
    }

    @Test
    public void testResetMelds() {
        ArrayList<Card> testInitialHand = new ArrayList<>();
        /*
         * Here, I must instantiate an entire testDeck and individually move 4 specific cards from it into an arrayList
         * instead of simply constructing a few cards that I have chosen by hand, because for some reason the
         * constructor for the Card class is private...
         */
        for (Card card : testDeck) {
            if (card.getRankValue() == 3) {
                testInitialHand.add(card);
            }
        }
        testPlayerOne.receiveInitialHand(testInitialHand);
        testPlayerOne.reset();

        assertEquals(0, testPlayerOne.getMelds().size());
    }

    /**
     * A method that pulls the first instance of a card of a particular rank from the testDeck,
     * because the constructors for cards are private for some reason...
     * Can't be used to pull multiple separate instances of the same rank of card
     * Also can't be used to get cards of separate suits, since I am unable to access the suit of any Card Object
     *
     * @param rankValue Rank of the card to search for
     */
    private Card pullCard(int rankValue) {
        for (Card card : testDeck) {
            if (card.getRankValue() == rankValue) {
                return card;
            }
        }
        return null;
    }
}
