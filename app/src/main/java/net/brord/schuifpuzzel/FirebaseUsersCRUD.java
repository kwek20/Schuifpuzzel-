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
public class FirebaseUsersCRUD extends FirebaseCRUD<User>{
    String userInfo;

    FirebaseUsersCRUD(Context context){
        super(context, "users");
        userInfo = "bla";
    }

    public void createUserData(User user){
        getFirebase().setValue(user);
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
