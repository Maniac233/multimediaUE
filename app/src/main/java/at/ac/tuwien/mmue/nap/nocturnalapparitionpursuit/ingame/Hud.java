package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * This is the information display for game counters.
 *
 * <p>
 * It displays the following information:
 * <ul>
 *     <li>number of nets that the player has left</li>
 *     <li>achieved/required number of bugs to catch in this level</li>
 *     <li>(in the future) current score</li>
 * </ul>
 * </p>
 *
 * <p>
 *     Currently, the HUD is implemented only as a placeholder image.
 * </p>
 */
public class Hud {
    private int positionX; // X coordinate ranging from [0..1000]
    private int positionY; // Y coordinate ranging from [0..1778]
    private Bitmap sprite; // source pixel data
    private int nets; // Count of nets left for player
    private int points; // Displayed score counter
    private int levelnr; // Level number

    /**
     * Place the new HUD at the given position with the given placeholder image.
     */
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

    /**
     * Draw the HUD on the canvas.
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
