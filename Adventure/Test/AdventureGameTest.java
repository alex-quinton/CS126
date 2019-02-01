import com.example.Adventure;
import com.google.gson.Gson;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class AdventureGameTest {
    private String testJson = "{\n" +
            "  \"startingRoom\": \"Corridor Entry\",\n" +
            "  \"endingRoom\": \"Corridor Exit\",\n" +
            "  \"player\": {\n" +
            "    \"name\": \"Alex\",\n" +
            "    \"itemsArray\": [],\n" +
            "    \"attack\": 1,\n" +
            "    \"defense\": 1,\n" +
            "    \"health\": 10,\n" +
            "    \"level\": 1\n" +
            "  },\n" +
            "  \"rooms\": [\n" +
            "    {\n" +
            "      \"name\": \"Corridor Entry\",\n" +
            "      \"description\": \"You find yourself at the end of a long corridor\",\n" +
            "      \"directions\": [\n" +
            "        {\n" +
            "          \"directionName\": \"north\",\n" +
            "          \"room\": \"Corridor Midsection\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Corridor Midsection\",\n" +
            "      \"description\": \"You find yourself at the midsection of a long corridor\",\n" +
            "      \"itemsArray\": [\n" +
            "        {\n" +
            "          \"name\": \"sword\",\n" +
            "          \"damage\": 1\n" +
            "        }\n" +
            "      ],\n" +
            "      \"directions\": [\n" +
            "        {\n" +
            "          \"directionName\": \"north\",\n" +
            "          \"room\": \"Atrium\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"directionName\": \"south\",\n" +
            "          \"room\": \"Corridor Entry\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"monstersInRoom\": [\n" +
            "        \"skeleton\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Atrium\",\n" +
            "      \"description\": \"You find yourself at the midsection of a long corridor\",\n" +
            "      \"itemsArray\": [\n" +
            "        {\n" +
            "          \"name\": \"coin\",\n" +
            "          \"damage\": 0\n" +
            "        }\n" +
            "      ],\n" +
            "      \"directions\": [\n" +
            "        {\n" +
            "          \"directionName\": \"north\",\n" +
            "          \"room\": \"Corridor Exit\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"directionName\": \"south\",\n" +
            "          \"room\": \"Corridor Midsection\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"monstersInRoom\": [\n" +
            "        \"skeleton\",\n" +
            "        \"knight\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Corridor Exit\",\n" +
            "      \"description\": \"You find yourself at the exit of a long corridor\",\n" +
            "      \"directions\": [\n" +
            "        {\n" +
            "          \"directionName\": \"south\",\n" +
            "          \"room\": \"Atrium\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"monsters\":[\n" +
            "    {\n" +
            "      \"name\": \"skeleton\",\n" +
            "      \"health\": 3,\n" +
            "      \"attack\": 2,\n" +
            "      \"defense\": 0\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"knight\",\n" +
            "      \"health\": 3,\n" +
            "      \"attack\": 2,\n" +
            "      \"defense\": 2\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    private Gson gson = new Gson();
    private Layout testLayout = gson.fromJson(testJson, Layout.class);
    private Room testRoom = testLayout.getRooms()[1];
    private Player testPlayer = testLayout.getPlayer();

    @Test
    public void testJsonFormat() {
        assertEquals(4, testLayout.getRooms().length);
    }

    @Test
    public void testRoomDescription() {
        testLayout.getPlayer().initializeItemsArrayList();
        testRoom.initializeItemsArrayList();
        testRoom.initializeMonsterArray(testLayout);
        assertEquals("You find yourself at the midsection of a long corridor\n" +
                        "\n" +
                        "This room contains:\n" +
                        "-sword\n" +
                        "\n" +
                        "Monsters in this room:\n" +
                        "-skeleton" +
                        "\n",
                testRoom.getRoomInfoAsString());
    }

    @Test
    public void testPlayerInfo() {
        assertEquals("name: Alex\n" +
                        "health: 10.0\n" +
                        "attack: 1.0\n" +
                        "defense: 1.0\n",
                testPlayer.getPlayerInfoAsString());
    }

    /*
    Tests for rooms with item array
     */
    @Test
    public void testTakeItem() {
        testLayout.getPlayer().initializeItemsArrayList();
        testRoom.initializeItemsArrayList();

        assertEquals("You took the sword", testRoom.takeItem("take sword", testPlayer));
    }

    @Test
    public void testTakeItemList() {
        testLayout.getPlayer().initializeItemsArrayList();
        testRoom.initializeItemsArrayList();

        testRoom.takeItem("take sword", testPlayer);
        assertEquals(new Item("sword", 1).getName(), testPlayer.getItems().get(0).getName());
    }

    @Test
    public void testTakeItemRoomDescription() {
        testLayout.getPlayer().initializeItemsArrayList();
        testRoom.initializeItemsArrayList();
        testRoom.initializeMonsterArray(testLayout);

        testRoom.takeItem("take sword", testPlayer);
        assertEquals("You find yourself at the midsection of a long corridor\n" +
                        "\n" +
                        "This room contains:\n" +
                        "nothing\n" +
                        "\n" +
                        "Monsters in this room:\n" +
                        "-skeleton" +
                        "\n",
                testRoom.getRoomInfoAsString());
    }

    // Tests for taking items
    @Test
    public void testTakeInvalidItem() {
        assertEquals("I can't take Invalid Object", testRoom.takeItem("take Invalid Object", testPlayer));
    }

    @Test
    public void testTakeNothing() {
        assertEquals("You must specify an item to take", testRoom.takeItem("take", testPlayer));
    }

    @Test
    public void testListEmptyInventory() {
        testLayout.getPlayer().initializeItemsArrayList();
        testRoom.initializeItemsArrayList();

        assertEquals("You currently have:\n" +
                        "nothing\n",
                testPlayer.listPlayerItems());
    }

    @Test
    public void testListInventory() {
        testLayout.getPlayer().initializeItemsArrayList();
        testRoom.initializeItemsArrayList();

        testRoom.takeItem("take sword", testPlayer);
        assertEquals("You currently have:\n" +
                        "-sword\n",
                testPlayer.listPlayerItems());
    }

    // Tests for dropping items
    @Test
    public void testDropItem() {
        testLayout.getPlayer().initializeItemsArrayList();
        testRoom.initializeItemsArrayList();
        testRoom.takeItem("take sword", testPlayer);
        assertEquals("You dropped the sword", testRoom.dropItem("drop sword", testPlayer));
    }

    @Test
    public void testDropItemList() {
        testLayout.getPlayer().initializeItemsArrayList();
        testRoom.initializeItemsArrayList();

        assertEquals("You currently have:\n" +
                        "nothing\n",
                testPlayer.listPlayerItems());
    }

    @Test
    public void testDropItemRoomDescription() {
        testLayout.getPlayer().initializeItemsArrayList();
        testRoom.initializeItemsArrayList();
        testRoom.initializeMonsterArray(testLayout);

        assertEquals("You find yourself at the midsection of a long corridor\n" +
                        "\n" +
                        "This room contains:\n" +
                        "-sword\n" +
                        "\n" +
                        "Monsters in this room:\n" +
                        "-skeleton" +
                        "\n",
                testRoom.getRoomInfoAsString());
    }

    @Test
    public void testDropInvalidItem() {
        testLayout.getPlayer().initializeItemsArrayList();
        testRoom.initializeItemsArrayList();

        assertEquals("I can't drop Invalid Object",
                testRoom.dropItem("take Invalid Object", testPlayer));
    }

    @Test
    public void testDropNothing() {
        testLayout.getPlayer().initializeItemsArrayList();
        testRoom.initializeItemsArrayList();

        assertEquals("You must specify an item to drop", testRoom.dropItem("drop", testPlayer));
    }

    @Test
    public void testChangeRoomMonsters() {
        testLayout = gson.fromJson(testJson, Layout.class);
        testRoom.initializeMonsterArray(testLayout);

        assertEquals("You cannot go anywhere until you defeat all the monsters!",
                AdventureGame.changeCurrentRoom("go south", testRoom, testLayout));
    }

    @Test
    public void testChangeRoom() {
        testLayout = gson.fromJson(testJson, Layout.class);
        testRoom.initializeMonsterArray(testLayout);
        testRoom.killMonster(testRoom.getMonsters()[0], testPlayer);

        assertEquals("You find yourself at the end of a long corridor\n" +
                        "\n" +
                        "This room contains:\n" +
                        "nothing\n" +
                        "\n" +
                        "No monsters in the room!\n" +
                        "\n" +
                        "From here, you can go: \n" +
                        "-north\n",
                AdventureGame.changeCurrentRoom("go south", testRoom, testLayout));
    }

    @Test
    public void testChangeInvalidRoom() {
        assertEquals("I can't go down",
                AdventureGame.changeCurrentRoom("go down", testRoom, testLayout));
    }

    /*
    Tests for room without item array
     */
    private Room testRoomWithNoItems = testLayout.getRooms()[0];

    @Test
    public void testRoomDescriptionEmptyRoom() {
        testRoom.initializeMonsterArray(testLayout);

        assertEquals("You find yourself at the end of a long corridor\n" +
                        "\n" +
                        "This room contains:\n" +
                        "nothing\n" +
                        "\n" +
                        "No monsters in the room!\n" +
                        "\n" +
                        "From here, you can go: \n" +
                        "-north\n",
                testRoomWithNoItems.getRoomInfoAsString());
    }

    @Test
    public void testTakeItemsEmptyRoom() {
        assertEquals("I can't take item", testRoomWithNoItems.takeItem("take item", testPlayer));
    }

    @Test
    public void testDropItemsEmptyRoomList() {
        testLayout.getPlayer().initializeItemsArrayList();
        testRoomWithNoItems.initializeItemsArrayList();

        testPlayer.getItems().add(new Item("sword", 1));
        testRoomWithNoItems.dropItem("drop sword", testPlayer);
        assertEquals("You currently have:\n" +
                        "nothing\n",
                testPlayer.listPlayerItems());
    }

    @Test
    public void testDropItemsEmptyRoomInfo() {
        testLayout.getPlayer().initializeItemsArrayList();
        testRoomWithNoItems.initializeItemsArrayList();
        testRoom.initializeMonsterArray(testLayout);

        testPlayer.getItems().add(new Item("sword", 1));
        testRoomWithNoItems.dropItem("drop sword", testPlayer);
        assertEquals("You find yourself at the end of a long corridor\n" +
                "\n" +
                "This room contains:\n" +
                "-sword\n" +
                "\n" +
                "No monsters in the room!\n" +
                "\n" +
                "From here, you can go: \n" +
                "-north\n", testRoomWithNoItems.getRoomInfoAsString());
    }

    /*
    Duel tests
     */

    private Monster testMonster = new Monster("testmonster", 10, 1, 1);

    @Test
    public void testStatus() {
        testMonster.initializeMaxHealth();
        testMonster.takeDamage(3.0);
        assertEquals("player has 10.0 health\n" +
                        "monster has 7.0 health\n" +
                        "Player:  ####################\n" +
                        "Monster: ##############------\n"
                , AdventureGame.getHealthAsString(testPlayer, testMonster));
    }

    @Test
    public void testAttackWithItem() {
        testRoom.initializeMonsterArray(testLayout);
        testPlayer.initializeMaxHealth();
        testMonster.initializeMaxHealth();
        assertEquals("testmonster takes 10.0 damage\n" +
                        "Alex takes 0.0 damage\n",
                AdventureGame.attackWithItem(new Item("testweapon", 10.0), testPlayer, testMonster, testRoom));
    }

    @Test
    public void testAttackWithoutItem() {
        assertEquals("testmonster takes 0.0 damage\n" +
                        "Alex takes 0.0 damage\n",
                AdventureGame.attackWithoutItem(testPlayer, testMonster, testRoom));
    }
}
