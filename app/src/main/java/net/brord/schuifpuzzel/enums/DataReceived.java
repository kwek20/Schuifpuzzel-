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
    OPPONENT_QUERIED_LOCATION(7);

    private int id;

    DataReceived(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
