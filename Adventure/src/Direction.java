public class Direction {
    private String directionName;
    private String room;

    public String getDirectionName() {
        return directionName;
    }

    public String getRoom() {
        return room;
    }

    /*
    Searches through the specified Layout for the room that this direction points to
    Returns null if room isn't found
     */
    public Room destinationRoom(Layout layoutToSearchThrough) {
        for (Room r : layoutToSearchThrough.getRooms()) {
            if (r.getName().equals(this.room)) {
                return r;
            }
        }
        return null;
    }
}
