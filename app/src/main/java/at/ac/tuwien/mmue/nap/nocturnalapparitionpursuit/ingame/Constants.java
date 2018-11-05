package at.ac.tuwien.mmue.nap.nocturnalapparitionpursuit.ingame;

/**
 * Defines static values which affect the core gameplay.
 */
public final class Constants {

    public static final int TICKS_PER_SECOND = 50; // game logic updates per second
    public static final int BOARD_WIDTH = 1000; // device-independent horizontal resolution of the game board
    public static final int BOARD_HEIGHT = 1700; // device-independent vertical resolution of the game board

    public static final int JUMP_WAIT = 3 * TICKS_PER_SECOND; // time after jump for which jump is prohibited

}
