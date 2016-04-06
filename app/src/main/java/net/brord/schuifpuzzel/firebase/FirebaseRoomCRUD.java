package net.brord.schuifpuzzel.firebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import net.brord.schuifpuzzel.OpponentScreen;
import net.brord.schuifpuzzel.POD.Room;
import net.brord.schuifpuzzel.POD.User;
import net.brord.schuifpuzzel.enums.Difficulty;
import net.brord.schuifpuzzel.interfaces.CallbackInterface;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Created by Iv on 18-6-2015.
 */
public class FirebaseRoomCRUD extends FirebaseCRUD<Room> {
    Firebase rooms;

    java.util.List<Room> roomsList = new java.util.LinkedList<>();

    public FirebaseRoomCRUD(Context context){
        super(context, "rooms");
        rooms = super.getFirebase().child("rooms");

        rooms.addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                Room room = snapshot.getValue(Room.class);
                roomsList.add(room);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                roomsList.remove(dataSnapshot.getValue(Room.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public Room createRoomInFirebase(User user1, Difficulty difficulty, Bitmap bmp){
        Firebase base = rooms.push();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        bmp.recycle();
        byte[] byteArray = stream.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Room r = new Room(user1.getUserName(), base.getKey(), imageFile, difficulty);
        base.setValue(r);
        return r;
    }
    public void setOpponentinRoom(User user2, String roomID){
        //Firebase base = rooms.push();
        java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
        map.put("user2",user2.getUserName());
        rooms.child(roomID).push().setValue(map);
    }
    public Bitmap getImage(Room r){
        Firebase fb = rooms.child(r.getRoomId()).child("image");
        byte[] imageAsBytes = Base64.decode(fb.getKey(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        return bmp;
    }

    public Room getRoom(String id){
        for (Room r : roomsList){
            if (r.getRoomId().equals(id)) return r;
        }
        return null;
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

    public void queryForOpponent(final int id, final FirebaseListener listener) {
        rooms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("FirebaseCrud", "received data is:" + dataSnapshot.getValue());
                String newUser = "dataSnapshot.userfromSnapshot";
                Log.d("MAD", "Added user is: " + newUser);
                listener.onDataReceived(newUser, id);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onDataCancelled(id);
            }
        });
    }
}
