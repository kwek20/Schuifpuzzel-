package net.brord.schuifpuzzel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Brord on 4/5/2015.
 */
public class WinScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winscreen);

        setMoves(getIntent().getIntExtra("moves", 0));
        setDifficulty((Difficulty)getIntent().getSerializableExtra("difficulty"));
        setImage(getIntent().getStringExtra("image"));
    }

    public void mainMenu(View v){
        Intent i = new Intent(this, Schuifpuzzel.class);
        startActivity(i);
    }

    private void setImage(String image) {
        try {
            ImageView v = ((ImageView)this.findViewById(R.id.image));
            v.setImageResource(R.mipmap.class.getField(image).getInt(null));
            v.setAlpha(127);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void setDifficulty(Difficulty difficulty) {
        ((TextView)this.findViewById(R.id.difficulty)).setText(getText(R.string.difficulty) + " " + getText(difficulty.getDifficulty()));
    }

    public void setMoves(int moves){
        ((TextView)this.findViewById(R.id.moves)).setText(getText(R.string.moves) + " " + moves);
    }

    private ImageView getImage(String name){
        try {
            Field image = R.mipmap.class.getField(name);
            ImageView iv = new ImageView(this);
            iv.setImageResource(image.getInt(null));
            return iv;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
