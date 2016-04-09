package net.brord.schuifpuzzel.images;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by Brord on 3/30/2015.
 */
public class MultiPlayerImageClickListener implements View.OnClickListener {

    private ImageManager manager;
    private ImageView oldClicked;

    private boolean active = false;
    private int moves = 0;

    public MultiPlayerImageClickListener(ImageManager manager) {
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

    private boolean adjecentTo(View first, ImageView view) {
        int firstNumer = ((ImageTile)first.getBackground()).getImageNumber();
        int viewNumer = ((ImageTile)view.getBackground()).getImageNumber();
        for (int x=0; x<manager.getX(); x++){
            for (int y=0; x<manager.getY(); y++){
                if (getTileNumber(manager.getView(x,y)) != viewNumer) continue;

                if (x > 0){if (getTileNumber(manager.getView(x-1,y)) == firstNumer) return true;}
                if (y > 0){if (getTileNumber(manager.getView(x,y-1)) == firstNumer) return true;}
                if (x < manager.getX()-1){if (getTileNumber(manager.getView(x+1,y)) == firstNumer) return true;}
                if (y < manager.getY()-1){if (getTileNumber(manager.getView(x,y+1)) == firstNumer) return true; }
            }
        }
        return false;
    }

    private int getTileNumber(View v){
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
