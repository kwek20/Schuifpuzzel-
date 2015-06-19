package net.brord.schuifpuzzel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Brord on 6/18/2015.
 */
public class OpponentScreen extends ActionBarActivity {

    private static final String LOADER_TAG = "Loader";
    private String username;

    private LoaderDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_opponentscreen);
    }

    public void findOpponent(View v){
        if (!checkUsername())return;
        final EditText input = new EditText(this);
        new AlertDialog.Builder(OpponentScreen.this)
                .setTitle(R.string.find)
                .setMessage(getString(R.string.username))
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        Log.d("MAD", value.toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    public void createGame(View v){
        if (!checkUsername())return;


    }

    private boolean checkUsername() {
        TextView group = (TextView) findViewById(R.id.userName);

        if (!(username = group.getText().toString()).equals("test")) return true;
        new AlertDialog.Builder(OpponentScreen.this)
                .setTitle(R.string.error)
                .setMessage(getString(R.string.unavailable))
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        waitForNotification();
                    }
                }).show();
        return false;
    }

    public void waitForNotification() {
        (loader = new LoaderDialog()).show(getFragmentManager(), LOADER_TAG);
    }

    public void doneLoading() {

        if (loader != null && loader.getShowsDialog()) {
            loader.dismiss();
        }
    }

    public static class LoaderDialog extends DialogFragment {

        public LoaderDialog(){}

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {

            ProgressDialog _dialog = new ProgressDialog(getActivity());
            this.setStyle(STYLE_NO_TITLE, getTheme()); // You can use styles or inflate a view
            _dialog.setMessage(getString(R.string.searching)); // set your messages if not inflated from XML
            _dialog.setCancelable(false);

            return _dialog;
        }
    }
}
