package net.brord.schuifpuzzel.firebase;

import com.firebase.client.Firebase;

/**
 * Created by Iv on 18-6-2015.
 */
public class FirebaseRef {
    private static Firebase firebase;

    private FirebaseRef(){}

    public static Firebase getFirebaseRef(){
        if (firebase == null){
            firebase = new Firebase("https://npuzzle2.firebaseio.com/");
        }
        return firebase;
    }
}
