package net.brord.schuifpuzzel.images;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by Brord on 3/30/2015.
 */
public class ImageClickListener implements View.OnClickListener {

    protected ImageManager manager;
    protected ImageView oldClicked;

    protected boolean active = false;
    protected int moves = 0;

    public ImageClickListener(ImageManager manager) {
        this.manager = manager;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void onClick(View v) {
        ImageView img = (ImageView)v;
        if (!isActive()) return;
        if (!adjecentTo(img, manager.getEmpty())) return;

        moves++;
        manager.swap(manager.getEmpty(), img);
    }

    protected boolean adjecentTo(View first, ImageView view) {
        int firstNumer = ((ImageTile)first.getBackground()).getImageNumber();
        int viewNumer = ((ImageTile)view.getBackground()).getImageNumber();
        for (int x=0; x<manager.getX(); x++){
            for (int y=0; y<manager.getY(); y++){
                if (getTileNumber(manager.getView(x,y)) != viewNumer) continue;

                if (x > 0){if (getTileNumber(manager.getView(x-1,y)) == firstNumer) return true;}
                if (y > 0){if (getTileNumber(manager.getView(x,y-1)) == firstNumer) return true;}
                if (x < manager.getX()-1){if (getTileNumber(manager.getView(x+1,y)) == firstNumer) return true;}
                if (y < manager.getY()-1){if (getTileNumber(manager.getView(x,y+1)) == firstNumer) return true; }
            }
        }
        return false;
    }

    protected int getTileNumber(View v){
        if (!(v instanceof ImageView)) return -1;
        return ((ImageTile)(v.getBackground())).getImageNumber();
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }
}
