package net.brord.schuifpuzzel.firebase;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import net.brord.schuifpuzzel.POD.User;
import net.brord.schuifpuzzel.interfaces.CallbackInterface;

import java.util.Map;

/**
 * Created by Iv on 18-6-2015.
 */
public class FirebaseUsersCRUD extends FirebaseCRUD<User> {
    Firebase users;
    public Boolean userExistance = false;
    public String userInfo = "bla";
    private CallbackInterface callback;
    public FirebaseUsersCRUD(Context context){
        super(context, "users");
        users = super.getFirebase().child("users");
        users.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("FirebaseCrud", "received data is:" + dataSnapshot.getValue());
                userInfo = (String)dataSnapshot.child("mark").child("userName").getValue();
//                Schuifpuzzel.setUserInfo(userInfo);

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public String getAllUserData() {
        return userInfo;
    }
    public void setUserInFirebase(User user){
        Log.d("MAD", "name: " + user.getUserName());
        users.child(user.getUserName()).setValue(user);
        Log.d("MAD", "ADDED CHHILDDASFA");
        Log.d("MAD", userInfo);
    }
    public void setUsersInFirebase(Map<String,User> users){
        this.users.setValue(users);
    }

    public void queryUserData(final String userName, final int ID, final FirebaseListener listener) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("MAD",(String)dataSnapshot.child("userName").getValue());
//                listener.onDataReceived(dataSnapshot.hasChildren(), ID);

                listener.onDataReceived(dataSnapshot.hasChild(userName), ID);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onDataCancelled(ID);
            }
        });
    }

    public void queryForOpponent(final User u, final int ID, final FirebaseListener listener) {
        users.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("FirebaseCrud", "received data is:" + dataSnapshot.getValue());
                listener.onDataReceived(dataSnapshot.child("").getValue(), ID);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onDataCancelled(ID);
            }
        });
    }

}
