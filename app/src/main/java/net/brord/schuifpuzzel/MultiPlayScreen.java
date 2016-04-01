package net.brord.schuifpuzzel;

import net.brord.schuifpuzzel.POD.Room;
import net.brord.schuifpuzzel.firebase.FirebaseListener;

/**
 * Created by Brord on 4/1/2016.
 */
public class MultiPlayScreen extends PlayScreen implements FirebaseListener {

    private final Room room;

    public MultiPlayScreen(Room room){
        this.room = room;
    }

    @Override
    public void onDataReceived(Object o, int ID) {

    }

    @Override
    public void onDataCancelled(int ID) {

    }
}
