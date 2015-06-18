package net.brord.schuifpuzzel;

import java.util.ArrayList;

/**
 * Created by Iv on 18-6-2015.
 */
public class User {
    private String userName;
    private String userCoords;
    private int userRoomNr;

    public User(String userName, String userCoords, int userRoomNr){
        this.userName = userName;
        this.userCoords = userCoords;
        this.userRoomNr = userRoomNr;
    }
    public String getUserName(){
        return userName;
    }
    public String getUserCoords(){
        return userCoords;
    }
    public int getUserRoomNr(){
        return userRoomNr;
    }
}
