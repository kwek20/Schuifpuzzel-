package net.brord.schuifpuzzel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;

import net.brord.schuifpuzzel.POD.Room;
import net.brord.schuifpuzzel.POD.User;
import net.brord.schuifpuzzel.enums.DataReceived;
import net.brord.schuifpuzzel.enums.Difficulty;
import net.brord.schuifpuzzel.enums.Status;
import net.brord.schuifpuzzel.firebase.FirebaseListener;
import net.brord.schuifpuzzel.firebase.FirebaseRef;
import net.brord.schuifpuzzel.firebase.FirebaseRoomCRUD;
import net.brord.schuifpuzzel.firebase.FirebaseUsersCRUD;

import java.lang.reflect.Field;

/**
 * Created by Brord on 6/18/2015.
 */
public class OpponentScreen extends ActionBarActivity implements FirebaseListener, LocationUpdater {

    //loader dialog data
    private static int message;
    private static final String LOADER_TAG = "Loader";
    private LoaderDialog loader;

    private User user;

    //Firebase connection handlers
    private FirebaseUsersCRUD crud;
    private FirebaseRoomCRUD roomCrud;

    //location management
    private GeoFire geoFire;
    private boolean locationSet = false;
    private net.brord.schuifpuzzel.LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_opponentscreen);

        crud = new FirebaseUsersCRUD(this);
        roomCrud = new FirebaseRoomCRUD(this);
        geoFire = new GeoFire(FirebaseRef.getFirebaseRef().child("locations"));

        user = (User) getIntent().getSerializableExtra("user");
        locationManager = new net.brord.schuifpuzzel.LocationManager(this, this);
    }

    public void setGeoFireLocation(Location loc){
        if(loc != null) {
            geoFire.setLocation(user.getUserName(), new GeoLocation(loc.getLatitude(), loc.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, FirebaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {
                        System.out.println("Location saved on server successfully!");
                        locationSet = true;
                        setLocationSearchEnabled(true);
                    }
                }
            });
        }
    }

    private void setLocationSearchEnabled(boolean enabled){
        Button searchLocButton = (Button) this.findViewById(R.id.button5);
        searchLocButton.setEnabled(enabled);
    }

    /**
     * Find opponent by username<br/>
     * Button click handler
     * @param v
     */
    public void findOpponent(View v){
        final EditText input = new EditText(this);
        new AlertDialog.Builder(OpponentScreen.this)
                .setTitle(R.string.findByName)
                .setMessage(getString(R.string.username))
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        crud.queryUserData(value.toString(), DataReceived.OPPONENT_QUERIED, OpponentScreen.this);
                        waitForNotification(R.string.searching);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    /**
     * find opponent by geoloc<br/>
     * Button click handler
     * @param v
     */
    public void findOpponentLoc(View v){
        Location loc = locationManager.getLocation();
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(loc.getLatitude(), loc.getLongitude()), 0.6);
        geoQuery.addGeoQueryEventListener(new GeoListener(this));

        waitForNotification(R.string.searchByLoc);
    }

    /**
     * Create game<br/>
     * button click handler
     * @param v
     */
    public void createGame(View v){
        Intent i = new Intent(this, Schuifpuzzel.class);
        startActivityForResult(i, 1);
    }

    @Override
    public void onDataReceived(Object o, DataReceived ID) {
        if (ID == DataReceived.OPPONENT_QUERIED && o != null) {
            //opponent exists, we join THEIR game

            handleOpponentFound((User) o);
        } else if (ID == DataReceived.OPPONENT_QUERIED_LOCATION && o != null){
            crud.queryUserData((String) o, DataReceived.OPPONENT_QUERIED, this);
        } else if (ID == DataReceived.WAIT_FOR_OPPONENT && o != null){
            //we found an opponent for OUR game
            doneLoading();
            startGame((Room)o);
        }
    }

    @Override
    public void onDataCancelled(DataReceived ID) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                 makeRoom((Difficulty) data.getSerializableExtra("difficulty"), data.getStringExtra("image"));
            }
        }
    }

    private void handleOpponentFound(User user) {
        if(user == null){
            Log.d("MAD", "Opponent not found");
        } else {
            Log.d("MAD", "Opponent found: " + user.getUserName());
            doneLoading();
            if (user.getRoomStatus().equals(Status.NO_ROOM)){
                //no room
                showAlert(R.string.error, getString(R.string.opponent_no_room));
            } else if (user.getRoomStatus().equals(Status.SEARCHING)){
                joinRoom(roomCrud.getRoom(user.getRoomID()));
            } else if (user.getRoomStatus().equals(Status.STARTED)){
                //playing
                showAlert(R.string.error, getString(R.string.opponent_already_playing));
            } else {
                showAlert(R.string.error, getString(R.string.error));
            }
        }
    }

    private void startGame(Room room) {
        Intent i = new Intent(this, MultiPlayScreen.class);
        startActivity(i);
    }

    private void joinRoom(Room r){
        showAlert(R.string.success, getString(R.string.opponentavailable));
        roomCrud.setOpponentinRoom(user.getUserName(),r.getRoomId());
        startGame(r);
    }

    private void makeRoom(Difficulty difficulty, String image) {
        try {
            Field f = R.mipmap.class.getField(image);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), f.getInt(null));
            Room r = roomCrud.createRoomInFirebase(user, difficulty, bmp);
            Log.d("MAD", "Room added");

            crud.assignRoomToUser(user, r);
            //does user exist?
            roomCrud.queryForOpponent(r, DataReceived.WAIT_FOR_OPPONENT, this);
            waitForNotification(R.string.waiting);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /*=================
        UTIL METHODS
     =================*/

    /**
     *
     * @param title
     * @param message
     */
    private void showAlert(int title, String message){
        new AlertDialog.Builder(OpponentScreen.this)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                }).show();
    }

    /**
     * Start a loaddialog with the message.<br/>
     * Stop using @fun
     *
     * @param message
     */
    public void waitForNotification(int message) {
        this.message = message;
        (loader = new LoaderDialog()).show(getFragmentManager(), LOADER_TAG);
    }

    /**
     * Stop the loader, if it was loading
     */
    public void doneLoading() {

        if (loader != null && loader.getShowsDialog()) {
            loader.dismiss();
        }
    }

    public static class LoaderDialog extends DialogFragment {
        public LoaderDialog(){}

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {

            ProgressDialog _dialog = new ProgressDialog(getActivity());
            this.setStyle(STYLE_NO_TITLE, getTheme()); // You can use styles or inflate a view
            _dialog.setMessage(getString(message)); // set your messages if not inflated from XML
            _dialog.setCancelable(true);
            _dialog.setCanceledOnTouchOutside(true);
            return _dialog;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
        }
    }
}
