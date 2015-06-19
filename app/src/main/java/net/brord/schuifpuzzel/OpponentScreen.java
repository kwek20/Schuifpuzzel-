package net.brord.schuifpuzzel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Brord on 6/18/2015.
 */
public class OpponentScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_opponentscreen);
    }

    public void findOpponent(View v){
        final EditText input = new EditText(this);
        new AlertDialog.Builder(OpponentScreen.this)
                .setTitle("Update Status")
                .setMessage("Find Opponent")
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
        final EditText input = new EditText(this);
        new AlertDialog.Builder(OpponentScreen.this)
                .setTitle("Update Status")
                .setMessage("Create game")
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
}
