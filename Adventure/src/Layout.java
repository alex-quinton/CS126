public class Layout {
    private String startingRoom;
    private String endingRoom;
    private Room[] rooms;
    private Player player;
    private Monster[] monsters;

    public String toString() {
        return "Starting room: " + startingRoom
                + ", Ending room: " + endingRoom
                + ", room array length: " + rooms.length;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public String getStartingRoom() {
        return startingRoom;
    }

    public String getEndingRoom() {
        return endingRoom;
    }

    public Player getPlayer() { return player; }

    public Monster[] getMonsters() { return monsters; }
}
