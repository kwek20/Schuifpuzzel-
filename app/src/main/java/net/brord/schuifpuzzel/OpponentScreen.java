package net.brord.schuifpuzzel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.brord.schuifpuzzel.POD.Room;
import net.brord.schuifpuzzel.POD.User;
import net.brord.schuifpuzzel.enums.DataReceived;
import net.brord.schuifpuzzel.enums.Difficulty;
import net.brord.schuifpuzzel.firebase.FirebaseListener;
import net.brord.schuifpuzzel.firebase.FirebaseRoomCRUD;
import net.brord.schuifpuzzel.firebase.FirebaseUsersCRUD;

import java.lang.reflect.Field;

/**
 * Created by Brord on 6/18/2015.
 */
public class OpponentScreen extends ActionBarActivity implements FirebaseListener {

    private static final String LOADER_TAG = "Loader";

    private String username;
    private String opponentName;
    private User user;

    private static int message;
    private LoaderDialog loader;

    private FirebaseUsersCRUD crud;
    private FirebaseRoomCRUD roomCrud;

    private net.brord.schuifpuzzel.LocationManager locationManager;

    private Difficulty difficulty = null;
    private String image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_opponentscreen);

        crud = new FirebaseUsersCRUD(this);
        roomCrud = new FirebaseRoomCRUD(this);

        locationManager = new net.brord.schuifpuzzel.LocationManager(this);
    }

    public void findOpponent(View v){
        //username will be an opponentname in this case
        opponentName = ((TextView) findViewById(R.id.userName)).getText().toString();
        final EditText input = new EditText(this);
        new AlertDialog.Builder(OpponentScreen.this)
                .setTitle(R.string.find)
                .setMessage(getString(R.string.username))
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        crud.queryUserData(value.toString(), DataReceived.OPPONENT_QUERIED.getId(), OpponentScreen.this);
                        waitForNotification(R.string.searching);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    public void createGame(View v){
        Intent i = new Intent(this, Schuifpuzzel.class);
        startActivityForResult(i, 1);

        //create game
        //start intent
        //end intent
        //load game data
        //check if user exists
        //create room
        //wait for opponent
    }

    private User loadUser() {
        username = ((TextView) findViewById(R.id.userName)).getText().toString();
        return new User(username, getLocation());
    }

    public void doneLoading() {

        if (loader != null && loader.getShowsDialog()) {
            loader.dismiss();
        }
    }

    @Override
    public void onDataReceived(Object o, int ID) {
        if (ID == DataReceived.USER_LOADED.getId()){
            if (user != null && o != null){
                //opponent found
                Log.d("MAD", "Opponent found");
                startGame((String)o);
                doneLoading();
            }
        } else if (ID == DataReceived.USER_QUERIED.getId() && o != null){
            //hey user
            handleUserLoaded((boolean) o);

        } else if (ID == DataReceived.OPPONENT_QUERIED.getId() && o != null){

            //opponent exists
            handleOpponentFounded((User) o);
//            Log.d("MAD", "Opponent founded");
        } else if (ID == DataReceived.WAIT_FOR_OPPONENT.getId() && o != null){
            startGame((String)o);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                TextView group = (TextView) findViewById(R.id.userName);

                difficulty = (Difficulty) data.getSerializableExtra("difficulty");
                image = data.getStringExtra("image");

                //does user exist?
                crud.queryUserData(group.getText().toString(), DataReceived.USER_QUERIED.getId(), this);
                waitForNotification(R.string.checking);
            }
        }
    }


    private void handleOpponentFounded(User user) {
        if(user == null){
            Log.d("MAD", "Opponent not founded");
        } else {
            Log.d("MAD", "Opponent founded: " + user.getUserName());
            doneLoading();

            Room r = roomCrud.getRoom(user.getRoomID());

            new AlertDialog.Builder(OpponentScreen.this)
                    .setTitle(R.string.success)
                    .setMessage(getString(R.string.opponentavailable))
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    }).show();
            //crud.queryForOpponent(user, DataReceived.USER_LOADED.getId(), OpponentScreen.this);
        }
    }

    private void startGame(String opponentName) {

    }

    private void handleUserLoaded(boolean exists) {
        //user doesnt exist, add and wait
        if (!exists) {
            user = loadUser();
            crud.setUserInFirebase(user);
            doneLoading();
            Log.d("MAD", "User added");
            try {
                Field f = R.mipmap.class.getField(image);
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), f.getInt(null));
                Room r = roomCrud.createRoomInFirebase(user, difficulty, bmp);
                Log.d("MAD", "Room added");

                crud.assignRoomToUser(user, r);
                //does user exist?
                roomCrud.queryForOpponent(DataReceived.WAIT_FOR_OPPONENT.getId(), this);
                waitForNotification(R.string.waiting);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            //user exists
            new AlertDialog.Builder(OpponentScreen.this)
                    .setTitle(R.string.error)
                    .setMessage(getString(R.string.unavailable))
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            doneLoading();
                        }
                    }).show();
        }
    }

    @Override
    public void onDataCancelled(int ID) {

    }

    private Location getLocation() {
        return locationManager.getLocation();
    }

    public void waitForNotification(int message) {
        this.message = message;
        (loader = new LoaderDialog()).show(getFragmentManager(), LOADER_TAG);
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
