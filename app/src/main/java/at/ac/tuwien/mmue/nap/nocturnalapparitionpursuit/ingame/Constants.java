package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame;

/**
 * Defines static values which affect the core gameplay.
 */
public final class Constants {

    public static final int TICKS_PER_SECOND = 50; // game logic updates per second
    public static final int BOARD_WIDTH = 1000; // device-independent horizontal resolution of the game board
    public static final int BOARD_HEIGHT = 1700; // device-independent vertical resolution of the game board

    public static final int JUMP_WAIT = 3 * TICKS_PER_SECOND; // time after jump for which jump is prohibited
    public static final float NET_SIZE = 300.f; // diameter of net collision area
    public static final int NET_DURATION = 2 * TICKS_PER_SECOND; // life time of net after cast

}
