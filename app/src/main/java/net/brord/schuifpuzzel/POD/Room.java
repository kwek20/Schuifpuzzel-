package net.brord.schuifpuzzel.POD;

import java.io.Serializable;

/**
 * Created by Brord on 4/1/2016.
 */
public class Room implements Serializable {

    private int roomId;
    private int user1, user2;

    private boolean isUser1Active;

    public Room(int roomId, int user1, int user2) {
        this.roomId = roomId;
        this.user1 = user1;
        this.user2 = user2;
        isUser1Active = true;
    }

    public int getRoomId() {
        return roomId;
    }

    public boolean isUser1Active() {
        return isUser1Active;
    }

    public boolean isUser2Active(){
        return !isUser1Active;
    }

}
