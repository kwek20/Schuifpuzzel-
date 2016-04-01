package net.brord.schuifpuzzel.enums;

import android.app.Activity;

import net.brord.schuifpuzzel.R;

/**
 * Created by Brord on 23-3-2015.
 */
public enum Difficulty {
    EASY(R.string.easy, 3, 3, 3000), MEDIUM(R.string.medium, 4, 4, 3000, true), HARD(R.string.hard, 5, 5, 3000);

    private final int difficulty;
    private final int x, y;
    private final int duration;
    private boolean aDefault;

    Difficulty(int difficulty, int x, int y, int duration) {
        this(difficulty, x, y, duration, false);
    }

    Difficulty(int difficulty, int x, int y, int duration, boolean aDefault) {
        this.x = x;
        this.y = y;
        this.difficulty = difficulty;
        this.aDefault = aDefault;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return difficulty + "";
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isDefault() {
        return aDefault;
    }

    public static Difficulty findByName(Activity a, String difficulty) {
        for (Difficulty d : values()){
            if (a.getText(d.getDifficulty()).toString().equalsIgnoreCase(difficulty)) return d;
        }
        return null;
    }
}