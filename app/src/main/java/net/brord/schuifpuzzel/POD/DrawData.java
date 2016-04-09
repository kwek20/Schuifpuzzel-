package net.brord.schuifpuzzel.POD;

/**
 * Created by Brord on 4/9/2016.
 */
public class DrawData {
    private float x, y;
    private Boolean started, ended;

    public DrawData(float x, float y, Boolean started, Boolean ended){
        this.x = x;
        this.y = y;
        this.started = started;
        this.ended = ended;
    }

    public DrawData(){
        //firebase contructor
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Boolean getStarted() {
        return started;
    }

    public Boolean getEnded() {
        return ended;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DrawData)) return false;
        DrawData d = (DrawData)o;
        return d.getEnded() == getEnded() && d.getStarted() == getStarted() && d.getX() == getX() && d.getY() == getY();
    }
}
