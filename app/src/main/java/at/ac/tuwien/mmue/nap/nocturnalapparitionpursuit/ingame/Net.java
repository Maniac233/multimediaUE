package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Represents the net that catches bullets on collision.
 *
 * The net is currently drawn as a placeholder sprite. In the future, it might include
 * circle primitives, effects etc.
 */
public class Net {
    private float positionX; // X coordinate
    private float positionY; // Y coordinate
    private float size; // Scale of covered area
    private Bitmap sprite; // source pixel data
    private int lifetime; // Leftover time to live

    public enum State { LIVE, DEAD };

    private State state; // current state of net

    /**
     * Place the Net at the given position with the given placeholder image.
     */
    public Net(float positionX, float positionY, float size, Bitmap sprite, int lifetime) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.size = size;
        this.sprite = sprite;
        this.lifetime = lifetime;
        this.state = State.LIVE;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void update() {
        if(lifetime > 0) {
            lifetime--;
            if(lifetime <= 0)
                state = State.DEAD;
        }
    }

    /**
     * Draw the Net on the canvas.
     *
     * @param canvas draw target
     * @param paint draw utility
     */
    public void draw(Canvas canvas, Paint paint) {
        if(State.LIVE == state) {
            paint.setAlpha(150);

            float drawX = positionX * canvas.getWidth() / Constants.BOARD_WIDTH;
            float drawY = positionY * canvas.getHeight() / Constants.BOARD_HEIGHT;
            float drawRadius = size * canvas.getHeight() / Constants.BOARD_HEIGHT;
            paint.setColor(Color.GREEN);
            canvas.drawCircle(drawX, drawY, drawRadius, paint);

//            Rect spriteRect = new Rect(0, 0, sprite.getWidth(), sprite.getHeight());
//            float drawLeft = (positionX - size) * canvas.getWidth() / Constants.BOARD_WIDTH;
//            float drawTop = (positionY - size) * canvas.getHeight() / Constants.BOARD_HEIGHT;
//            Rect destRect = new Rect((int)drawLeft, (int)drawTop,
//                    (int)(drawLeft + size * 2.f), (int)(drawTop + size * 2.f));
//            canvas.drawBitmap(sprite, spriteRect, destRect, paint);

            paint.setAlpha(255);
        }
    }
}
