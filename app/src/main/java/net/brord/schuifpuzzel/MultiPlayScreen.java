package net.brord.schuifpuzzel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private boolean host;

    protected void onCreate(Bundle savedInstanceState) {
        room = (Room) getIntent().getSerializableExtra("room");
        user = (User) getIntent().getSerializableExtra("user");
        host = (boolean) getIntent().getSerializableExtra("isHost");
        roomCrud = new FirebaseRoomCRUD(this);

        if (!host){
            //wait for tile send data
            waitForTileData();
        }

        super.onCreate(savedInstanceState);

        Bitmap bg = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bg);
        Paint clearPaint = new Paint();
        canvas.drawRect(50, 50, 200, 200, clearPaint);
}

    @Override
    public void setupScreen() {
        dif = room.getDifficulty();

        LinearLayout group = (LinearLayout) findViewById(R.id.gameImage);
        grid = new ImageGridManager(dif.getX(), dif.getY(), BORDER, group);
        setGridEnabled(false);


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

    @Override
    protected void gameStarted(){
        if (room.getUser1Active() && user.getUserName().equals(room.getUser1())){
            //shuffle that shit
            super.gameStarted();

            //we start, send OUR tile data
            sendRoomTiles();

            startTurn();
        } else {
            //load drawing canvas
            loadCanvas();

            //wait for opponent end turn
            waitForStart();

            setUserLabelName(room.getUser1() == user.getUserName() ? room.getUser2() : room.getUser1());
        }
    }

    @Override
    protected void onCountdownFinished() {
        if (host){
            super.onCountdownFinished();
        } else {
            //wait for tile send data!
        }
    }

    public void endTurn(View v){
        //we ended the turn by button press
        endTurn();
    }

    private void loadNewRoomTiles(){
        //new tiles in room variable
        manager.loadDataFrom(room.getTileData());
    }

    private void sendRoomTiles() {
        //set tiles in room variable
        room.setTileData(manager.getTileData());

        //send room to firebase
        roomCrud.sendRoomUpdate(room);
        //opponent will receive a OPPONENT_END_TURN or a WAIT_FOR_TILE_DATA notification
    }

    @Override
    protected ImageView loadImage() {
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(roomCrud.getImage(room));
        return iv;
    }

    public void startTurn(){
        unloadCanvas();
        setGridEnabled(true);
        setUserLabelName(user.getUserName());
        //we can make moves now
    }

    public void endTurn(){
        //disable our grid for movement
        setGridEnabled(false);

        //change active user
        room.setUser1Active(!room.getUser1Active());

        //set user label to opponent name
        setUserLabelName(room.getUser1() == user.getUserName() ? room.getUser2() : room.getUser1());

        //send update data and notify user that our turn has ended
        sendRoomTiles();

        //load drawing canvas
        loadCanvas();

        //wait for opponent end turn
        waitForStart();
    }

    private void waitForStart() {
        roomCrud.queryForRoomUser1Active(room, !room.getUser1Active(), DataReceived.OPPONENT_END_TURN, this);
    }

    private void waitForDrawing() {
        roomCrud.queryForRoomUser1Active(room, !room.getUser1Active(), DataReceived.DRAW, this);
    }

    private void waitForTileData(){
        roomCrud.queryFoTileData(room, DataReceived.WAIT_FOR_TILE_DATA, this);
    }

    private void setUserLabelName(String s) {
        TextView group = (TextView) findViewById(R.id.currentPlayer);
        group.setText(getString(R.string.currentPlayer) + s);
    }

    private void setGridEnabled(boolean b) {
        LinearLayout group = (LinearLayout) findViewById(R.id.gameImage);
        group.setEnabled(b);
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
        } else if (ID == DataReceived.OPPONENT_END_TURN){
            room = (Room) o;
            startTurn();
        } else if (ID == DataReceived.WAIT_FOR_TILE_DATA){
            room = (Room) o;
            loadNewRoomTiles();
        }
    }

    public void getRoomData(Room r){

    }

    @Override
    public void onDataCancelled(DataReceived ID) {

    }

}
