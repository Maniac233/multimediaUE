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
import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.Constants;
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

    private float tapX; // last touch down event x-coordinate
    private float tapY; // last touch down event y-coordinate
    private long tapTime; // time value of the last tap that we know of
    private final float TAP_DISTANCE = 10.f; // distance threshold between down and up that we consider a tap
    private final long TAP_TIME = 500; // time threshold between down and up that we consider a tap
    private final long DOUBLETAP_TIME = 500; // time threshold between two taps in a double-tap

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
     * This method translates Android touch events into game inputs and passes them to the {@link GameLoop}.
     *
     * @param e The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = 0.f;
        float y = 0.f;
        long time = 0;

        switch(e.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                x = e.getX();
                y = e.getY();
                loop.inputMove(screenToBoardX(x), screenToBoardY(y));
                return true;

            case MotionEvent.ACTION_DOWN:
                tapX = e.getX();
                tapY = e.getY();
                loop.inputMove(screenToBoardX(tapX), screenToBoardY(tapY));
                return true;

            case MotionEvent.ACTION_UP:
                loop.inputHalt();

                time = e.getEventTime();
                x = e.getX();
                y = e.getY();
                float distanceSquared = (x - tapX) * (x - tapX) + (y - tapY) * (y - tapY);
                if(time - e.getDownTime() <= TAP_TIME && distanceSquared <= TAP_DISTANCE * TAP_DISTANCE) {
                    if(time - tapTime <= DOUBLETAP_TIME) {
                        loop.inputJump(screenToBoardX(x), screenToBoardY(y));
                        loop.inputHalt();
                        tapTime = 0;
                    }
                    else {
                        tapTime = time;
                        tapX = x;
                        tapY = y;
                    }
                }
                return true;

            case MotionEvent.ACTION_POINTER_DOWN:
                if(1 == e.getActionIndex()) { // second touch
                    x = e.getX(1);
                    y = e.getY(1);
                    loop.inputJump(screenToBoardX(x), screenToBoardY(y));
                }
                return true;

        }

        return false;
    }

    /**
     * Draw everything to the target canvas.
     *
     * This method is called from the game loop whenever we have the time to do the drawing.
     *
     * @param canvas draw target
     * @param interpolation fraction [0 ~ 1.] of an update that has elapsed since the last <code>update()</code>
     */
    public void drawAll(final Canvas canvas, float interpolation) {
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

        // DEBUG: for orientation we draw some lines at a constant distance from the screen border
        final float MARGIN_RED = 50.f;
        final float MARGIN_YELLOW = 25.f;

        // top lines
        paint.setColor(Color.RED);
        canvas.drawLine(MARGIN_RED * canvas.getWidth() / Constants.BOARD_WIDTH,
                MARGIN_RED * canvas.getHeight() / Constants.BOARD_HEIGHT,
                (Constants.BOARD_WIDTH - MARGIN_RED) * canvas.getWidth() / Constants.BOARD_WIDTH,
                MARGIN_RED * canvas.getHeight() / Constants.BOARD_HEIGHT,
                paint);
        paint.setColor(Color.YELLOW);
        canvas.drawLine(MARGIN_YELLOW * canvas.getWidth() / Constants.BOARD_WIDTH,
                MARGIN_YELLOW * canvas.getHeight() / Constants.BOARD_HEIGHT,
                (Constants.BOARD_WIDTH - MARGIN_YELLOW) * canvas.getWidth() / Constants.BOARD_WIDTH,
                MARGIN_YELLOW * canvas.getHeight() / Constants.BOARD_HEIGHT,
                paint);

        // left lines
        paint.setColor(Color.RED);
        canvas.drawLine(MARGIN_RED * canvas.getWidth() / Constants.BOARD_WIDTH,
                MARGIN_RED * canvas.getHeight() / Constants.BOARD_HEIGHT,
                MARGIN_RED * canvas.getWidth() / Constants.BOARD_WIDTH,
                (Constants.BOARD_HEIGHT - MARGIN_RED) * canvas.getHeight() / Constants.BOARD_HEIGHT,
                paint);
        paint.setColor(Color.YELLOW);
        canvas.drawLine(MARGIN_YELLOW * canvas.getWidth() / Constants.BOARD_WIDTH,
                MARGIN_YELLOW * canvas.getHeight() / Constants.BOARD_HEIGHT,
                MARGIN_YELLOW * canvas.getWidth() / Constants.BOARD_WIDTH,
                (Constants.BOARD_HEIGHT - MARGIN_YELLOW) * canvas.getHeight() / Constants.BOARD_HEIGHT,
                paint);

        // bottom lines
        paint.setColor(Color.RED);
        canvas.drawLine(MARGIN_RED * canvas.getWidth() / Constants.BOARD_WIDTH,
                (Constants.BOARD_HEIGHT - MARGIN_RED) * canvas.getHeight() / Constants.BOARD_HEIGHT,
                (Constants.BOARD_WIDTH - MARGIN_RED) * canvas.getWidth() / Constants.BOARD_WIDTH,
                (Constants.BOARD_HEIGHT - MARGIN_RED) * canvas.getHeight() / Constants.BOARD_HEIGHT,
                paint);
        paint.setColor(Color.YELLOW);
        canvas.drawLine(MARGIN_YELLOW * canvas.getWidth() / Constants.BOARD_WIDTH,
                (Constants.BOARD_HEIGHT - MARGIN_YELLOW) * canvas.getHeight() / Constants.BOARD_HEIGHT,
                (Constants.BOARD_WIDTH - MARGIN_YELLOW) * canvas.getWidth() / Constants.BOARD_WIDTH,
                (Constants.BOARD_HEIGHT - MARGIN_YELLOW) * canvas.getHeight() / Constants.BOARD_HEIGHT,
                paint);

        // right lines
        paint.setColor(Color.RED);
        canvas.drawLine((Constants.BOARD_WIDTH - MARGIN_RED) * canvas.getWidth() / Constants.BOARD_WIDTH,
                MARGIN_RED * canvas.getHeight() / Constants.BOARD_HEIGHT,
                (Constants.BOARD_WIDTH - MARGIN_RED) * canvas.getWidth() / Constants.BOARD_WIDTH,
                (Constants.BOARD_HEIGHT - MARGIN_RED) * canvas.getHeight() / Constants.BOARD_HEIGHT,
                paint);
        paint.setColor(Color.YELLOW);
        canvas.drawLine((Constants.BOARD_WIDTH - MARGIN_YELLOW) * canvas.getWidth() / Constants.BOARD_WIDTH,
                MARGIN_YELLOW * canvas.getHeight() / Constants.BOARD_HEIGHT,
                (Constants.BOARD_WIDTH - MARGIN_YELLOW) * canvas.getWidth() / Constants.BOARD_WIDTH,
                (Constants.BOARD_HEIGHT - MARGIN_YELLOW) * canvas.getHeight() / Constants.BOARD_HEIGHT,
                paint);
    }

    /**
     * Convert an x-coordinate from screen pixels to game board resolution.
     *
     * @return The x-coordinate on the game board
     */
    private float screenToBoardX(float x) {
        return x * Constants.BOARD_WIDTH / getWidth();
    }

    /**
     * Convert an y-coordinate from screen pixels to game board resolution.
     *
     * @return The y-coordinate on the game board
     */
    private float screenToBoardY(float y) {
        return y * Constants.BOARD_HEIGHT / getHeight();
    }

}
