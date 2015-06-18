package net.brord.schuifpuzzel;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by Iv on 18-6-2015.
 */
public class FirebaseUsersCRUD extends FirebaseCRUD<User>{
    String userInfo;

    FirebaseUsersCRUD(Context context){
        super(context, "users");
        userInfo = "bla";
    }

    public ValueEventListener getChildEventListener(){
        return new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String info = (String)dataSnapshot.getValue();
                Log.d("FirebaseCrud", "user info:" + info);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
//        return new ChildEventListener() {
//
//            @Override
//            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
////                Object userInformation = (Object) snapshot.getValue();
//                Object userName = (Object)snapshot.getValue();
//                Log.d("FirebaseCrud", "here user info" + userName.toString());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        };
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
