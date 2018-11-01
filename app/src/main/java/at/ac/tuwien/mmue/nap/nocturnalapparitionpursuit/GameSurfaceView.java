package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    //aus den Folion übernommen
    public GameSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    //aus den Folien übernommen
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        GameLoop loop = new GameLoop(holder, this);
        Thread loopThread = new Thread(loop);
        loopThread.start();
    }

    //aus den Folion übernommen
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // SurfaceView wurde verändert(Größe, Format...)
        // z.B. Gameloop sauber beenden
    }

    //aus Folien übernommen
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //Surface wurde versteckt
    }

    //aus Folien übernommen
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //ein Touch-Event wurde ausgelöst
        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            //Was soll bei einer Berührung passieren?
        }
        return true;
    }

    //aus Folien übernommen
    @Override
    public void draw(Canvas c) {
        super.draw(c);

        //Paint erstellen
        Paint p = new Paint();
        p.setColor(Color.YELLOW);

        //Kreis zeichnen:
        c.drawCircle(20,20,50, p);
    }


}
