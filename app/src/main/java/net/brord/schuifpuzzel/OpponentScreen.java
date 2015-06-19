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
import android.app.FragmentManager;
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

    private String username;
    private User user;

    private LoaderDialog loader;
    private FirebaseUsersCRUD crud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_opponentscreen);
        crud = new FirebaseUsersCRUD(this);
    }

    public void findOpponent(View v){
        if (!checkUsername())return;
        final EditText input = new EditText(this);
        new AlertDialog.Builder(OpponentScreen.this)
                .setTitle(R.string.find)
                .setMessage(getString(R.string.username))
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        Log.d("MAD", value.toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    public void createGame(View v){
        if (!checkUsername())return;

        Log.d("MAD", "create");
        user = loadUser();
        crud.setUserInFirebase(user);
        crud.queryForOpponent(user, USER_LOADED, OpponentScreen.this);
        waitForNotification();
    }

    private boolean checkUsername() {
        TextView group = (TextView) findViewById(R.id.userName);
        Log.d("MAD", "exists: " + crud.userExists(username = group.getText().toString()));
        if (!crud.userExists(username = group.getText().toString())) return true;

        new AlertDialog.Builder(OpponentScreen.this)
                .setTitle(R.string.error)
                .setMessage(getString(R.string.unavailable))
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                }).show();
        return false;
    }

    private User loadUser() {
        return new User(username, getLocation());
    }

    private Location getLocation() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
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

    public void waitForNotification() {
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
                doneLoading();

            }
            Log.d("MAD", "User found!");
        }
    }

    public static class LoaderDialog extends DialogFragment {

        public LoaderDialog(){}

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {

            ProgressDialog _dialog = new ProgressDialog(getActivity());
            this.setStyle(STYLE_NO_TITLE, getTheme()); // You can use styles or inflate a view
            _dialog.setMessage(getString(R.string.searching)); // set your messages if not inflated from XML
            _dialog.setCancelable(false);
            _dialog.setCanceledOnTouchOutside(false);
            return _dialog;
        }
    }
}
