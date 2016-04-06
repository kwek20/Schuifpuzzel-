package net.brord.schuifpuzzel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import net.brord.schuifpuzzel.POD.User;
import net.brord.schuifpuzzel.enums.Difficulty;
import net.brord.schuifpuzzel.firebase.FirebaseUsersCRUD;
import net.brord.schuifpuzzel.interfaces.CallbackInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brord on 6/18/2015.
 */
public class StartScreen extends ActionBarActivity implements CallbackInterface{
    public CallbackInterface cb;
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
        startActivityForResult(i, 1);
    }

    public void multiPlayer(View v){
        Intent i = new Intent(this, OpponentScreen.class);
        startActivity(i);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Intent i = new Intent(this, PlayScreen.class);
                i.putExtra("difficulty", data.getStringExtra("difficulty"));
                i.putExtra("image", data.getStringExtra("image"));
                startActivity(i);
            }
        }
    }

    @Override
    public String returnData(String data) {
        return data;
    }
}
