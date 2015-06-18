package net.brord.schuifpuzzel.firebase;

import android.content.Context;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;

/**
 * Created by Brord on 6/18/2015.
 */
public abstract class FirebaseCRUD<T extends Serializable>{

    private Firebase firebaseRef;
    private String tag;

    public FirebaseCRUD(Context context, String tag){
        Firebase.setAndroidContext(context);
        this.tag = tag;
        firebaseRef = FirebaseRef.getFirebaseRef().child("users");
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
}
