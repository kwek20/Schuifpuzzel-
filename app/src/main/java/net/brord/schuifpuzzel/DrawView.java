package net.brord.schuifpuzzel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import net.brord.schuifpuzzel.POD.DrawData;
import net.brord.schuifpuzzel.interfaces.DrawListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Brord on 4/9/2016.
 * Source: http://stackoverflow.com/questions/16650419/draw-in-canvas-by-finger-android
 *
 */
public class DrawView extends View {
    private boolean disabled = true;

    private Bitmap mBitmap;
    private Canvas  mCanvas;
    private Path mPath;

    Context context;

    private Paint mBitmapPaint;
    private Paint circlePaint;
    private Paint mPaint;

    private Path circlePath;

    private java.util.List<DrawData> storedData;
    private DrawListener listener;

    public DrawView(Context c, DrawListener listener) {
        super(c);
        context=c;

        this.listener = listener;
        storedData = new LinkedList<>();

        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath( mPath,  mPaint);
        canvas.drawPath( circlePath,  circlePaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (disabled) return false;

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        circlePath.reset();
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();

        //TODO send drawing data update!
    }

    public void loadFromData(java.util.List<DrawData> data){
        for (DrawData d : data){
            if (d.getStarted()){
                touch_start(d.getX(), d.getY());
            } else if (d.getEnded()){
                touch_up();
            } else {
                touch_move(d.getX(), d.getY());
            }
        }
    }

    public void clearDrawing()
    {
        storedData.clear();
        setDrawingCacheEnabled(false);
        // don't forget that one and the match below,
        // or you just keep getting a duplicate when you save.

        onSizeChanged(getWidth(), getHeight(), getWidth(), getHeight());
        invalidate();

        setDrawingCacheEnabled(true);
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        setEnabled(!disabled);
    }

    public boolean isDisabled() {
        return disabled;
    }

    public List<DrawData> getStoredData() {
        return storedData;
    }
}
