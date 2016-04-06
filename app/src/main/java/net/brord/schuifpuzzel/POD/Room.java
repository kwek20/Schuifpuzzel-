package net.brord.schuifpuzzel.POD;

import java.io.Serializable;

/**
 * Created by Brord on 4/1/2016.
 */
public class Room implements Serializable {

    private String roomId;
    private boolean ready = false;
    private String user1, user2;

    private boolean isUser1Active;

    public Room(String user1, String roomId) {
        this.roomId = roomId;
        this.user1 = user1;
    }

    public void setUser2(String user2){
        this.user2 = user2;
        ready = true;
    }

    public boolean isReady() {
        return ready;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isUser1Active() {
        return isUser1Active;
    }

    public boolean isUser2Active(){
        return !isUser1Active;
    }

}
