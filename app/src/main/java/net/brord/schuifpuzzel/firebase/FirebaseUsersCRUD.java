package net.brord.schuifpuzzel.firebase;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import net.brord.schuifpuzzel.OpponentScreen;
import net.brord.schuifpuzzel.Schuifpuzzel;
import net.brord.schuifpuzzel.User;
import net.brord.schuifpuzzel.interfaces.CallbackInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Iv on 18-6-2015.
 */
public class FirebaseUsersCRUD extends FirebaseCRUD<User> {
    Firebase users;
    public Boolean userExistance = false;
    public String userInfo = "bla";
    private CallbackInterface callback;
    public FirebaseUsersCRUD(Context context, final CallbackInterface cb){
        super(context, "users");
        users = super.getFirebase().child("users");
        users.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("FirebaseCrud", "received data is:" + dataSnapshot.getValue());
                userInfo = (String)dataSnapshot.child("mark").child("userName").getValue();
//                Schuifpuzzel.setUserInfo(userInfo);
                cb.returnData(userInfo);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
    public Boolean userExists(String userName){
        if (users.child(userName).child(userName) == null){
            return false;
        }
        return true;
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


    public void queryForOpponent(final User u, final int ID, final FirebaseListener listener) {
        users.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("FirebaseCrud", "received data is:" + dataSnapshot.getValue());
                userInfo = (String)dataSnapshot.child("mark").child("userName").getValue();
                listener.onDataReceived(userInfo, ID);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

}
