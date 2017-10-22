package rlforj.math;

/**
 * A Eucleidian 2D line class represented by integers.
 *
 * @author Jonathan Duerig
 */
public class Line2I
{
    public Point2I near;

    public Point2I far;

    public Line2I(Point2I newNear, Point2I newFar)
    {
        near = newNear;
        far = newFar;
    }

    public Line2I(int x1, int y1, int x2, int y2)
    {
        near = new Point2I(x1, y1);
        far = new Point2I(x2, y2);
    }

    public boolean isBelow(final Point2I point)
    {
        return relativeSlope(point) > 0;
    }

    public boolean isBelowOrContains(final Point2I point)
    {
        return relativeSlope(point) >= 0;
    }

    public boolean isAbove(final Point2I point)
    {
        return relativeSlope(point) < 0;
    }

    public boolean isAboveOrContains(final Point2I point)
    {
        return relativeSlope(point) <= 0;
    }

    public boolean doesContain(final Point2I point)
    {
        return relativeSlope(point) == 0;
    }

    // negative if the line is above the point.
    // positive if the line is below the point.
    // 0 if the line is on the point.
    public int relativeSlope(final Point2I point)
    {
        return (far.y - near.y) * (far.x - point.x) - (far.y - point.y) * (far.x - near.x);
    }

    @Override
    public String toString()
    {
        return "( " + near + " -> " + far + " )";
    }

}
