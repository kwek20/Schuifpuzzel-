package net.brord.schuifpuzzel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by Brord on 6/18/2015.
 */
public class StartScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_startscreen);
    }

    public void singlePlayer(View v){
        Intent i = new Intent(this, Schuifpuzzel.class);
        startActivity(i);
    }

    public void multiPlayer(View v){
        Intent i = new Intent(this, OpponentScreen.class);
        startActivity(i);
    }
}
