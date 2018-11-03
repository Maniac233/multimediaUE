package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.Background;
import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.Bullet;
import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.IngameObjects;
import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.Net;

/**
 * This is the Android view which displays our game contents.
 *
 * In the <code>surfaceCreated</code> event, we instantiate and start the separate game loop thread.
 * The thread lives and dies together with the view.
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop loop; // game logic implementation
    private Thread loopThread; // thread which runs the game loop

    // on-screen entities
    private Background background; // background graphics implementation
    private IngameObjects ingameObjects; // foreground objects
    private Paint paint; // draw utility

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);

        // Initialize the assets:
        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        ingameObjects = new IngameObjects(getResources());
        paint = new Paint();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        loop = new GameLoop(holder, this, ingameObjects);
        loopThread = new Thread(loop);
        loopThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * Handler for touch input.
     *
     * The game accepts two kinds of input: continuous touches for movement and taps for net-casting.
     *
     * @param e The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            loop.inputTouch(e.getX(), e.getY());
        }
        return true;
    }

    /**
     * Draw everything to the target canvas.
     *
     * This method is called from the game loop whenever we have the time to do the drawing.
     *
     * @param canvas draw target
     * @param interpolation fraction [0 ~ 1.] of an update that has elapsed since the last <code>update()</code>
     */
    public void drawAll(Canvas canvas, float interpolation) {
        // Draw the bitmaps:
        background.draw(canvas, paint);
        ingameObjects.getHud().draw(canvas, paint);
        ingameObjects.getPlayer().draw(canvas, paint);
        Net net = ingameObjects.getNet();
        if(net != null) {
            net.draw(canvas, paint);
        }

        for (Bullet bullet : ingameObjects.getBullets()) {
            bullet.draw(canvas, paint);
        }
    }
}
