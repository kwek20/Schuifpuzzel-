package net.brord.schuifpuzzel.firebase;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import net.brord.schuifpuzzel.OpponentScreen;
import net.brord.schuifpuzzel.POD.Room;
import net.brord.schuifpuzzel.POD.User;
import net.brord.schuifpuzzel.interfaces.CallbackInterface;

import java.util.Map;

/**
 * Created by Iv on 18-6-2015.
 */
public class FirebaseUsersCRUD extends FirebaseCRUD<User> {
    Firebase users;
    public String userInfo = "bla";

    public FirebaseUsersCRUD(Context context){
        super(context, "users");
        users = super.getFirebase().child("users");

        users.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = (String)dataSnapshot.child("mark").child("userName").getValue();
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
        users.child(user.getUserName()).setValue(user);
    }
    public void setUsersInFirebase(Map<String,User> users){
        this.users.setValue(users);
    }
    public void queryUserData(final String userName, final int ID, final FirebaseListener listener) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = null;
                if (dataSnapshot.hasChild(userName)){
                    user = dataSnapshot.child(userName).getValue(User.class);
                }
                listener.onDataReceived(user, ID);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onDataCancelled(ID);
            }
        });
    }

    public void assignRoomToUser(final User u, final Room r){
        u.setRoomID(r.getRoomId());
        users.child(u.getUserName()).setValue(u);
    }

    public void queryForOpponent(final User u, final int ID, final FirebaseListener listener) {
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("FirebaseCrud", "received data is:" + dataSnapshot.getValue());
                Log.d("MAD", "Added user is: " + u.getUserName());
                listener.onDataReceived(dataSnapshot.hasChild(u.getUserName()), ID);
                //Slistener.onDataReceived(dataSnapshot.child("").getValue(), ID);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onDataCancelled(ID);
            }
        });
    }
}
