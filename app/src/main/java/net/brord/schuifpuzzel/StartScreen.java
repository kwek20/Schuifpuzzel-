package net.brord.schuifpuzzel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import net.brord.schuifpuzzel.firebase.FirebaseUsersCRUD;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brord on 6/18/2015.
 */
public class StartScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_startscreen);
        //creating user
        User user1 = new User("jan",null);
        User user2 = new User("piet",null);
        User user3 = new User("mark",null);
        Map<String,User> users = new HashMap<String,User>();
        users.put("jan",user1);
        users.put("piet",user2);
        users.put("mark",user3);

        //saving it in the firebase
        FirebaseUsersCRUD usersFirebaseController = new FirebaseUsersCRUD(this);
        usersFirebaseController.setUsersInFirebase(users);
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
