public class Monster {
    private String name;
    private double health;
    private double maxHealth;
    private double attack;
    private double defense;

    public Monster(String name, int health, int attack, int defense) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
    }

    public void initializeMaxHealth() {
        maxHealth = health;
    }

    public void takeDamage(double damage) {
        health -= damage;
    }

    // Basic getters
    public String getName() {
        return name;
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getAttack() {
        return attack;
    }

    public double getDefense() {
        return defense;
    }
}
