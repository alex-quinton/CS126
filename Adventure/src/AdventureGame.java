import com.google.gson.Gson;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;


public class AdventureGame {
    private static final String BEGINNING_MESSAGE = "Your journey begins here";
    private static final String ENDING_MESSAGE = "You have reached your final destination";
    private static final int MAX_KEYWORD_LENGTH = 5; // maximum length of input command keyword
    private static Room currentRoom;
    private static final String DEFAULT_FILENAME = "SensFortress.json";

    public static void main(String[] args) {

        Scanner userInputReader = new Scanner(System.in);
        Layout layout = processURL();

        // Initialize variables to be used in main game loop
        layout.getPlayer().initializeItemsArrayList();
        layout.getPlayer().initializeMaxHealth();
        AdventureGame.currentRoom = layout.getRooms()[0];

        // Iterates through rooms and monsters, initializing each of the necessary variables
        for (Room r : layout.getRooms()) {
            r.initializeItemsArrayList();
            r.initializeMonsterArray(layout);

            if (r.getMonsters() != null) {
                for (Monster m : r.getMonsters()) {
                    m.initializeMaxHealth();
                }
            }
        }

        // Checks for beginning/ending room
        if (AdventureGame.currentRoom.getName().equals(layout.getStartingRoom())) {
            System.out.println(BEGINNING_MESSAGE);
        }

        if (AdventureGame.currentRoom.getName().equals(layout.getEndingRoom())) {
            System.out.println(ENDING_MESSAGE);
            System.exit(1); // 1 to show nothing went wrong
        }

        System.out.println(AdventureGame.currentRoom.getRoomInfoAsString());

    /*
     Main game loop starts here
     */
        while (true) {
            String input = userInputReader.nextLine();

            // Console output for invalid inputs
            if (input.length() == 0) {
                System.out.println("Input must contain characters");
                continue;
            }

            // Quit the game
            if (input.equalsIgnoreCase("exit") || input.equals("quit")) {
                System.exit(1); // 1 to show nothing went wrong
            }

            // Take items
            else if (input.length() >= MAX_KEYWORD_LENGTH &&
                    input.substring(0, MAX_KEYWORD_LENGTH).equalsIgnoreCase("take ")) {
                System.out.println(AdventureGame.currentRoom.takeItem(input, layout.getPlayer()));
            }

            // Drop items
            else if (input.length() >= MAX_KEYWORD_LENGTH &&
                    input.substring(0, MAX_KEYWORD_LENGTH).equalsIgnoreCase("drop ")) {
                System.out.println(AdventureGame.currentRoom.dropItem(input, layout.getPlayer()));
            }

            // List items
            else if (input.trim().equalsIgnoreCase("list")) {
                System.out.println(layout.getPlayer().listPlayerItems());
            }

            // List player's info
            else if (input.equalsIgnoreCase("playerinfo")) {
                System.out.println(layout.getPlayer().getPlayerInfoAsString());
            }

            // Duel a monster
            else if (input.length() >= MAX_KEYWORD_LENGTH &&
                    input.substring(0, MAX_KEYWORD_LENGTH).equalsIgnoreCase("duel ")) {

                // If room doesn't have specified monster, print "I can't duel..."
                // Otherwise initiate duel
                if (currentRoom.getMonsterOfName(input.substring(input.indexOf(' ') + 1)) == null) {
                    System.out.println("I can't duel " + input.substring(input.indexOf(' ') + 1));
                } else {
                    duelSequence(layout.getPlayer(), currentRoom.getMonsterOfName(input.substring(input.indexOf(' ') + 1)), layout);
                }
            }

            // Move in a direction
            else if (input.length() >= 2 &&
                    input.substring(0, 2).equalsIgnoreCase("go")) {
                System.out.println(changeCurrentRoom(input, AdventureGame.currentRoom, layout));
            } else {
                System.out.println("I don't understand '" + input + "'");
            }

        } // End of main game loop
    }

