package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.LinkedBlockingQueue;

import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.Bullet;
import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.IngameObjects;
import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.Net;

/**
 * This is our implementation of the game logic.
 *
 * Run in a separate thread from the {@link GameSurfaceView}, the loop runs and draws until
 * it is stopped with <code>setRunning(false)</code>.
 */
public class GameLoop implements Runnable {

    public final int TICKS_PER_SECOND = 50;

    private SurfaceHolder holder; // used for canvas synchronization
    private GameSurfaceView view; // the view can draw the game
    private IngameObjects ingameObjects; // container for player, bullets etc.
    private boolean running; // we quit when this flag gets set to false

    /**
     * This represents one user input coming from the GUI thread.
     *
     * We keep these inputs in a thread-safe queue to be picked up and handled by the loop thread.
     * This means that we do not directly manipulate any game objects from the GUI thread(s),
     * which would require additional synchronization efforts.
     */
    private class QueuedInput {
        public float x; // x-coordinate of screen touch
        public float y; // y-coordinate of screen touch

        public QueuedInput(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private LinkedBlockingQueue<QueuedInput> inputs; // pending inputs

    /**
     * Construct the runnable loop.
     *
     * @param holder used for canvas synchronization
     * @param view we will often call into the view to draw the game
     * @param ingameObjects container for player, bullets etc.
     */
    public GameLoop(SurfaceHolder holder, GameSurfaceView view, IngameObjects ingameObjects) {
        this.holder = holder;
        this.view = view;
        this.ingameObjects = ingameObjects;
        this.running = false;
        this.inputs = new LinkedBlockingQueue<>();
    }

    /**
     * Set the running flag.
     *
     * If the game loop is currently running and the flag gets changed to false, the loop will
     * exit at the next opportunity.
     *
     * @param running new value for the running flag
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Executes the game loop.
     *
     * This loop runs forever, unless someone (e.g. from another thread) shuts it down by calling
     * <code>setRunning(false)</code>.
     *
     * The loop draws the game as often as possible and updates it as often as necessary,
     * regardless of the real speed of the system.
     *
     * One current downside is that the loop uses as many resources as possible for drawing as
     * often as possible. As a future improvement, we might thing about yielding CPU time if the
     * drawing is already fluent.
     */
    @Override
    public void run() {
        this.running = true;

        // Better game loop inspired by http://www.koonsolo.com/news/dewitters-gameloop/
        final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
        final int MAX_FRAMESKIP = 5;

        // use monotonic clock
        // see https://developer.android.com/reference/android/os/SystemClock
        long next_game_tick = SystemClock.uptimeMillis();
        int loops;
        float interpolation;

        while( running ) {

            loops = 0;
            while( SystemClock.uptimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) {
                handleInputs();
                updateGame();

                next_game_tick += SKIP_TICKS;
                loops++;
            }

            interpolation = (float)( SystemClock.uptimeMillis() + SKIP_TICKS - next_game_tick )
                    / (float) SKIP_TICKS;
            drawGame(interpolation);
        }
    }

    /**
     * Enqueue one touch input in the loop's input queue.
     *
     * Inputs from the queue will be woven into the game at the next update from the loop's
     * core to enable thread safety.
     *
     * @param x x-coordinate of the input
     * @param y y-coordinate of the input
     */
    public void inputTouch(float x, float y) {
        try {
            inputs.put(new QueuedInput(x, y));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pop inputs from the loop-internal queue and trigger the game logic for handling them.
     */
    private void handleInputs() {
        QueuedInput input = null;

        while((input = inputs.poll()) != null) {
            ingameObjects.spawnStuff(input.x, input.y);
        }
    }

    /**
     * Run one tick of game logic. This entails updating the positions etc. of ingame objects.
     */
    private void updateGame() {
        // Update game logic
        Net net = ingameObjects.getNet();
        if(net != null) {
            net.setLifetime(ingameObjects.getNet().getLifetime() - 1);
        }

        for(Bullet bullet : ingameObjects.getBullets()) {
            bullet.update();
        }
    }

    /**
     * Instruct the view to render all game contents to the screen.
     *
     * We generally assume that this is the most time-consuming part of the game loop.
     *
     * @param interpolation fraction [0 ~ 1.] of an update that has elapsed since the last <code>update()</code>
     */
    private void drawGame(float interpolation) {
        Canvas canvas = null;
        try {
            canvas = holder.lockCanvas(null);
            synchronized (holder) {
                if(canvas != null) {
                    view.drawAll(canvas, interpolation);
                }
            }
        } finally {
            if(canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
