package net.brord.schuifpuzzel.images;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.brord.schuifpuzzel.GridManager;

/**
 * Created by Brord on 4/2/2015.
 */
public class ImageGridManager extends GridManager<ImageView> {

    private final int border;

    public ImageGridManager(int x, int y, int border, ViewGroup view) {
        super(x, y, view);
        this.border = border;
    }

    @Override
    public ViewGroup.MarginLayoutParams getParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0,0,border,border);
        //params.gravity = 1;
        params.weight = 100f;
        return params;
    }

    @Override
    public ImageView newView(Context c) {
        return new ImageView(c);
    }

    @Override
    public ViewGroup getLayout(Context c) {
        return new LinearLayout(c);
    }
}
