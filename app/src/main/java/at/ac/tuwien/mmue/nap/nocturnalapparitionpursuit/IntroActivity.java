package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class IntroActivity extends AppCompatActivity {

    private boolean fromMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Intent intent = getIntent();
        fromMenu = intent.getBooleanExtra("fromMenu", false);
    }

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
