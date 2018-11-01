package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameLoop implements Runnable {
    SurfaceHolder holder;
    SurfaceView view;
    boolean running = true;

    public GameLoop(SurfaceHolder holder, SurfaceView view) {
        this.holder = holder;
        this.view = view;
    }

    public void run(){
        //
    }

    private void update() {

    }
}
