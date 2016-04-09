package net.brord.schuifpuzzel.interfaces;

import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;

import net.brord.schuifpuzzel.OpponentScreen;
import net.brord.schuifpuzzel.enums.DataReceived;

/**
 * Created by Brord on 4/7/2016.
 */
public class GeoListener implements GeoQueryEventListener {

    private final OpponentScreen opponentScreen;

    public GeoListener(OpponentScreen opponentScreen) {
        this.opponentScreen = opponentScreen;
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        opponentScreen.onDataReceived(key, DataReceived.OPPONENT_QUERIED_LOCATION);
    }

    @Override
    public void onKeyExited(String key) {

    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {

    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(FirebaseError error) {

    }
}
