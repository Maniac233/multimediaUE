package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Bullet {
    private float positionX; //! X coordinate ranging from [0..1000]
    private float positionY; //! Y coordinate ranging from [0..1778]
    private Bitmap sprite; //! source pixel data
    private Rect spriteRect; //! draw source area
    private int state; //! current state of player

    // ---- behavior ----
    private float heading; //! movement direction [0..2pi]
    private float speed; //! movement speed
    private float turn; //! delta-heading to be applied to direction every update
    private float acceleration; //! delta-speed

    public Bullet(float positionX, float positionY, Bitmap sprite,
                  float heading, float speed, float turn, float acceleration) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.sprite = sprite;
        this.spriteRect = new Rect(0, 0, sprite.getWidth(), sprite.getHeight());

        this.heading = heading;
        this.speed = speed;
        this.turn = turn;
        this.acceleration = acceleration;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
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

    /// Execute a movement step
    public void update() {
        this.positionX += Math.cos(this.heading) * this.speed;
        this.positionY += Math.sin(this.heading) * this.speed;
        this.heading += this.turn;
        this.speed += this.acceleration;
    }

    public void draw(Canvas canvas, Paint paint) {
        float bulletSize = 50;
        float drawLeft = (positionX * canvas.getWidth() - bulletSize / 2.f) / 1000.f;
        float drawTop = (positionY * canvas.getHeight() - bulletSize / 2.f) / 1778.f;
        Rect destRect = new Rect((int)drawLeft, (int)drawTop,
                (int)(drawLeft + bulletSize), (int)(drawTop + bulletSize));
        canvas.drawBitmap(sprite, spriteRect, destRect, paint);
    }
}