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

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop loop;
    private Thread loopThread;

    // on-screen entities
    private Background background;
    private IngameObjects ingameObjects;
    private Paint paint;

    //aus den Folion übernommen
    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);

        // Initialize the assets:
        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        ingameObjects = new IngameObjects(getResources());
        paint = new Paint();
    }

    //aus den Folien übernommen
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

    //aus Folien übernommen
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //ein Touch-Event wurde ausgelöst
        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            loop.inputTouch(e.getX(), e.getY());
        }
        return true;
    }

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
