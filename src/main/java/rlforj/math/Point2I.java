package rlforj.math;

/**
 * A class encapsulating a 2D point, as integers
 * <p>
 * (Reason for existance: java.awt.Point uses double
 * and I wanted speed.)
 *
 * @author sdatta
 */
public class Point2I
{
    public int x;
    public int y;

    public Point2I(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }

    public String toString()
    {
        return "Point2I[" + x + ", " + y + "]";
    }

    public int distance(final Point2I p)
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

        final Point2I point2I = (Point2I) o;

        if (x != point2I.x)
            return false;
        return y == point2I.y;
    }

    @Override
    public int hashCode()
    {
        return x << 7 - x + y;//x*prime+y
    }
}
