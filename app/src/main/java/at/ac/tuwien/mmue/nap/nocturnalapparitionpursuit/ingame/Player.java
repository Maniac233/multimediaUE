package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Represents the player character on the screen.
 */
public class Player {
    private float positionX; // X coordinate ranging from [0..1000]
    private float positionY; // Y coordinate ranging from [0..1778]
    private float targetX; // X coordinate where we want to go
    private float targetY; // Y coordinate where we want to go
    private float speed; // movement speed per update
    private Bitmap sprite; // source pixel data
    private int state; //! current state of player

    /**
     * Place the player at the given position with the given image.
     */
    public Player(float positionX, float positionY, float speed, Bitmap sprite) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.targetX = positionX;
        this.targetY = positionY;
        this.speed = speed;
        this.sprite = sprite;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    /**
     * Set the character's movement goal to the given coordinates.
     *
     * The character will automatically move towards the target position on every <code>update()</code>.
     *
     * @param targetX x-coordinate of where we want to be
     * @param targetY y-coordinate of where we want to be
     */
    public void moveTowards(float targetX, float targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    /**
     * Tell the character to stop moving on <code>update()</code>.
     */
    public void haltMovement() {
        this.targetX = positionX;
        this.targetY = positionY;
    }

    /**
     * Immediately change the character's position to the given coordinates.
     *
     * @param targetX jump target x-coordinate
     * @param targetY jump target y-coordinate
     */
    public void jumpTo(float targetX, float targetY)
    {
        this.positionX = targetX;
        this.positionY = targetY;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * Perform automatic movement of the player character.
     */
    public void update() {
        float headingX = targetX - positionX;
        float headingY = targetY - positionY;
        double heading = Math.atan2(headingY, headingX);
        double deltaX = Math.cos(heading) * speed;
        double deltaY = Math.sin(heading) * speed;

        // are we there yet?
        if(Math.abs(headingX) > Math.abs(deltaX)) { // no -> proceed
            positionX += deltaX;
            positionY += deltaY;
        }
        else { // yes -> go directly to target
            positionX = targetX;
            positionY = targetY;
        }
    }

    /**
     * Draw the player on the canvas.
     *
     * @param canvas draw target
     * @param paint draw utility
     */
    public void draw(Canvas canvas, Paint paint) {
        float drawLeft = positionX * canvas.getWidth() / 1000.f - sprite.getWidth() / 2.f;
        float drawTop = positionY * canvas.getHeight() / 1778.f - sprite.getHeight() / 2.f;
        canvas.drawBitmap(sprite, drawLeft, drawTop, paint);
    }
}
