package net.brord.schuifpuzzel;

import android.content.Context;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;

/**
 * Created by Brord on 6/18/2015.
 */
public abstract class FirebaseCRUD<T> {

    private Firebase firebaseRef;
    private String tag;

    public FirebaseCRUD(Context context, String tag){
        Firebase.setAndroidContext(context);
        this.tag = tag;
        Firebase.setAndroidContext(context);
        firebaseRef = FirebaseRef.getFirebaseRef().child("users");
        firebaseRef.addChildEventListener(getChildEventListener());
    }
    public String getTag() {
        return tag;
    }

    public Firebase getFirebase() {
        return firebaseRef;
    }

    @Override
    public String toString() {
        return "FirebaseCRUD<" + tag + ">";
    }
}
