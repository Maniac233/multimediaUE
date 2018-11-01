package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Background {
    private Bitmap bitmap;

    public Background(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void draw(Canvas canvas, Paint paint) {
        Rect boundsRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        canvas.drawBitmap(bitmap, srcRect, boundsRect, paint);
    }
}
