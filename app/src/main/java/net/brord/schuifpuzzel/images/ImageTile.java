package net.brord.schuifpuzzel.images;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by Brord on 4/2/2015.
 */
public class ImageTile extends BitmapDrawable {

    private int imageNumber;

    public ImageTile(int imageNumber, Resources resources, Bitmap bitmap) {
        super(resources, bitmap);
        this.imageNumber = imageNumber;
    }

    public int getImageNumber() {
        return imageNumber;
    }
}
