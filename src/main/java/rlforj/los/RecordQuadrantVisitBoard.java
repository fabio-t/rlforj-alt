package rlforj.los;

import rlforj.math.Point;

import java.util.HashSet;
import java.util.Set;

/**
 * A LOS board that records points that were visited, while using another
 * board to decide obstacles.
 *
 * @author sdatta
 */
public class RecordQuadrantVisitBoard implements ILosBoard, GenericCalculateProjection.VisitedBoard
{
    ILosBoard b;

    int sx, sy, sxy;

    int targetX, targetY;

    // int manhattanDist;
    Set<Point> visitedNotObs = new HashSet<>();

    boolean endVisited = false;

    boolean calculateProject;
    private final Point visitedCheck = new Point(0, 0);

    public RecordQuadrantVisitBoard(final ILosBoard b, final int sx, final int sy, final int dx, final int dy, final boolean calculateProject)
    {
        super();
        this.b = b;
        this.sx = sx;
        this.sy = sy;
        sxy = sx + sy;
        this.targetX = dx;
        this.targetY = dy;

        this.calculateProject = calculateProject;
    }

    public boolean contains(final int x, final int y)
    {
        return b.contains(x, y);
    }

    public boolean isObstacle(final int x, final int y)
    {
        return b.isObstacle(x, y);
    }

    @Override
    public boolean blocksLight(final int x, final int y)
    {
        return b.blocksLight(x, y);
    }

    @Override
    public boolean blocksStep(final int x, final int y)
    {
        return b.blocksStep(x, y);
    }

    public void visit(final int x, final int y)
    {
        //			System.out.println("visited "+x+" "+y);
        if (x == targetX && y == targetY)
            endVisited = true;
        if (calculateProject && !b.blocksLight(x, y))
        {
            int dx = x - sx;
            dx = dx > 0 ? dx : -dx;
            int dy = y - sy;
            dy = dy > 0 ? dy : -dy;
            visitedNotObs.add(new Point(dx, dy));
        }
        //DEBUG
        //		b.visit(x, y);
    }

    public boolean wasVisited(final int x, final int y)
    {
        visitedCheck.x = x;
        visitedCheck.y = y;
        return visitedNotObs.contains(visitedCheck);
    }

}
