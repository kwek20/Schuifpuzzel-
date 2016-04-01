package net.brord.schuifpuzzel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import net.brord.schuifpuzzel.POD.Room;
import net.brord.schuifpuzzel.enums.DataReceived;
import net.brord.schuifpuzzel.firebase.FirebaseListener;

/**
 * Created by Brord on 4/1/2016.
 */
public class MultiPlayScreen extends PlayScreen implements FirebaseListener {

    private final Room room;
    private int myId;

    private Canvas canvas;

    public MultiPlayScreen(Room room, int myId){
        this.room = room;

        Bitmap bg = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bg);
        Paint clearPaint = new Paint();
        canvas.drawRect(50, 50, 200, 200, clearPaint);

        //attach to an element?!
        /*LinearLayout ll = (LinearLayout) findViewById(R.id.rect);
        ll.setBackgroundDrawable(new BitmapDrawable(bg));*/
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
    public void onDataReceived(Object o, int ID) {
        if (ID == DataReceived.DRAW.getId()){
            //drawing! WOOOOO
            drawOnCanvas();
        }
    }

    @Override
    public void onDataCancelled(int ID) {

    }
}
