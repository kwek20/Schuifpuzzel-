package net.brord.schuifpuzzel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.widget.LinearLayout;

import net.brord.schuifpuzzel.POD.Room;
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

    //private final Room room;

    private int myId;

    private Canvas canvas;

    private FirebaseRoomCRUD roomCrud;

//    public MultiPlayScreen(Room room, int myId){
//        this.room = room;
//
//        roomCrud = new FirebaseRoomCRUD(this);
//
//        Bitmap bg = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//        canvas = new Canvas(bg);
//        Paint clearPaint = new Paint();
//        canvas.drawRect(50, 50, 200, 200, clearPaint);
//
//        //attach to an element?!
//        /*LinearLayout ll = (LinearLayout) findViewById(R.id.rect);
//        ll.setBackgroundDrawable(new BitmapDrawable(bg));*/
//    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomCrud = new FirebaseRoomCRUD(this);
        setContentView(R.layout.activity_playscreen);
        LinearLayout group = (LinearLayout) findViewById(R.id.gameImage);

//        //load difficulty
//        dif = (Difficulty) getIntent().getSerializableExtra("difficulty");
//        grid = new ImageGridManager(dif.getX(), dif.getY(), BORDER, group);
//
//        //load image manager for handling the image drawing
//        manager = new ImageManager(grid,
//                generateDrawables(getImage(getIntent().getStringExtra("image"))), //its a manager not generator
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        //win(); //WOOT!
//                    }
//                });
//
//        //the listener for each image
//        grid.setClickListener(clickListener = new ImageClickListener(manager));
//        grid.setup(this);
//
//        //load images in layout
//        manager.loadImage(dif);
//
//        loadDialog();
//
//        //start countdown
//        startCountdown();
        //saving it in the firebase

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
