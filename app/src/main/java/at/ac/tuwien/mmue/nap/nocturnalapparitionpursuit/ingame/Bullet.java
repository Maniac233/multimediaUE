package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Represents one of the catchable targets on the screen (fireflies, bugs, other things...?)
 *
 * Their concerted behavior defines the level's gameplay challenge.
 * Currently, behavior is only defined in very basic terms and can be expanded in the future.
 */
public class Bullet {
    private float positionX; // X coordinate ranging from [0..1000]
    private float positionY; // Y coordinate ranging from [0..1778]
    private Bitmap sprite; // source pixel data
    private Rect spriteRect; // draw source area

    public enum State { LIVE, DEAD };

    private State state; // current state of the bullet

    // ---- behavior ----
    private float heading; // movement direction [0..2pi]
    private float speed; // movement speed
    private float turn; // delta-heading to be applied to direction every update
    private float acceleration; // delta-speed

    /**
     * Construct the Bullet with basic behavior.
     *
     * @param positionX spawn position x-coordinate
     * @param positionY spawn position y-coordinate
     * @param sprite bullet image (heading should be *right*)
     * @param heading initial movement direction
     * @param speed initial move rate per game update
     * @param turn per-update absolute change to heading
     * @param acceleration per-update absolute change to speed
     */
    public Bullet(float positionX, float positionY, Bitmap sprite,
                  float heading, float speed, float turn, float acceleration) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.sprite = sprite;
        this.spriteRect = new Rect(0, 0, sprite.getWidth(), sprite.getHeight());
        this.state = State.LIVE;

        this.heading = heading;
        this.speed = speed;
        this.turn = turn;
        this.acceleration = acceleration;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public float getHeading() {
        return heading;
    }

    public void setHeading(float heading) {
        this.heading = heading;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getTurn() {
        return turn;
    }

    public void setTurn(float turn) {
        this.turn = turn;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * Return <code>true</code> if the bullet is in or just beyond the screen area.
     *
     * If the bullet strays outside this area, it will be removed from the game.
     *
     * @return whether the bullet is within a margin of the game board
     */
    public boolean isInBounds() {
        final float MARGIN = 100.f;

        return positionX > -MARGIN && positionX < Constants.BOARD_WIDTH + MARGIN &&
               positionY > -MARGIN && positionY < Constants.BOARD_HEIGHT + MARGIN;
    }

    /**
     * Execute a movement step according to the bullet's behavior.
     */
    public void update() {
        this.positionX += Math.cos(this.heading) * this.speed;
        this.positionY += Math.sin(this.heading) * this.speed;
        this.heading += this.turn;
        this.speed += this.acceleration;
    }

    /**
     * Draw the bullet on the canvas.
     *
     * @param canvas draw target
     * @param paint draw utility
     */
    public void draw(Canvas canvas, Paint paint) {
        float bulletSize = 50;
        float drawLeft = (positionX * canvas.getWidth() - bulletSize / 2.f) / Constants.BOARD_WIDTH;
        float drawTop = (positionY * canvas.getHeight() - bulletSize / 2.f) / Constants.BOARD_HEIGHT;
        Rect destRect = new Rect((int)drawLeft, (int)drawTop,
                (int)(drawLeft + bulletSize), (int)(drawTop + bulletSize));
        canvas.drawBitmap(sprite, spriteRect, destRect, paint);
    }
}
