package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


/**
 * “Main screen” Activity which leads into other activities based on the user's selection.
 */
public class MenuActivity extends Activity {

    private final static String TAG = MenuActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Hello World!");
    }

    /**
     * User choice handler for (re-) playing the intro video.
     */
    public void launchIntro(View v) {
        Intent introIntent = new Intent(this, IntroActivity.class);
        introIntent.putExtra("fromMenu", true);
        startActivity(introIntent);
    }

    /**
     * User choice handler for starting the game.
     *
     * There is currently no level selection.
     */
    public void launchGame(View v) {
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
    }
}
