import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    private String name;
    private Item[] itemsArray;
    private double attack;
    private double defense;
    private double health;
    private double maxHealth;
    private int level;
    private double experience;
    private ArrayList<Item> items;

    /**
     * Turns itemsArray into an empty arraylist if it's null
     */
    public void initializeItemsArrayList() {
        items = new ArrayList<>(Arrays.asList(itemsArray));
    }

    public void initializeMaxHealth() {
        maxHealth = health;
    }

    public void takeDamage(double damage) {
        health -= damage;
    }

    /**
     * Returns items in player's possession as a string
     *
     * @return String containing player's items
     */
    public String listPlayerItems() {
        StringBuilder output = new StringBuilder("You currently have:\n");

        for (Item item : items) {
            output.append("-");
            output.append(item.getName());
            output.append("\n");
        }

        if (items.size() == 0) {
            output.append("nothing\n");
        }

        return new String(output);
    }

    /**
     * Returns player's stats
     *
     * @return String containing player's stats
     */
    public String getPlayerInfoAsString() {
        return "name: " + name + "\n" +
                "health: " + health + "\n" +
                "attack: " + attack + "\n" +
                "defense: " + defense + "\n";
    }

    /**
     * Returns item of specified name, null if not found
     *
     * @param itemName name of the item
     * @return Item of specified name
     */
    public Item getItemOfName(String itemName) {
        for (Item i : items) {
            if (i.getName().equalsIgnoreCase(itemName)) {
                return i;
            }
        }

        return null;
    }

    /**
     * Increases player's experience by specified amount, levels up player if enough experience is gained
     * Calls calculateNeededExperience and levelUp
     *
     * @param experienceToGain Experience to be granted to the player
     */
    public void gainExperience(double experienceToGain) {
        experience += experienceToGain;
        double requiredExperience = calculateNeededExperience(level + 1);

        while (experience >= requiredExperience) {
            levelUp();
            experience -= requiredExperience;
            requiredExperience = calculateNeededExperience(level + 1);
        }
    }

    private static final double LEVEL_TWO_EXPERIENCE = 25.0;
    private static final double LEVEL_THREE_EXPERIENCE = 50.0;

    private double calculateNeededExperience(double requiredLevel) {
        if (requiredLevel == 2) {
            return LEVEL_TWO_EXPERIENCE;
        }
        if (requiredLevel == 3) {
            return LEVEL_THREE_EXPERIENCE;
        }
        return (calculateNeededExperience(requiredLevel - 1)
                + calculateNeededExperience(requiredLevel - 2))
                * 1.1;
    }

    private final double ATTACK_DEFENSE_LEVEL_MULTIPLIER = 1.5;
    private final double HEALTH_LEVEL_MULTIPLIER = 1.3;

    private void levelUp() {
        level++;
        attack *= ATTACK_DEFENSE_LEVEL_MULTIPLIER;
        defense *= ATTACK_DEFENSE_LEVEL_MULTIPLIER;
        maxHealth *= HEALTH_LEVEL_MULTIPLIER;
        health = maxHealth;
        System.out.println("You gained a level!");
        System.out.println("Your attack is now: " + attack);
        System.out.println("Your defense is now: " + defense);
        System.out.println("Your health is now: " + health);
        System.out.println("Your health has been restored to full");
    }

    // Basic getter methods
    public String getName() {
        return name;
    }

    public Item[] getItemsArray() {
        return itemsArray;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public double getAttack() {
        return attack;
    }

    public double getDefense() {
        return defense;
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public int getLevel() {
        return level;
    }
}
