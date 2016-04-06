package net.brord.schuifpuzzel.POD;

import android.location.Location;

import net.brord.schuifpuzzel.enums.Status;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Iv on 18-6-2015.
 */
public class User implements Serializable{

    String roomID;
    String userName;
    Status roomStatus;

    public User(String userName){
        this.userName = userName;
        this.roomStatus = Status.NO_ROOM;
    }

    public User(){

    }


    public Status getRoomStatus() {
        return roomStatus;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getUserName() {
        return userName;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
}
