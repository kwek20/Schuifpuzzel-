package net.brord.schuifpuzzel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Iv on 18-6-2015.
 */
public class User implements Serializable{
    private String userName;
    private ArrayList<String> userCoords;
    private int userRoomNr;

    public User(String userName, ArrayList<String> userCoords, int userRoomNr){
        this.userName = userName;
        this.userCoords = userCoords;
        this.userRoomNr = userRoomNr;
    }
    public String getUserName(){
        return userName;
    }
    public ArrayList<String> getUserCoords(){
        return userCoords;
    }
    public int getUserRoomNr(){
        return userRoomNr;
    }


}
