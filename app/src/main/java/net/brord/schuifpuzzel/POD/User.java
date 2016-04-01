package net.brord.schuifpuzzel.POD;

import android.location.Location;

import net.brord.schuifpuzzel.enums.Status;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Iv on 18-6-2015.
 */
public class User implements Serializable{
    private String userName;
    private Location userCoords;
    private Status roomStatus;

    public User(String userName, Location userCoords){
        this.userName = userName;
        this.userCoords = userCoords;
        this.roomStatus = Status.NO_ROOM;
    }
    public String getUserName(){
        return userName;
    }
    public Location getUserCoords(){
        return userCoords;
    }

    public Status getStatus(){
        return roomStatus;
    }

    public void setStatus(Status roomStatus) {
        this.roomStatus = roomStatus;
    }
}
