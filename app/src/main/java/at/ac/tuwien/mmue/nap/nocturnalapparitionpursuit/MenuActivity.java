package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


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

    public void launchIntro(View v) {
        Intent introIntent = new Intent(this, IntroActivity.class);
        introIntent.putExtra("fromMenu", true);
        startActivity(introIntent);
    }
}
