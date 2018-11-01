package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.LinkedBlockingQueue;

import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.Bullet;
import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.IngameObjects;
import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame.Net;

public class GameLoop implements Runnable {

    public final int TICKS_PER_SECOND = 50;

    private SurfaceHolder holder;
    private GameSurfaceView view;
    private IngameObjects ingameObjects;
    private boolean running;

    private class QueuedInput {
        public float x;
        public float y;

        public QueuedInput(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private LinkedBlockingQueue<QueuedInput> inputs; // pending inputs

    public GameLoop(SurfaceHolder holder, GameSurfaceView view, IngameObjects ingameObjects) {
        this.holder = holder;
        this.view = view;
        this.ingameObjects = ingameObjects;
        this.running = false;
        this.inputs = new LinkedBlockingQueue<>();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Executes the game loop
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

    public void inputTouch(float x, float y) {
        try {
            inputs.put(new QueuedInput(x, y));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleInputs() {
        QueuedInput input = null;

        while((input = inputs.poll()) != null) {
            ingameObjects.spawnStuff(input.x, input.y);
        }
    }

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
