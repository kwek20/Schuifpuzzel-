package net.brord.schuifpuzzel.firebase;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import net.brord.schuifpuzzel.User;

/**
 * Created by Iv on 18-6-2015.
 */
public class FirebaseUsersCRUD extends FirebaseCRUD<User>{
    String userInfo;
    Firebase firebaseRef;
    Firebase users;
    public FirebaseUsersCRUD(Context context){
        super(context, "users");
        userInfo = "bla";
        firebaseRef = FirebaseRef.getFirebaseRef();
        users = firebaseRef.child("users");
        users.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 userInfo = (String)dataSnapshot.getValue();
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
        users.setValue(user);
    }
//    data.addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            long room =  (long)dataSnapshot.getValue();
//            Toast.makeText(Schuifpuzzel.this, "IK KOM LEKKER HIERIN " + room, Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onCancelled(FirebaseError firebaseError) {
//
//        }
//    });
}
