package net.brord.schuifpuzzel.images;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by Brord on 3/30/2015.
 */
public class MultiPlayerImageClickListener extends ImageClickListener implements View.OnClickListener {

    private ImageView oldEmpty;

    private boolean hasMoved;

    public MultiPlayerImageClickListener(ImageManager manager) {
        super(manager);
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
        ImageView empty = manager.getEmpty();

        if (oldClicked == null){
            if (!adjecentTo(img, empty)) return;

            //no oldClicked, so its a fresh move
            hasMoved = true;
            manager.swap(manager.getEmpty(), img);
            oldClicked = empty;
            moves++;
        } else {
            //were just going back, so we dont care where the user clicked!
            manager.swap(oldClicked, empty);
            oldClicked = null;
            moves--;
        }
    }
}
