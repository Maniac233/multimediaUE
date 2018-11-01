package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Player {
    private int positionX; //! X coordinate ranging from [0..1000]
    private int positionY; //! Y coordinate ranging from [0..1778]
    private Bitmap sprite; //! source pixel data
    private int state; //! current state of player

    public Player(int positionX, int positionY, Bitmap sprite) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.sprite = sprite;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
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
