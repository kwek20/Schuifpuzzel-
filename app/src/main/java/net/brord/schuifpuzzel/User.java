package net.brord.schuifpuzzel;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Iv on 18-6-2015.
 */
public class User implements Serializable{
    private String userName;
    private Location userCoords;
    private int userRoomNr;

    public User(String userName, Location userCoords){
        this.userName = userName;
        this.userCoords = userCoords;
        this.userRoomNr = -1;
    }
    public String getUserName(){
        return userName;
    }
    public Location getUserCoords(){
        return userCoords;
    }

    public int getUserRoomNr(){
        return userRoomNr;
    }

    public void setUserRoomNr(int userRoomNr) {
        this.userRoomNr = userRoomNr;
    }
}
