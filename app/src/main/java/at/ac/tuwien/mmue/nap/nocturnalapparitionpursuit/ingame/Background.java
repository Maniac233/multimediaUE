package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Represents the fullscreen background graphics layer.
 *
 * In the future, the background might change depending on the level and might even be animated.
 */
public class Background {
    private Bitmap bitmap;

    /**
     * Construct the background to display only the given bitmap stretched to full screen.
     *
     * @param bitmap background image
     */
    public Background(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * Draw the background to the given canvas.
     *
     * @param canvas draw target
     * @param paint draw utility
     */
    public void draw(Canvas canvas, Paint paint) {
        Rect boundsRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        canvas.drawBitmap(bitmap, srcRect, boundsRect, paint);
    }
}
