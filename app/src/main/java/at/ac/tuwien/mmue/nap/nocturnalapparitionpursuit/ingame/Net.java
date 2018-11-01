package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Net {
    private int positionX; //! X coordinate ranging from [0..1000]
    private int positionY; //! Y coordinate ranging from [0..1778]
    private float size; //! Scale of covered area
    private Bitmap sprite; //! source pixel data
    private int lifetime; //! Leftover time to live
    private int state; //! current state of net

    public Net(int positionX, int positionY, float size, Bitmap sprite, int lifetime) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.size = size;
        this.sprite = sprite;
        this.lifetime = lifetime;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void draw(Canvas canvas, Paint paint) {
        float drawLeft = positionX * canvas.getWidth() / 1000.f - sprite.getWidth() / 2.f;
        float drawTop = positionY * canvas.getHeight() / 1778.f - sprite.getHeight() / 2.f;
        canvas.drawBitmap(sprite, drawLeft, drawTop, paint);
    }
}
