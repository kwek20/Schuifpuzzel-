package net.brord.schuifpuzzel.firebase;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import net.brord.schuifpuzzel.Schuifpuzzel;
import net.brord.schuifpuzzel.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Iv on 18-6-2015.
 */
public class FirebaseUsersCRUD extends FirebaseCRUD<User>{
    Firebase users;
    public String userInfo = "bla";
//    Schuifpuzzel puzzle = new Schuifpuzzel();

    public FirebaseUsersCRUD(Context context){
        super(context, "users");
        users = super.getFirebase().child("users");
        users.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("FirebaseCrud", "received data is:" + dataSnapshot.getValue());
                userInfo = (String)dataSnapshot.child("mark").child("userName").getValue();
                Schuifpuzzel.setUserInfo(userInfo);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
    public String getAllUserData() {
        return userInfo;
    }
    public void sendUserData(String data) {
//        puzzle.setUserData(data);
    }
    public void setUserInFirebase(User user){
        users.setValue(user);
    }
    public void setUsersInFirebase(Map<String,User> users){
        this.users.setValue(users);
    }
}
