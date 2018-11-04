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
        hud = new Hud(Constants.BOARD_WIDTH * 7 / 10, 70, BitmapFactory.decodeResource(resources, R.drawable.hud));
        player = new Player(Constants.BOARD_WIDTH / 2.f, Constants.BOARD_HEIGHT * .6f, 3.f, BitmapFactory.decodeResource(resources, R.drawable.player));
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
     * Add a single bullet to the level.
     *
     * @param positionX spawn position x-coordinate
     * @param positionY spawn position y-coordinate
     * @param heading initial movement direction
     * @param speed initial move rate per game update
     * @param turn per-update absolute change to heading
     * @param acceleration per-update absolute change to speed
     */
    public void spawnBullet(float positionX, float positionY, float heading, float speed,
                            float turn, float acceleration)
    {
        Bullet bullet = new Bullet(positionX, positionY, bulletSprite, heading, speed, turn, acceleration);
        bullets.add(bullet);
    }
}
