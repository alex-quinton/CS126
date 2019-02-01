public class Item {
    private String name;
    private Double damage;

    public Item(String name, double damage) {
        this.name = name;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public Double getDamage() {
        return damage;
    }
}
