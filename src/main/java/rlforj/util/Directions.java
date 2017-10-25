package rlforj.util;

import rlforj.math.Point;

/**
 * A class for various directions, their offsets.
 *
 * @author sdatta
 */
public enum Directions
{
    NORTH,
    WEST,
    SOUTH,
    EAST,
    NE,
    NW,
    SE,
    SW;
    public static final int[] dx = { 0, -1, 0, 1, 1, -1, 1, -1 }, dy = { 1, 0, -1, 0, 1, 1, -1, -1 };

    /**
     * The N4 neighbourhood, in clockwise order
     * NORTH, WEST, SOUTH, EAST
     */
    public static final Directions[] N4 = { NORTH, WEST, SOUTH, EAST };

    /**
     * The N8 neighbourhood, in clockwise order
     * NORTH, NW, WEST, SW, SOUTH, SE, EAST, NE
     */
    public static final Directions[] N8 = { NORTH, NW, WEST, SW, SOUTH, SE, EAST, NE };

    /**
     * Get the Ith direction.
     *
     * @param i
     * @return
     */
    public static Directions getDirection(int i)
    {
        return values()[i];
    }

    /**
     * Get the offsets in a Point object
     *
     * @return
     */
    public Point getDir()
    {
        return new Point(dx[this.ordinal()], dy[this.ordinal()]);
    }

    /**
     * The x offset
     *
     * @return
     */
    public int dx()
    {
        return dx[this.ordinal()];
    }

    /**
     * The y offset
     *
     * @return
     */
    public int dy()
    {
        return dy[this.ordinal()];
    }
}