    /**
     * Handles moving from room to room
     *
     * @param input                 The input command from the player
     * @param parameterRoom         Room the player is moving from
     * @param layoutToSearchThrough Layout the player is within
     * @return Message to print after attempting to move
     */
    public static String changeCurrentRoom(String input, Room parameterRoom, Layout layoutToSearchThrough) {
        boolean directionIsValid = false;
        String directionToGo = input.substring(input.indexOf(' ') + 1);
        String output = "";

        for (Direction d : parameterRoom.getDirections()) {

            // If layout has a direction with the name specified by the player
            if (d.getDirectionName().toLowerCase().equalsIgnoreCase(directionToGo) && !directionIsValid) {

                if (parameterRoom.calcNumMonsters() == 0) {
                    // Changes current player position, prints relevant room info
                    AdventureGame.currentRoom = d.destinationRoom(layoutToSearchThrough);
                    parameterRoom = d.destinationRoom(layoutToSearchThrough);
                    output = parameterRoom.getRoomInfoAsString();

                    // Checks for beginning/ending room
                    if (AdventureGame.currentRoom.getName().equals(layoutToSearchThrough.getStartingRoom())) {
                        System.out.println(BEGINNING_MESSAGE);
                    }

                    if (AdventureGame.currentRoom.getName().equals(layoutToSearchThrough.getEndingRoom())) {
                        System.out.println(output);
                        System.out.println(ENDING_MESSAGE);
                        System.exit(1); //1 to show nothing went wrong
                    }

                    // If the room still has monsters
                } else {
                    output = "You cannot go anywhere until you defeat all the monsters!";
                }

                directionIsValid = true;
            }
        }

        if (!directionIsValid) {
            if (input.indexOf(' ') == -1) {
                output = "I don't understand 'go'";
            } else {
                output = "I can't go " + input.substring(input.indexOf(' ') + 1);
            }
        }

        return output;
    }

    private static boolean playerIsDueling = true;

    /**
     * Handles dueling sequence
     *
     * @param player  player that is dueling
     * @param monster monster being dueled with
     * @param layout  layout the duel takes place in
     */
    private static void duelSequence(Player player, Monster monster, Layout layout) {
        playerIsDueling = true;
        Scanner duelInput = new Scanner(System.in);

        System.out.println("You initiate a duel with the " + monster.getName());

        while (playerIsDueling) {
            String input = duelInput.nextLine();

            // Console output for invalid inputs
            if (input.length() == 0) {
                System.out.println("Input must contain characters");
                continue;
            }

            // Quit the game
            if (input.equalsIgnoreCase("exit") || input.equals("quit")) {
                System.exit(1); // 1 to show nothing went wrong
            }

            // List items as outside of duel
            else if (input.equalsIgnoreCase("list")) {
                System.out.println(layout.getPlayer().listPlayerItems());
            }

            // Print the status of the duel
            else if (input.equalsIgnoreCase("status")) {
                System.out.println(getHealthAsString(player, monster));
            }

            // Disengage the monster and end the duel
            else if (input.equalsIgnoreCase("disengage")) {
                playerIsDueling = false;
                System.out.println("You disengage with the " + monster.getName());
            }

            // List player's stats as outside of duel
            else if (input.equalsIgnoreCase("playerinfo")) {
                System.out.println(layout.getPlayer().getPlayerInfoAsString());
            }

            // Attack the monster with an item
            // 12 is length of the word "attack with"
            else if (input.length() >= 12 &&
                    input.substring(0, 12).equalsIgnoreCase("attack with ")) {

                // Get's item name from input, and retrieves the item object being used
                String itemName = input.substring(input.indexOf(' ', 8) + 1); // 8 is the index after the space
                Item itemToAttackWith = player.getItemOfName(itemName);

                System.out.println(attackWithItem(itemToAttackWith, player, monster, currentRoom));
            }

            // Attack the monster
            // 6 is length of the word "attack"
            else if (input.length() >= 6 &&
                    input.substring(0, 6).equalsIgnoreCase("attack")) {
                System.out.println(attackWithoutItem(player, monster, currentRoom));
            } else {
                System.out.println("I don't understand '" + input + "'");
            }
        }

        System.out.println(currentRoom.getRoomInfoAsString());
    } // End of duelSequence

    /**
     * Attacks a monster using a specified item
     * Might end game if player dies
     *
     * @param itemToAttackWith Item player is using to attack
     * @param player           player that's attacking, might receive experience
     * @param monster          monster being attacked, might turn into null
     * @return String output of damage values
     */
    public static String attackWithItem(Item itemToAttackWith, Player player, Monster monster, Room roomWithMonster) {
        String output = "";
        double damageMonsterTakes = player.getAttack() + itemToAttackWith.getDamage() - monster.getDefense();
        double damagePlayerTakes = monster.getAttack() - player.getDefense();

        if (damagePlayerTakes < 0){
            damagePlayerTakes = 0;
        }
        if (damageMonsterTakes < 0){
            damageMonsterTakes = 0;
        }

        output += monster.getName() + " takes " + damageMonsterTakes + " damage\n";
        output += player.getName() + " takes " + damagePlayerTakes + " damage\n";

        monster.takeDamage(damageMonsterTakes);
        player.takeDamage(damagePlayerTakes);

        if (monster.getHealth() <= 0.0) {
            // Eventually prints level up messages

            roomWithMonster.killMonster(monster, player);
            playerIsDueling = false;
        }

        if (player.getHealth() <= 0.0) {
            System.out.println("YOU DIED");
            System.exit(1);
        }

        return output;
    }

