package net.brord.schuifpuzzel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
    }

    private boolean checkUsername() {
        TextView group = (TextView) findViewById(R.id.userName);
        Log.d("MAD", "name: " + group.getText());
        if (!group.getText().equals("firebasecheckhere")) return true;

        new AlertDialog.Builder(OpponentScreen.this)
                .setTitle(R.string.error)
                .setMessage(getString(R.string.unavailable))
        .show();

        return false;
    }
}
