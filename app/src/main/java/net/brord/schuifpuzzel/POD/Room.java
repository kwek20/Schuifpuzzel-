package net.brord.schuifpuzzel.POD;

import net.brord.schuifpuzzel.enums.Difficulty;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Brord on 4/1/2016.
 */
public class Room implements Serializable {

    private String roomId, image;
    private boolean ready = false;
    private String user1, user2;

    private Difficulty difficulty;

    private boolean isUser1Active;

    public Room(String user1, String roomId, String image, Difficulty difficulty) {
        this.roomId = roomId;
        this.user1 = user1;

        this.image = image;
        this.difficulty = difficulty;
    }

    public void setUser2(String user2){
        this.user2 = user2;
        ready = true;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public boolean isReady() {
        return ready;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getImage() {
        return image;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public boolean isUser1Active() {
        return isUser1Active;
    }

    public boolean isUser2Active(){
        return !isUser1Active;
    }


}
