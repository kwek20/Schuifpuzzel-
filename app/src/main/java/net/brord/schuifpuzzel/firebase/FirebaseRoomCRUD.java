package net.brord.schuifpuzzel.firebase;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import net.brord.schuifpuzzel.POD.Room;
import net.brord.schuifpuzzel.POD.User;
import net.brord.schuifpuzzel.interfaces.CallbackInterface;

import java.util.Map;

/**
 * Created by Iv on 18-6-2015.
 */
public class FirebaseRoomCRUD extends FirebaseCRUD<Room> {
    Firebase rooms;

    public FirebaseRoomCRUD(Context context){
        super(context, "rooms");
        rooms = super.getFirebase().child("rooms");

        rooms.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void setRoomInFirebase(Room room){
        Log.d("MAD", "Room: " + room.getRoomId());
        rooms.child(room.getRoomId() + "").setValue(room);
    }

    public void queryRoomData(final String userName, final int ID, final FirebaseListener listener) {
        rooms.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onDataReceived(dataSnapshot.hasChild(userName), ID);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onDataCancelled(ID);
            }
        });
    }
}
