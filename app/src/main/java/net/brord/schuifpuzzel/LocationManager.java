package net.brord.schuifpuzzel;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;

/**
 * Created by Brord on 4/1/2016.
 */
public class LocationManager {

    private final Context context;

    private android.location.LocationManager locationManager;
    private LocationListener locationListener;

    public LocationManager (Context c){
        this.context = c;
        startSearching();
    }

    private void startSearching() {
        locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationManagerListener();
        locationManager.requestLocationUpdates(android.location.LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void stopSearching(){
        locationManager.removeUpdates(locationListener);
        locationManager = null;
        locationListener = null;
    }

    public Location getLocation(){
        Location loc;

        if(locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER) != null) {
            loc = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
        } else if(locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER) != null) {
            loc = locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
        } else {
            Toast.makeText(context, "Turn on GPS please.", Toast.LENGTH_SHORT).show();
            loc = null;
        }

       return loc;
    }


    /* GEOFIRE CRAP
    GeoFire geoFire = new GeoFire(new Firebase(FireBaseCommunicator.URL));
        if(loc != null) {
            geoFire.setLocation(prefs.getString("UUID", ""), new GeoLocation(loc.getLatitude(), loc.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, FirebaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {
                        System.out.println("Location saved on server successfully!");
                    }
                }
            });
        }

        fireBaseCommunicator = new FireBaseCommunicator();
        Player p = new Player(dummyId, "DummyData", roomList);
        fireBaseCommunicator.saveAPlayer(dummyId, p);

        if(loc != null) {
            geoFire.setLocation(dummyId, new GeoLocation(loc.getLatitude(), loc.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, FirebaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {
                        System.out.println("Location saved on server successfully!");
                    }
                }
            });
        }
     */

    private class LocationManagerListener implements LocationListener{
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            //makeUseOfNewLocation(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    }
}
