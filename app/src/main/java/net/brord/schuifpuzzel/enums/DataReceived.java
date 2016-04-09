package net.brord.schuifpuzzel.enums;

/**
 * Created by Brord on 4/1/2016.
 */
public enum DataReceived {
    USER_LOADED (1),
    USER_QUERIED (2),
    OPPONENT_QUERIED (3),
    WAIT_FOR_OPPONENT(5),
    DRAW (4),
    ROOM_CREATED(6),
    OPPONENT_QUERIED_LOCATION(7),
    OPPONENT_END_TURN(8),
    WAIT_FOR_TILE_DATA(9),
    USER_LEFT(10),
    USER_HAS_ROOM(11);

    private int id;

    DataReceived(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
