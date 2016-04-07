package net.brord.schuifpuzzel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import net.brord.schuifpuzzel.POD.User;
import net.brord.schuifpuzzel.enums.DataReceived;
import net.brord.schuifpuzzel.enums.Difficulty;
import net.brord.schuifpuzzel.firebase.FirebaseListener;
import net.brord.schuifpuzzel.firebase.FirebaseUsersCRUD;
import net.brord.schuifpuzzel.interfaces.CallbackInterface;


/**
 * Created by Brord on 6/18/2015.
 */
public class StartScreen extends ActionBarActivity implements FirebaseListener{
    public CallbackInterface cb;

    private FirebaseUsersCRUD crud;
    private static int message;
    private LoaderDialog loader;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_startscreen);

        //saving it in the firebase
        crud = new FirebaseUsersCRUD(this);

    }

    public void singlePlayer(View v){
        Intent i = new Intent(this, Schuifpuzzel.class);
        startActivityForResult(i, 1);
    }

    public void multiPlayer(View v){
        final EditText input = new EditText(this);
        new AlertDialog.Builder(StartScreen.this)
                .setTitle(R.string.find)
                .setMessage(getString(R.string.username))
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        userName = value.toString();
                        crud.queryUserData(value.toString(), DataReceived.USER_QUERIED.getId(), StartScreen.this);
                        waitForNotification(R.string.checking);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    public void waitForNotification(int message) {
        this.message = message;
        (loader = new LoaderDialog()).show(getFragmentManager(), OpponentScreen.LOADER_TAG);
    }

    public void doneLoading() {

        if (loader != null && loader.getShowsDialog()) {
            loader.dismiss();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Intent i = new Intent(this, PlayScreen.class);
                i.putExtra("difficulty", (Difficulty)data.getSerializableExtra("difficulty"));
                i.putExtra("image", data.getStringExtra("image"));
                startActivity(i);
            }
        }
    }

    @Override
    public void onDataReceived(Object o, int ID) {
        if (ID == DataReceived.USER_QUERIED.getId()){
            //hey user
            handleUserLoaded( o != null);

        }
    }

    private void handleUserLoaded(boolean exists) {
        //user doesnt exist, add and wait
        if (!exists) {
            User u;
            crud.setUserInFirebase(u = new User(userName));
            doneLoading();

            Log.d("MAD", "User added");

            Intent i = new Intent(this, OpponentScreen.class);
            i.putExtra("user", u);
            startActivity(i);

        } else {
            //user exists
            new AlertDialog.Builder(StartScreen.this)
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
