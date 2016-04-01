package net.brord.schuifpuzzel.enums;

/**
 * Created by Brord on 4/1/2016.
 */
public enum DataReceived {
    USER_LOADED (1),
    USER_QUERIED (2),
    OPPONENT_QUERIED (3),
    DRAW (4);

    private int id;

    DataReceived(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