    /**
     * Attacks specified monster, player damage purely dependent on base stats
     * Might end game if player dies
     *
     * @param player  player that's attacking, might receive experience
     * @param monster monster being attacked, might turn into null
     * @return String output of damage values
     */
    public static String attackWithoutItem(Player player, Monster monster, Room roomWithMonster) {
        String output = "";
        double damageMonsterTakes = player.getAttack() - monster.getDefense();
        double damagePlayerTakes = monster.getAttack() - player.getDefense();

        output += monster.getName() + " takes " + damageMonsterTakes + " damage\n";
        output += player.getName() + " takes " + damagePlayerTakes + " damage\n";

        monster.takeDamage(damageMonsterTakes);
        player.takeDamage(damagePlayerTakes);

        if (monster.getHealth() <= 0.0) {
            // Eventually prints level up messages

            roomWithMonster.killMonster(monster, player);
            playerIsDueling = false;
        }

        if (player.getHealth() <= 0.0) {
            System.out.println(output + "YOU DIED");
            System.exit(1);
        }

        return output;
    }

    private static final int NUMBER_OF_HEALTH_BAR_CHARS = 20;

    /**
     * Returns a string representation of health percentages for given monster and player
     *
     * @param player Player whose health to show
     * @param monster Monster whose health to show
     * @return String output of health values and bars
     */
    public static String getHealthAsString(Player player, Monster monster) {
        StringBuilder output = new StringBuilder("");
        output.append("player has ");
        output.append(player.getHealth());
        output.append(" health");
        output.append("\n");
        output.append("monster has ");
        output.append(monster.getHealth());
        output.append(" health");
        output.append("\n");

        StringBuilder healthBars = new StringBuilder("");
        double playerHealthRatio = player.getHealth() / player.getMaxHealth();
        double monsterHealthRatio = monster.getHealth() / monster.getMaxHealth();
        int numPlayerBars = (int) (NUMBER_OF_HEALTH_BAR_CHARS * playerHealthRatio);
        int numMonsterBars = (int) (NUMBER_OF_HEALTH_BAR_CHARS * monsterHealthRatio);

        healthBars.append("Player:  ");

        for (int i = 0; i < NUMBER_OF_HEALTH_BAR_CHARS; i++) {
            if (i < numPlayerBars) {
                healthBars.append('#');
            } else {
                healthBars.append('-');
            }
        }

        healthBars.append("\nMonster: ");

        for (int i = 0; i < NUMBER_OF_HEALTH_BAR_CHARS; i++) {
            if (i < numMonsterBars) {
                healthBars.append('#');
            } else {
                healthBars.append('-');
            }
        }

        healthBars.append('\n');
        output.append(healthBars);

        return new String(output);
    }

    /**
     * Takes user input and turns it into an URL to read in order to create the Json file, and also the layout
     * @return The layour created from the Json URL
     */
    private static Layout processURL(){
        // Initializing variables to be used for reading the Json file
        boolean urlIsValid = true;
        boolean isUsingURL = false;
        StringBuilder jsonToParse = new StringBuilder("");
        String urlToUse = "";
        URL jsonURL;
        InputStream inStream;
        InputStreamReader inStreamReader;
        Scanner urlReader = new Scanner(System.in);

        do {
            // Prompt for and read input from the user
            isUsingURL = false;
            urlIsValid = true;
            System.out.println("enter \"default\" to play in the default world, or enter a URL to use a different one");
            String userInput = urlReader.nextLine().trim();

            if (userInput.equals("default")) {
                if (Data.getFileContentsAsString(DEFAULT_FILENAME) == null){
                    jsonToParse = null;
                } else {
                    jsonToParse = new StringBuilder(Data.getFileContentsAsString(DEFAULT_FILENAME));
                }
            } else {
                isUsingURL = true;
                urlToUse = userInput;
            }
            if (isUsingURL) {
                // The following code turns the online Json file into a String
                try {
                    jsonURL = new URL(urlToUse);
                    inStream = jsonURL.openStream();
                    inStreamReader = new InputStreamReader(inStream, Charset.forName("US-ASCII"));
                    Boolean isReading = true;

                    // Individually reads characters from the input stream and appends them to the Json string
                    while (isReading) {
                        int streamReturn = inStreamReader.read();
                        char charReturn;
                        if (streamReturn != -1) {
                            charReturn = (char) (streamReturn);
                            jsonToParse.append(charReturn);
                        } else {
                            isReading = false;
                        }
                    }
                } catch (IOException e) {
                    System.out.println(urlToUse + " isn't a valid filepath");
                    urlIsValid = false;
                }
            }
            // Prompt user again if the URL isn't valid
        } while (!urlIsValid);

        Gson gson = new Gson();
        return gson.fromJson(new String(jsonToParse), Layout.class);
    }
}
