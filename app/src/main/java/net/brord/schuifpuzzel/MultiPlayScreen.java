package net.brord.schuifpuzzel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.brord.schuifpuzzel.POD.DrawData;
import net.brord.schuifpuzzel.POD.Room;
import net.brord.schuifpuzzel.POD.User;
import net.brord.schuifpuzzel.enums.DataReceived;
import net.brord.schuifpuzzel.enums.Difficulty;
import net.brord.schuifpuzzel.firebase.FirebaseListener;
import net.brord.schuifpuzzel.firebase.FirebaseRoomCRUD;
import net.brord.schuifpuzzel.firebase.FirebaseUsersCRUD;
import net.brord.schuifpuzzel.images.ImageGridManager;
import net.brord.schuifpuzzel.images.ImageManager;
import net.brord.schuifpuzzel.images.MultiPlayerImageClickListener;
import net.brord.schuifpuzzel.interfaces.DrawListener;

import java.util.List;

/**
 * Created by Brord on 4/1/2016.
 */
public class MultiPlayScreen extends PlayScreen implements FirebaseListener, DrawListener {

    private Room room;
    private User user;

    private FirebaseRoomCRUD roomCrud;

    private boolean host;
    private boolean started = false;

    private DrawView drawingView;

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
}

    @Override
    protected int getLayout() {
        return R.layout.activity_multiplayscreen;
    }

    @Override
    public void setupScreen() {
        dif = room.getDifficulty();

        LinearLayout group = (LinearLayout) findViewById(R.id.gameImage);
        grid = new ImageGridManager(dif.getX(), dif.getY(), BORDER, group);

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

        //the listener for each image
        grid.setClickListener(clickListener = new MultiPlayerImageClickListener(manager));
        grid.setup(this);

        //disable grid stuff after setup!
        setGridEnabled(false);

        drawingView = new DrawView(this, this);
        LinearLayout l = (LinearLayout) findViewById(R.id.gameImage);
        addContentView(drawingView, l.getLayoutParams());
    }

    @Override
    protected void quitGame() {
        if (room.getUser2().equals("")){
            new AlertDialog.Builder(MultiPlayScreen.this)
                    .setTitle(getString(R.string.user_left))
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            roomCrud.delete(room);

                            Intent i = new Intent(MultiPlayScreen.this, OpponentScreen.class);
                            i.putExtra("user", user);
                            startActivity(i);
                        }
                    }).show();
        } else {
            roomCrud.leaveRoom(room, user);
            new FirebaseUsersCRUD(this).userLeaveRoom(user);

            Intent i = new Intent(this, OpponentScreen.class);
            i.putExtra("user", user);
            startActivity(i);
        }
    }

    @Override
    protected void restart(Difficulty d){
        if (room.getUser1Active() && !room.getUser1().equals(user.getUserName())) {
            toast(getString(R.string.not_your_turn));
            return;
        }

        Intent i = new Intent();
        i.setClass(this, this.getClass());
        room.setDifficulty(d);
        i.putExtra("user", user);
        i.putExtra("room", room);
        i.putExtra("isHost", host);
        startActivity(i);
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

            setUserLabelName(room.getUser1().equals(user.getUserName()) ? room.getUser2() : room.getUser1());
        }

        started = true;

        //always listen for drawdata since anyoen can clear it
        waitForDrawing();

        //listen for user leave :(
        roomCrud.queryForUserLeave(room, DataReceived.USER_LEFT, this);
    }

    @Override
    protected void onCountdownFinished() {
        if (host){
            super.onCountdownFinished();
        } else {
            //wait for tile send data!
        }
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

    public void endTurn(View v){
        //we ended the turn by button press
        endTurn();
    }

    public void endTurn(){
        ((MultiPlayerImageClickListener)clickListener).newRound();

        //disable our grid for movement
        setGridEnabled(false);

        //change active user
        room.setUser1Active(!room.getUser1Active());

        //set user label to opponent name
        setUserLabelName(room.getUser1().equals(user.getUserName()) ? room.getUser2() : room.getUser1());

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
        roomCrud.queryForDrawData(room, DataReceived.DRAW, this);
    }

    private void waitForTileData(){
        roomCrud.queryForTileData(room, DataReceived.WAIT_FOR_TILE_DATA, this);
    }

    private void setUserLabelName(String s) {
        TextView group = (TextView) findViewById(R.id.currentPlayer);
        group.setText(getString(R.string.currentPlayer) + " " + s);
    }

    private void setGridEnabled(boolean b) {
        LinearLayout group = (LinearLayout) findViewById(R.id.gameImage);
        group.setEnabled(b);

        clickListener.setActive(b);
    }

    private void cleanCanvas(){
        drawingView.clearDrawing();
    }

    private void drawOnCanvas(){
        drawingView.clearDrawing();
        drawingView.loadFromData(room.getDrawData());
    }

    public void clearScreen(View v){
        //clear screen button
        cleanCanvas();
    }

    private void loadCanvas(){
        drawingView.setDisabled(false);
        cleanCanvas();
    }

    private void unloadCanvas(){
        drawingView.setDisabled(true);
        cleanCanvas();
    }

    @Override
    public void sendDrawUpdate(List<DrawData> data) {
        room.setDrawData(drawingView.getStoredData());
        roomCrud.sendRoomUpdate(room);
    }

    @Override
    public void onDataReceived(Object o, DataReceived ID) {
        if (ID == DataReceived.DRAW){
            //drawing! WOOOOO
            room = (Room) o;
            drawOnCanvas();
        } else if (ID == DataReceived.OPPONENT_END_TURN){
            room = (Room) o;
            loadNewRoomTiles();
            startTurn();
        } else if (ID == DataReceived.WAIT_FOR_TILE_DATA){
            room = (Room) o;
            loadNewRoomTiles();

            if (!started){ //first time we receive images, so also fire up our magic <3
                gameStarted();
            }
        } else if (ID == DataReceived.USER_LEFT){
            quitGame();
        }
    }

    @Override
    public void onDataCancelled(DataReceived ID) {

    }
}
