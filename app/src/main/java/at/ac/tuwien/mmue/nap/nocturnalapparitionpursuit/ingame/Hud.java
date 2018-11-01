package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Hud {
    private int positionX; //! X coordinate ranging from [0..1000]
    private int positionY; //! Y coordinate ranging from [0..1778]
    private Bitmap sprite; //! source pixel data
    private int nets; //! Count of nets left for player
    private int points; //! Displayed score counter
    private int levelnr; //! Level number

    public Hud(int positionX, int positionY, Bitmap sprite) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.sprite = sprite;
        this.nets = 3;
        this.points = 0;
        this.levelnr = 0;
    }

    public int getNets() {
        return nets;
    }

    public void setNets(int nets) {
        this.nets = nets;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getLevelnr() {
        return levelnr;
    }

    public void setLevelnr(int levelnr) {
        this.levelnr = levelnr;
    }

    public void draw(Canvas canvas, Paint paint) {
        float drawLeft = positionX * canvas.getWidth() / 1000.f - sprite.getWidth() / 2.f;
        float drawTop = positionY * canvas.getHeight() / 1778.f - sprite.getHeight() / 2.f;
        canvas.drawBitmap(sprite, drawLeft, drawTop, paint);
    }
}
