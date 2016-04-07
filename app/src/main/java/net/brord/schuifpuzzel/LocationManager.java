package net.brord.schuifpuzzel;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
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
        android.location.LocationManager service = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
                || service.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        } else {
            Criteria criteria = new Criteria();

            String provider = service.getBestProvider(criteria, false);
            Location location = service.getLastKnownLocation(provider);
            return location;
        }
        return null;
    }

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
