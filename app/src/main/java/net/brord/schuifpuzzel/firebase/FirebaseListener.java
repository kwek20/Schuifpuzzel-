package net.brord.schuifpuzzel.firebase;

import net.brord.schuifpuzzel.enums.DataReceived;

/**
 * Created by Brord on 6/19/2015.
 */
public interface FirebaseListener {

    void onDataReceived(Object o, DataReceived ID);

    void onDataCancelled(DataReceived ID);
}
