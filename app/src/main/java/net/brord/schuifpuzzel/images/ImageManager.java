package net.brord.schuifpuzzel.images;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import net.brord.schuifpuzzel.enums.Difficulty;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Brord on 4/1/2015.
 */
public class ImageManager {

    private final ArrayList<ImageTile> images;
    private final ImageGridManager manager;
    private final Runnable callback;
    private int[] tileData;

    /**
     * Creates a manager for images in the tile puzzle game
     * @param manager The ImageGridManager
     * @param images The tiles ;)
     * @param callback The call we make when were finished!
     */
    public ImageManager(ImageGridManager manager, ArrayList<ImageTile> images, Runnable callback){
        this.manager = manager;
        this.images = images;
        this.callback = callback;
    }

    public ImageView getEmpty() {
        for (int x=0; x<manager.getX(); x++) {
            for (int y = 0; y < manager.getY(); y++) {
                if (manager.getView(x,y).getVisibility() == View.INVISIBLE) return manager.getView(x,y);
            }
        }
        return null; //shouldnt happen after we started
    }

    public void loadImage(Difficulty dif){
        loopOver(getMethod("loadImage"));
    }

    public boolean loadImage(View v, Integer num){
        if (v instanceof ImageView){
            //set background
            ((ImageView)v).setBackground(images.get(num));
           return true;
        }
        return false;
    }

    public void swap(ImageView first, ImageView second){
        Drawable temp = first.getBackground();
        int visibility = first.getVisibility();

        first.setBackground(second.getBackground());
        first.setVisibility(second.getVisibility());

        second.setBackground(temp);
        second.setVisibility(visibility);

        check();
    }

    public boolean check(){
        boolean c = loopOver(getMethod("checkMethod"));
        if (c){callback.run();}
        return c;
    }


    public boolean checkMethod(View v, Integer num){
        if (v instanceof ImageView && ((ImageView)v).getBackground() instanceof ImageTile) {
            return (((ImageTile) ((ImageView) v).getBackground()).getImageNumber() == num);
        }
        return false;
    }

    private boolean loopOver(Method m){
        int imageCount = 0;
        for (int x=0; x<manager.getX(); x++){
            for (int y=0; y<manager.getY(); y++){
                ImageView img = manager.getView(x,y);
                if (runMethod(m, img, imageCount)){
                    imageCount++;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public void shuffle(){
        ArrayList<ImageTile> images = new ArrayList<>(this.images);
        Collections.reverse(images);

        int imageCount = 0;
        for (int x=0; x<manager.getX(); x++) {
            for (int y = 0; y < manager.getY(); y++) {
                manager.getView(x,y).setBackground(images.get(imageCount));
                imageCount++;
            }
        }
    }

    public int[] getTileData() {
        int[] tileData = new int[images.size()];
        int imageCount = 0;
        for (int x=0; x<manager.getX(); x++) {
            for (int y = 0; y < manager.getY(); y++) {
                ImageTile tile = (ImageTile) manager.getView(x,y).getBackground();
                tileData[imageCount] = tile.getImageNumber();
                imageCount++;
            }
        }
        return tileData;
    }

    ///////////////////////////////
    //UTIL FOR REFLECTION FROM HERE
    ///////////////////////////////

    private boolean runMethod(Method m, View v, int imageCount){
        try {
            return (boolean)m.invoke(this, v, imageCount);
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return false;
    }

    private Method getMethod(String name){
        try {
            return this.getClass().getMethod(name, View.class, Integer.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    ///////////////////////////////
    //GETTERS AND SETTERS
    ///////////////////////////////

    public int getX() { return manager.getX();}

    public int getY() { return manager.getY();}

    public ImageView getView(int x, int y) {
        return manager.getView(x, y); }

    public String store() {
        String data = "";
        for (ImageTile t : images) {
            data+=t.getImageNumber() + " ";
        }

        for (int x=0; x<manager.getX(); x++) {
            for (int y = 0; y < manager.getY(); y++) {
                if (manager.getView(x,y).getVisibility() == View.INVISIBLE) data += x+","+y;
            }
        }

        return data;
    }

    public void loadDataFrom(int[] tileData) {
        this.tileData = tileData;
        int imgId = 0;
        for (int x=0; x<manager.getX(); x++) {
            for (int y = 0; y < manager.getY(); y++) {
                manager.getView(x,y).setBackground(images.get(tileData[imgId]));
                imgId++;
            }
        }
    }

    public void restore(String stored){
        Log.d("MAD", "restore: " + stored);
        for (String s : stored.split(" ")){

        }
    }
}
