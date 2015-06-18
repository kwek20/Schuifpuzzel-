package net.brord.schuifpuzzel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;
import java.util.Objects;

/**
 * Created by Iv on 18-6-2015.
 */
public class FirebaseUsersCRUD {

    Firebase myFirebaseRef;
    Firebase users;
    String userInfo;

    FirebaseUsersCRUD(Context context){

        Firebase.setAndroidContext(context);
        myFirebaseRef = FirebaseRef.getFirebaseRef();
        users = myFirebaseRef.child("users");
        userInfo = "bla";
        users.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
//                Object userInformation = (Object) snapshot.getValue();
                userInfo = (String)snapshot.child("userName").getValue();
                Log.d("FirebaseCrud", "here user info"+userInfo);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void createUserData(User user){
        users.setValue(user);
    }
    public String getAllUserData() {
        return userInfo;
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
