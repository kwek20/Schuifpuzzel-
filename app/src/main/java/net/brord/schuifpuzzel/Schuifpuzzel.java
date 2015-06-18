package net.brord.schuifpuzzel;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Schuifpuzzel extends ActionBarActivity {

    ClickListener clickListener = new ClickListener(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //creating user
        ArrayList coords = new ArrayList<String>();
        coords.add("100");
        User user1 = new User("jan",coords,2);

        //saving it in the firebase
        FirebaseUsersCRUD usersFirebaseController = new FirebaseUsersCRUD(this);
        usersFirebaseController.createData(user1);

        //retrieving user info
//        Map<String,Object> userInfo = usersFirebaseController.getAllUserData();
        String userInfo = usersFirebaseController.getAllUserData();
        Toast.makeText(Schuifpuzzel.this, "USER NAME IS: " + userInfo, Toast.LENGTH_LONG).show();

        setContentView(R.layout.activity_schuifpuzzel);

        loadDifficulty((RadioGroup) this.findViewById(R.id.radiogroup));
        loadDifficultyPreference();

        loadImages((ViewGroup) this.findViewById(R.id.images));

        Log.d("MAD", savedInstanceState + "");
    }

    private void loadDifficultyPreference() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String tag = getText(R.string.difficulty_pref) + "";
        if (sharedPref.contains(tag)){
            String diff = sharedPref.getString(tag, "");
            if (!diff.equals("")){
                RadioGroup group = (RadioGroup) this.findViewById(R.id.radiogroup);
                for (int i=0; i<group.getChildCount(); i++){
                    RadioButton b = (RadioButton)group.getChildAt(i);
                    if (b.getText().equals(diff) && !b.isChecked()){
                        group.check(b.getId());
                    }
                }
            }
        }
    }

    public int loadDifficulty(RadioGroup g){
        ViewGroup.LayoutParams BtnParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (Difficulty d : Difficulty.values()){
            RadioButton b = new RadioButton(this);
            b.setLayoutParams(BtnParam);
            b.setText(getText(d.getDifficulty()));
            b.setEnabled(true);
            g.addView(b);

            if (d.isDefault()){
                g.check(b.getId());
            }
        }

        return 1;
    }

    public int loadImages(ViewGroup view){

        ViewGroup.LayoutParams ImgParam = new ViewGroup.LayoutParams(convertDpToPixel(40, this), convertDpToPixel(40, this));
        ViewGroup.LayoutParams BtnParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Field[] afbeeldingResources = R.mipmap.class.getFields(); //of R.drawable.class.getFields();
        for (Field f : afbeeldingResources) {
            try {
                String name = f.getName();
                int resourceId = f.getInt(null);

                if (resourceId == R.mipmap.ic_launcher) continue;
                view.addView(getImageGroup(resourceId, name, ImgParam, BtnParam));
            } catch (Exception e) {
                Log.e("MAD","### OOPS", e);
            }
        }
        return afbeeldingResources.length;
    }

    private ViewGroup getImageGroup(int id, String name, ViewGroup.LayoutParams ImgParam, ViewGroup.LayoutParams BtnParam){
        ViewGroup layout = new LinearLayout(this);
        ImageView image = new ImageView(this);
        image.setImageResource(id);
        image.setLayoutParams(ImgParam);

        Button b = new Button(this);
        b.setLayoutParams(BtnParam);
        b.setText(name);
        b.setOnClickListener(clickListener);

        layout.addView(image);
        layout.addView(b);

        return layout;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static int convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }
}
