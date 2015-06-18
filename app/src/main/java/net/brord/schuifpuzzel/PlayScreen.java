package net.brord.schuifpuzzel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Brord on 23-3-2015.
 */
public class PlayScreen extends ActionBarActivity {

    private static final String MAD = "Mad";
    private static int BORDER = 10;

    public Difficulty dif;

    private ImageClickListener clickListener;
    private ImageManager manager;

    private ImageGridManager grid;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MAD", "onCreate " + savedInstanceState);
        setContentView(R.layout.activity_playscreen);
        LinearLayout group = (LinearLayout) findViewById(R.id.gameImage);

        //load difficulty
        dif = (Difficulty) getIntent().getSerializableExtra("difficulty");
        grid = new ImageGridManager(dif.getX(), dif.getY(), BORDER, group);

        //load image manager for handling the image drawing
        manager = new ImageManager(grid,
                generateDrawables(getImage(getIntent().getStringExtra("image"))), //its a manager not generator
                new Runnable() {
                    @Override
                    public void run() {
                        win(); //WOOT!
                    }
                });

        //the listener for each image
        grid.setClickListener(clickListener = new ImageClickListener(manager));
        grid.setup(this);

        //load images in layout
        manager.loadImage(dif);

        loadDialog();

        //start countdown
        startCountdown();
    }

    private void loadDialog() {
         dialog = new AlertDialog(this){

            @Override
            public boolean dispatchTouchEvent(MotionEvent event){
                dismiss();
                return false;
            }

        };
        dialog.setView(getImage(getIntent().getStringExtra("image")));
        dialog.setTitle(R.string.dismiss);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d("MAD", "onSaveInstanceState");
        // Save the user's current game state
        savedInstanceState.putString("grid", manager.store());
        savedInstanceState.putInt("moves", clickListener.getMoves());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("MAD", "onRestoreInstanceState");
        // Restore state members from saved instance

        clickListener.setMoves(savedInstanceState.getInt("moves"));
        manager.restore(savedInstanceState.getString("grid"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_schuifpuzzel, menu);

        MenuItem difficulty = menu.findItem(R.id.action_settings);
        SubMenu submenu = difficulty.getSubMenu();

        for (Difficulty d : Difficulty.values()) {
            if (d != dif) {
                submenu.add(getText(d.getDifficulty()));
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Difficulty dif = Difficulty.findByName(this, item.getTitle() + "");

        if (dif != null) {
            //checked difficulty
            restart(dif);
            return true;
        } else switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.reset:
                restart(this.dif);
                return true;
            case R.id.solution:
                dialog.show();
                return true;
            case R.id.quit:
                Intent i = new Intent(this, Schuifpuzzel.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void restart(Difficulty dif) {
        Intent i = new Intent();
        i.setClass(this, this.getClass());
        i.putExtra("difficulty", dif);
        i.putExtra("image", getIntent().getStringExtra("image"));
        startActivity(i);
    }

    private void win() {
        Log.e("MAD", "WON!");
        Intent i = new Intent(this, WinScreen.class);

        i.putExtra("difficulty", dif);
        i.putExtra("image", getIntent().getStringExtra("image"));
        i.putExtra("moves", clickListener.getMoves());

        startActivity(i);
    }

    private void startCountdown() {
        toast(dif.getDuration());
        new CountDownTimer(dif.getDuration(), 1000) {

            public void onTick(long millisUntilFinished) {
                toast(millisUntilFinished);
            }

            public void onFinish() {
                manager.shuffle();
                grid.getView(dif.getX() - 1, dif.getY() - 1).setVisibility(View.INVISIBLE);
                clickListener.setActive(true);

                if (dif.getX() % 2 == 0) {
                    manager.swap(manager.getView(dif.getX() - 1, dif.getY() - 2), manager.getView(dif.getX() - 1, dif.getY() - 3));
                }
                //win();
            }
        }.start();
    }

    private void toast(long seconds){
        Toast.makeText(PlayScreen.this, (CharSequence)("Starting in " + (seconds / 1000)), Toast.LENGTH_SHORT).show();
    }

    private ImageView getImage(String name) {
        try {
            Field image = R.mipmap.class.getField(name);
            ImageView iv = new ImageView(this);
            iv.setImageResource(image.getInt(null));
            return iv;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<ImageTile> generateDrawables(ImageView image) {
        //For the number of rows and columns of the grid to be displayed
        int rows = dif.getX(), cols = dif.getY();

        //For height and width of the small image chunks
        int chunkHeight, chunkWidth;

        //To store all the small image chunks in bitmap format in this list
        ArrayList<ImageTile> chunkedImages = new ArrayList<>(rows * cols);

        //Getting the scaled bitmap of the source image
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        Bitmap scaledBitmap = getScaledBitmap(bitmap, BORDER);

        chunkHeight = scaledBitmap.getHeight() / rows;
        chunkWidth = scaledBitmap.getWidth() / cols;

        for (int x = 0, yCoord = 0; x < rows; x++, yCoord += chunkHeight) {
            for (int y = 0, xCoord = 0; y < cols; y++, xCoord += chunkWidth) {
                chunkedImages.add(new ImageTile(x * cols + y, getResources(), Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight)));
            }
        }

        return chunkedImages;
    }

    /**
     * Properly scales the bitmap taking the border into account
     * @param bitmap
     * @return
     */
    private Bitmap getScaledBitmap(Bitmap bitmap, int BORDER){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        float scale = ((float)bitmap.getWidth()) / ((float)bitmap.getHeight());

        int xW, yW, xS, yS;
        xS = size.x - (dif.getX() * BORDER);
        yS = size.y - (dif.getY() * BORDER);

        //remove border sizes from total
        xS -= Schuifpuzzel.convertDpToPixel(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin), this) * 2;
        yS -= Schuifpuzzel.convertDpToPixel(getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin), this) * 2;

        if (xS > yS){
            //horizontal
            yW = yS;
            xW = (int)Math.floor(yS * scale);
        } else {
            //vertical
            yW = (int)Math.floor(xS * scale);
            xW = xS;
        }

        if (xW > xS){
            yW = (int)Math.round((xW - xS) * scale);
            xW = xS;
        } else if (yW > yS){
            xW = (int)Math.round((yW - yS) * scale);
            yW = yS;
        }

        Log.d("MAD", "xS: " + xS + " yS: " + yS + " xW: " + xW + " yW: " + yW + " size.x: " + size.x + " size.y: " + size.y );
        return Bitmap.createScaledBitmap(bitmap, xW, yW, true);
    }
}
