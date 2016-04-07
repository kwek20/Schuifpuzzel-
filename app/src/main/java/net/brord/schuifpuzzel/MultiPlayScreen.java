package net.brord.schuifpuzzel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.brord.schuifpuzzel.POD.Room;
import net.brord.schuifpuzzel.POD.User;
import net.brord.schuifpuzzel.enums.DataReceived;
import net.brord.schuifpuzzel.enums.Difficulty;
import net.brord.schuifpuzzel.firebase.FirebaseListener;
import net.brord.schuifpuzzel.firebase.FirebaseRoomCRUD;
import net.brord.schuifpuzzel.firebase.FirebaseUsersCRUD;
import net.brord.schuifpuzzel.images.ImageClickListener;
import net.brord.schuifpuzzel.images.ImageGridManager;
import net.brord.schuifpuzzel.images.ImageManager;

/**
 * Created by Brord on 4/1/2016.
 */
public class MultiPlayScreen extends PlayScreen implements FirebaseListener {

    private Room room;
    private User user;

    private int myId;

    private Canvas canvas;

    private FirebaseRoomCRUD roomCrud;

    protected void onCreate(Bundle savedInstanceState) {
        room = (Room) getIntent().getSerializableExtra("room");
        user = (User) getIntent().getSerializableExtra("user");

        roomCrud = new FirebaseRoomCRUD(this);

        super.onCreate(savedInstanceState);

        Bitmap bg = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bg);
        Paint clearPaint = new Paint();
        canvas.drawRect(50, 50, 200, 200, clearPaint);
}

    @Override
    public void setupScreen() {
        dif = room.getDifficulty();

        ImageView iv = new ImageView(this);
        iv.setImageBitmap(roomCrud.getImage(room));

        manager = new ImageManager(grid,
                generateDrawables(iv), //its a manager not generator
                new Runnable() {
                    @Override
                    public void run() {
                        //win(); //WOOT!
                    }
                });
    }

    public void startTurn(){
        unloadCanvas();

        //we can make moves now
    }

    public void endTurn(){
        //disable movement

        loadCanvas();
    }

    private void cleanCanvas(){
        Paint clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRect(0, 0, 100, 100, clearPaint);
    }

    private void drawOnCanvas(){
        if (canvas == null) return;

        //draw stuff?
        Paint red = new Paint();
        red.setARGB(20, 20, 0, 0);
        canvas.drawCircle(20, 20, 10, red);
    }

    private void loadCanvas(){
        cleanCanvas();
    }

    private void unloadCanvas(){
        cleanCanvas();
    }

    @Override
    public void onDataReceived(Object o, DataReceived ID) {
        if (ID == DataReceived.DRAW){
            //drawing! WOOOOO
            drawOnCanvas();
        }
    }

    @Override
    public void onDataCancelled(DataReceived ID) {

    }
}
