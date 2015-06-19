package net.brord.schuifpuzzel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.brord.schuifpuzzel.firebase.FirebaseListener;
import net.brord.schuifpuzzel.firebase.FirebaseUsersCRUD;

/**
 * Created by Brord on 6/18/2015.
 */
public class OpponentScreen extends ActionBarActivity implements FirebaseListener {

    private static final String LOADER_TAG = "Loader";
    private static final int USER_LOADED = 1;
    private static final int USER_QUERIED = 2;
    private static final int OPPONENT_QUERIED = 3;

    private String username;
    private User user;

    private static int message;
    private LoaderDialog loader;
    private FirebaseUsersCRUD crud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_opponentscreen);
        crud = new FirebaseUsersCRUD(this);
    }

    public void findOpponent(View v){
        final EditText input = new EditText(this);
        new AlertDialog.Builder(OpponentScreen.this)
                .setTitle(R.string.find)
                .setMessage(getString(R.string.username))
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        crud.queryUserData(value.toString(), OPPONENT_QUERIED, OpponentScreen.this);
                        waitForNotification(R.string.searching);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    public void createGame(View v){
        TextView group = (TextView) findViewById(R.id.userName);
        crud.queryUserData(group.getText().toString(), USER_QUERIED, this);
        waitForNotification(R.string.checking);
    }

    private User loadUser() {
        username = ((TextView) findViewById(R.id.userName)).getText().toString();
        return new User(username, getLocation());
    }

    private Location getLocation() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else {
            Criteria criteria = new Criteria();
            String provider = service.getBestProvider(criteria, false);
            Location location = service.getLastKnownLocation(provider);
            return location;
        }
        return null;
    }

    public void waitForNotification(int message) {
        this.message = message;
        (loader = new LoaderDialog()).show(getFragmentManager(), LOADER_TAG);
    }

    public void doneLoading() {

        if (loader != null && loader.getShowsDialog()) {
            loader.dismiss();
        }
    }

    @Override
    public void onDataReceived(Object o, int ID) {
        if (ID == USER_LOADED){
            if (user != null && o != null){
                //opponent found
                startGame(o);
                doneLoading();
            }
        } else if (ID == USER_QUERIED && o != null){
            handleUserLoaded((boolean) o);
        } else if (ID == OPPONENT_QUERIED && o != null){
            //opponent exists
            
        }
    }

    private void startGame(Object o) {
    }

    private void handleUserLoaded(boolean exists) {
        //user doesnt exist, add and wait
        if (!exists) {
            user = loadUser();
            crud.setUserInFirebase(user);
            crud.queryForOpponent(user, USER_LOADED, OpponentScreen.this);
            waitForNotification(R.string.searching);
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

    public static class LoaderDialog extends DialogFragment {

        public LoaderDialog(){}

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {

            ProgressDialog _dialog = new ProgressDialog(getActivity());
            this.setStyle(STYLE_NO_TITLE, getTheme()); // You can use styles or inflate a view
            _dialog.setMessage(getString(message)); // set your messages if not inflated from XML
            _dialog.setCancelable(false);
            _dialog.setCanceledOnTouchOutside(false);
            return _dialog;
        }
    }
}
