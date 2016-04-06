package net.brord.schuifpuzzel.POD;

import net.brord.schuifpuzzel.enums.Difficulty;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Brord on 4/1/2016.
 */
public class Room implements Serializable {

     String roomId, image;
     boolean ready = false;
     String user1, user2;

     Difficulty difficulty;

     boolean isUser1Active;

    public Room(String user1, String roomId, String image, Difficulty difficulty) {
        this.roomId = roomId;
        this.user1 = user1;
        this.user2 = "";
        this.image = image;
        this.difficulty = difficulty;
        this.isUser1Active = true;
    }

    public  Room(){
        //firebase serialization
    }

    public void setUser2(String user2){
        this.user2 = user2;
        ready = true;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getImage() {
        return image;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }
}

