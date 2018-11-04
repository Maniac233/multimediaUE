package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Represents the net that catches bullets on collision.
 *
 * The net is currently drawn as a placeholder sprite. In the future, it might include
 * circle primitives, effects etc.
 */
public class Net {
    private int positionX; // X coordinate
    private int positionY; // Y coordinate
    private float size; // Scale of covered area
    private Bitmap sprite; // source pixel data
    private int lifetime; // Leftover time to live
    private int state; // current state of net

    /**
     * Place the Net at the given position with the given placeholder image.
     */
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


    /**
     * Draw the Net on the canvas.
     *
     * @param canvas draw target
     * @param paint draw utility
     */
    public void draw(Canvas canvas, Paint paint) {
        float drawLeft = positionX * canvas.getWidth() / Constants.BOARD_WIDTH - sprite.getWidth() / 2.f;
        float drawTop = positionY * canvas.getHeight() / Constants.BOARD_HEIGHT - sprite.getHeight() / 2.f;
        canvas.drawBitmap(sprite, drawLeft, drawTop, paint);
    }
}
