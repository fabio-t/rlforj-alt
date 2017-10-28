package rlforj.math;

/**
 * A class encapsulating a 2D point, as integers.
 *
 * @author sdatta
 */
public class Point
{
    public int x;
    public int y;

    public Point(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }

    public String toString()
    {
        return "(" + x + "," + y + ")";
    }

    public int distance(final Point p)
    {
        return distance(p.x, p.y);
    }

    public int distance(final int x, final int y)
    {
        return Math.max(Math.abs(this.x - x), Math.abs(this.y - y));
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final Point point = (Point) o;

        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode()
    {
        return x << 7 - x + y;//x*prime+y
    }
}
