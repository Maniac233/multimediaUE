package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity which runs the actual game.
 *
 * In its layout, we define a screen-filling {@link GameSurfaceView}.
 * The view starts the {@link GameLoop} in a separate thread, which then handles logic
 * and drawing and everything runs from there.
 */
public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}
