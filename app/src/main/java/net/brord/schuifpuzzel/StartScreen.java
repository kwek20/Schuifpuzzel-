package net.brord.schuifpuzzel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.brord.schuifpuzzel.POD.Room;
import net.brord.schuifpuzzel.POD.User;
import net.brord.schuifpuzzel.enums.DataReceived;
import net.brord.schuifpuzzel.enums.Difficulty;
import net.brord.schuifpuzzel.firebase.FirebaseListener;
import net.brord.schuifpuzzel.firebase.FirebaseUsersCRUD;
import net.brord.schuifpuzzel.interfaces.CallbackInterface;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brord on 6/18/2015.
 */
public class StartScreen extends ActionBarActivity implements FirebaseListener{
    public CallbackInterface cb;

    private FirebaseUsersCRUD crud;
    private int message;
    private OpponentScreen.LoaderDialog loader;
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
                        waitForNotification(R.string.searching);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    public void waitForNotification(int message) {
        this.message = message;
        (loader = new OpponentScreen.LoaderDialog()).show(getFragmentManager(), OpponentScreen.LOADER_TAG);
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
}
