package net.brord.schuifpuzzel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Brord on 4/2/2015.
 */
public class ImageGridManager extends GridManager<ImageView>{

    private final int border;

    public ImageGridManager(int x, int y, int border, ViewGroup view) {
        super(x, y, view);
        this.border = border;
    }

    @Override
    ViewGroup.MarginLayoutParams getParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0,0,border,border);
        //params.gravity = 1;
        params.weight = 100f;
        return params;
    }

    @Override
    ImageView newView(Context c) {
        return new ImageView(c);
    }

    @Override
    ViewGroup getLayout(Context c) {
        return new LinearLayout(c);
    }
}
