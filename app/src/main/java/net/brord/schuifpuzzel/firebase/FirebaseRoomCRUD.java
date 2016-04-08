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

import net.brord.schuifpuzzel.MultiPlayScreen;
import net.brord.schuifpuzzel.OpponentScreen;
import net.brord.schuifpuzzel.POD.Room;
import net.brord.schuifpuzzel.POD.User;
import net.brord.schuifpuzzel.enums.DataReceived;
import net.brord.schuifpuzzel.enums.Difficulty;
import net.brord.schuifpuzzel.interfaces.CallbackInterface;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
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
    public void setOpponentinRoom(String user2, String roomID){
        rooms.child(roomID).child("user2").setValue(user2);
    }
    public Bitmap getImage(Room r){
        byte[] imageAsBytes = Base64.decode(r.getImage(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        return bmp;
    }

    public Room getRoom(String id){
        for (Room r : roomsList){
            if (r.getRoomId().equals(id)) return r;
        }
        return null;
    }

    public void queryRoomData(final String userName, final DataReceived ID, final FirebaseListener listener) {
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

    public void queryForOpponent(final Room r, final DataReceived id, final FirebaseListener listener) {
        rooms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(r.getRoomId())) {
                    Room roomFound = dataSnapshot.child(r.getRoomId()).getValue(Room.class);
                    if (r.getRoomId().equals(roomFound.getRoomId())) {
                        if (!roomFound.getUser2().equals("")) {
                            rooms.removeEventListener(this);
                            listener.onDataReceived(roomFound, id);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onDataCancelled(id);
            }
        });
    }

    public void queryForRoomUser1Active(final Room room, final boolean b, final DataReceived id, final FirebaseListener listener) {
        rooms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room.getRoomId())) {
                    Room roomFound = dataSnapshot.child(room.getRoomId()).getValue(Room.class);
                    if (room.getRoomId().equals(roomFound.getRoomId())) {
                        if (roomFound.getUser1Active() == b) {
                            rooms.removeEventListener(this);
                            listener.onDataReceived(roomFound, id);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onDataCancelled(id);
            }
        });
    }

    public void queryForTileData(final Room room, final DataReceived id, final FirebaseListener listener) {
        rooms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room.getRoomId())) {
                    Room roomFound = dataSnapshot.child(room.getRoomId()).getValue(Room.class);
                    if (room.getRoomId().equals(roomFound.getRoomId())) {
                        if (!Arrays.equals(roomFound.getTileData(),room.getTileData())) {
                            rooms.removeEventListener(this);
                            listener.onDataReceived(roomFound, id);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onDataCancelled(id);
            }
        });
    }

    public void sendRoomUpdate(Room room) {
        rooms.child(room.getRoomId()).setValue(room);
    }
}
