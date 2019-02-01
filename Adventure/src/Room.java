import java.util.ArrayList;
import java.util.Arrays;

public class Room {
    private String name;
    private String description;
    private Direction[] directions;
    private Item[] itemsArray;
    private ArrayList<Item> items;
    private String[] monstersInRoom;
    private Monster[] monsters;

    public String toString() {
        String returnString = "name: " + name + ", description: " + description;

        if (directions != null) {
            returnString += ", # of directions: " + directions.length;
        }

        if (items != null) {
            returnString += ", # of items: " + items.size();
        }

        return returnString;
    }

    /**
     * Turns itemsArray into an empty arraylist if it's null
     */
    public void initializeItemsArrayList() {
        if (itemsArray == null) {
            items = new ArrayList<>();
        } else {
            items = new ArrayList<>(Arrays.asList(itemsArray));
        }
    }

    /**
     * Turns monsterArray into an empty arraylist if it's null
     */
    public void initializeMonsterArray(Layout layoutToSearchThrough){

        if (monstersInRoom == null){
            monsters = new Monster[0];
        } else {
            monsters = new Monster[monstersInRoom.length];


            int monsterArrayIndex = 0;
            for (String monsterName : monstersInRoom) {
                for (Monster monsterInLayout : layoutToSearchThrough.getMonsters()) {
                    if (monsterInLayout.getName().equalsIgnoreCase(monsterName)) {
                        monsters[monsterArrayIndex] = monsterInLayout;
                        monsterArrayIndex++;
                    }
                }
            }
        }
    }

    /**
     * Returns a string containing all the info about the room
     *
     * @return String containing info
     */
    public String getRoomInfoAsString() {
        StringBuilder output = new StringBuilder("");
        output.append(description);
        output.append("\n");
        output.append("\nThis room contains:\n");
        int itemsPrinted = 0;
        int monstersPrinted = 0;

        // Appends item list
        if (items != null) {
            for (Item item : items) {
                output.append("-");
                output.append(item.getName());
                output.append("\n");
                itemsPrinted++;
            }
        }

        if (itemsPrinted == 0) {
            output.append("nothing\n");
        }
        output.append("\n");

        // Appends monster list
        if (monsters != null) {
            output.append("Monsters in this room:\n");

            for (Monster monster : monsters) {
                if (monster != null) {
                    output.append("-");
                    output.append(monster.getName());
                    output.append("\n");
                    monstersPrinted++;
                }
            }
        }

        // Prints directions if all monsters have been defeated
        if (monstersPrinted == 0 || monsters == null) {
            output.append("No monsters in the room!\n");
            output.append("\n");
            output.append("From here, you can go: \n");

            for (Direction dir : directions) {
                output.append("-");
                output.append(dir.getDirectionName());
                output.append("\n");
            }
        }

        return new String(output);
    }

    /**
     * Takes an item from the room's array and moves it into the specified arrayList
     *
     * @param input          input from player
     * @param playerToGiveTo player to give taken item to
     */
    public String takeItem(String input, Player playerToGiveTo) {
        boolean roomHasItem = false;
        Item itemToTake = null;
        String itemNameToTake = input.substring(input.indexOf(' ') + 1);
        String output = "";

        if (items != null) {
            for (Item item : items) {
                if (item.getName().equalsIgnoreCase(itemNameToTake)) {
                    roomHasItem = true;
                    itemToTake = item;
                }
            }
        }

        if (roomHasItem) {
            playerToGiveTo.getItems().add(itemToTake);
            items.remove(itemToTake);
            output = "You took the " + itemToTake.getName();
        }

        if (input.indexOf(' ') == -1) {
            output = "You must specify an item to take";
        } else if (!roomHasItem) {
            output = "I can't take " + input.substring(input.indexOf(' ') + 1);
        }

        return output;
    }

    /**
     * Takes an item from the specified player's inventory and moves it into the specified room
     *
     * @param input            input from player
     * @param playerToDropFrom player to remove item from
     */
    public String dropItem(String input, Player playerToDropFrom) {
        String itemNameToDrop = input.substring(input.indexOf(' ') + 1);
        boolean playerHasItem = false;
        Item itemToDrop = null;
        String output = "";

        for (Item item : playerToDropFrom.getItems()) {
            if (item.getName().equals(itemNameToDrop)) {
                playerHasItem = true;
                itemToDrop = item;
            }
        }

        if (playerHasItem) {
            items.add(itemToDrop);
            playerToDropFrom.getItems().remove(itemToDrop);
            output = "You dropped the " + itemNameToDrop;
        }

        if (input.indexOf(' ') == -1) {
            output = "You must specify an item to drop";
        } else if (!playerHasItem) {
            output = "I can't drop " + input.substring(input.indexOf(' ') + 1);
        }

        return output;
    }

    private static final double EXPERIENCE_MULTIPLIER = 20.0;

    /**
     * Makes the specified monster null, calls Player.gainExperience
     *
     * @param monsterToKill Monster that will die
     * @param playerToGrantExperience Experience to give to the player
     */
    public void killMonster(Monster monsterToKill, Player playerToGrantExperience) {
        for (int i = 0; i < monsters.length; i++) {
            if (monsterToKill == monsters[i]) {
                monsters[i] = null;
                break;
            }
        }

        double experienceToGain = ((monsterToKill.getAttack() + monsterToKill.getDefense()) / 2)
                + monsterToKill.getMaxHealth() * 20;

        playerToGrantExperience.gainExperience(experienceToGain);
        System.out.println("You killed the " + monsterToKill.getName() + "!");
        System.out.println("You gained " + experienceToGain + " experience!");
    }

    /*
     basic getter methods
      */
    public String getName() {
        return name;
    }

    public int calcNumMonsters() {
        int sum = 0;

        if (monsters != null) {
            for (Monster monster : monsters) {
                if (monster != null) {
                    sum++;
                }
            }
        }

        return sum;
    }

    public Monster getMonsterOfName(String monsterName) {
        if (monsters == null) {
            return null;
        }

        for (Monster monster : monsters) {
            if (monster != null && monster.getName().equalsIgnoreCase(monsterName)) {
                return monster;
            }
        }

        return null;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public Item[] getItemsArray() {
        return itemsArray;
    }

    public Direction[] getDirections() {
        return directions;
    }

    public Monster[] getMonsters() {
        return monsters;
    }
}
