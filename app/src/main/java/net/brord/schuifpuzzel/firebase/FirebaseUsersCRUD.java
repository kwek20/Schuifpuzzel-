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
import net.brord.schuifpuzzel.enums.DataReceived;
import net.brord.schuifpuzzel.enums.Status;
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

    public void setUserInFirebase(User user){
        users.child(user.getUserName()).setValue(user);
    }
    public void queryUserData(final String userName, final DataReceived ID, final FirebaseListener listener) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = null;
                if (dataSnapshot.hasChild(userName)) {
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
        u.setRoomStatus(Status.SEARCHING);
        users.child(u.getUserName()).setValue(u);
    }

    public void queryForOpponent(final User u, final DataReceived ID, final FirebaseListener listener) {
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onDataReceived(dataSnapshot.hasChild(u.getUserName()), ID);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onDataCancelled(ID);
            }
        });
    }

    public void deleteUser(User user) {
        users.child(user.getUserName()).setValue(null);
    }

    public void userLeaveRoom(User user) {
        user.setRoomID("");
        user.setRoomStatus(Status.NO_ROOM);
        users.child(user.getUserName()).setValue(user);
    }

    public void changeUserStatus(User user, Status started) {
        user.setRoomStatus(started);
        users.child(user.getUserName()).setValue(user);
    }
}
