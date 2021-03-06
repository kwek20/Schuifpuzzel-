package net.brord.schuifpuzzel;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;

/**
 * Created by Brord on 4/2/2015.
 */
public abstract class GridManager<T extends View> {

    private final ViewGroup view;
    private final int y, x;

    private View.OnClickListener listener;

    public GridManager(int x, int y, ViewGroup view){
        this.x = x;
        this.y = y;
        this.view = view;
    }

    public void setup(Context c){
        ViewGroup.MarginLayoutParams params = getParams();

        ViewGroup layout = null;
        for (int i=0; i<x*y; i++) {
            if (i % x == 0){
                layout = getLayout(c);
                view.addView(layout);
            }

            T v = newView(c);
            v.setLayoutParams(params);

            if (listener != null) v.setOnClickListener(listener);

            layout.addView(v);
        }
    }

    public java.util.List<T> getViews(){
        java.util.List<T> list = new java.util.LinkedList<T>();
        for (int x=0; x<getX(); x++){
            for (int y=0; y<getY(); y++){
                list.add(getView(x,y));
            }
        }
        return list;
    }

    public T getView(int x, int y){
        return (T) ((ViewGroup)view.getChildAt(x)).getChildAt(y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public abstract ViewGroup.MarginLayoutParams getParams();

    public abstract T newView(Context c);

    public abstract ViewGroup getLayout(Context c);

    /**
     * Created by Brord on 4/7/2016.
     */
    public static class GeoListener implements GeoQueryEventListener {

        private final OpponentScreen opponentScreen;

        public GeoListener(OpponentScreen opponentScreen) {
            this.opponentScreen = opponentScreen;
        }

        @Override
        public void onKeyEntered(String key, GeoLocation location) {
            Log.d("MAD", "key: " + key);
            opponentScreen.locationOpponentFound(key);
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
}
