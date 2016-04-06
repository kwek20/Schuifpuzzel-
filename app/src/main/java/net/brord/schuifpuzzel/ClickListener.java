package net.brord.schuifpuzzel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.brord.schuifpuzzel.enums.Difficulty;

/**
 * Created by Brord on 23-3-2015.
 */
public class ClickListener implements View.OnClickListener{

    private final Schuifpuzzel schuifPuzzel;

    public ClickListener(Schuifpuzzel schuifpuzzel) {
        this.schuifPuzzel = schuifpuzzel;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();

        Activity host = (Activity) v.getContext();
        RadioGroup radioGroup = (RadioGroup) host.findViewById(R.id.radiogroup);
        RadioButton button = (RadioButton) host.findViewById(radioGroup.getCheckedRadioButtonId());

        SharedPreferences sharedPref = schuifPuzzel.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(schuifPuzzel.getString(R.string.difficulty_pref), button.getText() + "");
        editor.commit();

        i.putExtra("difficulty", Difficulty.findByName(host, button.getText() + ""));
        i.putExtra("image", ((Button)v).getText());

        host.setResult(Activity.RESULT_OK,i);
        host.finish();
    }
}
