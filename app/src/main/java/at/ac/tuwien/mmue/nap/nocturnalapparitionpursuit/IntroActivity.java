package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Activity which plays the intro video.
 *
 * If the activity was started from the menu, we always go back to the menu aftewards.
 * If the activity was started from launching the app, we go forward to a new menu.
 */
public class IntroActivity extends AppCompatActivity {

    private boolean fromMenu = false; // history helper flag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Intent intent = getIntent();
        fromMenu = intent.getBooleanExtra("fromMenu", false);
    }

    /**
     * End the intro video and go straight to the menu.
     *
     * Re-use a menu if we already have it. We do not ever want two menus on the history stack.
     */
    public void skipIntro(View v) {
        if(fromMenu) {
            finish();
        }
        else {
            Intent menuIntent = new Intent(this, MenuActivity.class);
            startActivity(menuIntent);
        }
    }
}
