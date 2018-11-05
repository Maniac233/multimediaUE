package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.Bullet;
import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.Constants;
import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.IngameObjects;
import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.Net;
import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.Player;

/**
 * This is our implementation of the game logic.
 *
 * Run in a separate thread from the {@link GameSurfaceView}, the loop runs and draws until
 * it is stopped with <code>setRunning(false)</code>.
 */
public class GameLoop implements Runnable {

    private final static String TAG = MenuActivity.class.getSimpleName(); // for logging

    private SurfaceHolder holder; // used for canvas synchronization
    private GameSurfaceView view; // the view can draw the game
    private IngameObjects ingameObjects; // container for player, bullets etc.
    private boolean running; // we quit when this flag gets set to false

    /**
     * Enumeration of the kinds of input that we can receive from the player.
     */
    public enum InputType { MOVE, HALT, JUMP };

    /**
     * This represents one user input coming from the GUI thread.
     *
     * We keep these inputs in a thread-safe queue to be picked up and handled by the loop thread.
     * This means that we do not directly manipulate any game objects from the GUI thread(s),
     * which would require additional synchronization efforts.
     */
    private class QueuedInput {
        InputType type;
        public float x; // x-coordinate of screen touch
        public float y; // y-coordinate of screen touch

        public QueuedInput(InputType type, float x, float y) {
            this.type = type;
            this.x = x;
            this.y = y;
        }
    }

    private LinkedBlockingQueue<QueuedInput> inputs; // pending inputs

    private long levelTime; // level tick counter; will move to the dedicated level class
    private int jumpCooldown; // remaining time for the player until jump allowed

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

        this.levelTime = 0;
        this.jumpCooldown = 0;
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

        this.levelTime = 0;

        // Better game loop inspired by http://www.koonsolo.com/news/dewitters-gameloop/
        final int SKIP_TICKS = 1000 / Constants.TICKS_PER_SECOND;
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
     * Enqueue the player move input in the loop's input queue.
     *
     * The player character will move in the direction of the given coordinates until it reaches
     * them or until <code>inputHalt()</code> is called.
     *
     * Inputs from the queue will be woven into the game at the next update from the loop's
     * core to enable thread safety.
     *
     * @param x move target x-coordinate
     * @param y move target y-coordinate
     */
    public void inputMove(float x, float y) {
        try {
            inputs.put(new QueuedInput(InputType.MOVE, x, y));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enqueue the player halt input in the loop's input queue.
     *
     * The player character will stop moving around until <code>inputMove()</code> is called again.
     *
     * Inputs from the queue will be woven into the game at the next update from the loop's
     * core to enable thread safety.
     */
    public void inputHalt() {
        try {
            inputs.put(new QueuedInput(InputType.HALT, 0, 0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enqueue the player jump input in the loop's input queue.
     *
     * The player character will jump to the given coordinates if the game rules allow it.
     * A net will spawn at the previous location and capture the bullets in the vicinity.
     * Note that this is only the player's request to jump. If it is prohibited e.g. by
     * a cooldown, the jump will not happen.
     *
     * Inputs from the queue will be woven into the game at the next update from the loop's
     * core to enable thread safety.
     *
     * @param x jump target x-coordinate
     * @param y jump target y-coordinate
     */
    public void inputJump(float x, float y) {
        try {
            inputs.put(new QueuedInput(InputType.JUMP, x, y));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pop inputs from the loop-internal queue and trigger the game logic for handling them.
     */
    private void handleInputs() {
        QueuedInput input = null;
        Player player = ingameObjects.getPlayer();

        while((input = inputs.poll()) != null) {
            switch(input.type) {
                case MOVE:
                    player.moveTowards(input.x, input.y);
                    break;

                case HALT:
                    player.haltMovement();
                    break;

                case JUMP:
                    if(jumpCooldown <= 0) {
                        player.jumpTo(input.x, input.y);
                        jumpCooldown = Constants.JUMP_WAIT;
                    }
                    break;
            }
//            ingameObjects.spawnStuff(input.x, input.y);
        }
    }

    /**
     * Run onehis entails updating the positions etc. of ingame objects.
     */
    private void updateGame() {
        // Update game logic
        Net net = ingameObjects.getNet();
        if(net != null) {
            net.setLifetime(ingameObjects.getNet().getLifetime() - 1);
        }

        ingameObjects.getPlayer().update();

        List<Bullet> bullets = ingameObjects.getBullets();
        for(Bullet bullet : bullets) {
            bullet.update();
        }

        // clear offscreen bullets
        bullets.removeIf((Bullet b) -> !b.isInBounds());

        if(jumpCooldown > 0)
            jumpCooldown--;

        updateLevel();
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

    /**
     * Run the per-update changes that define a level (i.e. spawn bullets).
     *
     * This method will be replaced by a dedicated level class.
     */
    private void updateLevel() {
        final int directions = 6; // how many bullets at once
        final int spawnInterval = 60; // after how many ticks to spawn the next bullet
        final int turnInterval = 120; // after how many ticks to spawn in the other rotation

        if(levelTime % spawnInterval == 0) {
            float clockwise = ((levelTime / turnInterval) % 2) * 2.f - 1.f;
            for(int i = 0; i < directions; i++) {
                float heading = (float)(2. * Math.PI / directions * i * clockwise);
                float turn = .01f * clockwise;
                ingameObjects.spawnBullet(Constants.BOARD_WIDTH / 2.f, Constants.BOARD_HEIGHT / 2.f,
                        heading, .6f, turn, .01f);
            }
        }

        levelTime++;
    }
}
