package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.R;

/**
 * Container for all objects to be displayed on the screen while we are running the game loop.
 *
 * We currently implement only one net at a time. To be changed based on gameplay tests.
 */
public class IngameObjects {
    private Hud hud;
    private Player player;
    private Net net;
    private List<Bullet> bullets;

    private Resources resources;
    private Bitmap netSprite;
    private Bitmap bulletSprite;

    public IngameObjects(Resources resources) {
        hud = new Hud(700, 70, BitmapFactory.decodeResource(resources, R.drawable.hud));
        player = new Player(500.f, 700.f, 3.f, BitmapFactory.decodeResource(resources, R.drawable.player));
        net = null;
        Bitmap bulletSprite = BitmapFactory.decodeResource(resources, R.drawable.bullet);
        bullets = new ArrayList<Bullet>();

        this.resources = resources;
        this.netSprite = BitmapFactory.decodeResource(resources, R.drawable.net);
        this.bulletSprite = BitmapFactory.decodeResource(resources, R.drawable.bullet);
    }

    public Hud getHud() {
        return hud;
    }

    public Player getPlayer() {
        return player;
    }

    public Net getNet() {
        return net;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    /**
     * Temporary method to put interesting stuff on the screen.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void spawnStuff(float x, float y)
    {
        net = new Net((int)x, (int)y, 200.f, netSprite, 5000);

        for(int i = 0; i < 6; i++) {
            Bullet bullet = new Bullet(500.f, 900.f, bulletSprite,
                    (float)(Math.PI / 3. * i), .6f, .01f, .01f);
            bullets.add(bullet);
        }
    }
}
