package net.brord.schuifpuzzel.firebase;

import android.content.Context;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.Serializable;

/**
 * Created by Brord on 6/18/2015.
 */
public abstract class FirebaseCRUD<T extends Serializable> implements ChildEventListener{

    private Firebase firebaseRef;
    private String tag;

    public FirebaseCRUD(Context context, String tag){
        Firebase.setAndroidContext(context);
        this.tag = tag;
        Firebase.setAndroidContext(context);
        firebaseRef = FirebaseRef.getFirebaseRef().child("users");
        firebaseRef.addChildEventListener(this);
    }
    public String getTag() {
        return tag;
    }

    public Firebase getFirebase() {
        return firebaseRef;
    }

    public void createData(T type){
        getFirebase().setValue(type);
    }

    @Override
    public String toString() {
        return "FirebaseCRUD<" + tag + ">";
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
