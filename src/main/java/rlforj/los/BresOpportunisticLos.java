package rlforj.los;

import rlforj.math.Point;
import rlforj.util.BresenhamLine;

import java.util.List;
import java.util.Vector;

/**
 * Bresenham LOS.
 * Tries to reach destination along first path. If
 * obstacled, shifts to alternate path. If that is blocked,
 * shift to first path again. Fails only if both are blocked
 * at a point.
 *
 * @author sdatta
 */
public class BresOpportunisticLos implements ILosAlgorithm
{

    private Vector<Point> path;

    public boolean existsLineOfSight(final ILosBoard b, final int startX, final int startY, final int x1, final int y1, final boolean calculateProject)
    {
        final int dx  = startX - x1;
        final int dy  = startY - y1;
        final int adx = dx > 0 ? dx : -dx;
        final int ady = dy > 0 ? dy : -dy;
        final int len = (adx > ady ? adx : ady) + 1;

        if (calculateProject)
            path = new Vector<>(len);

        final int[] px  = new int[len];
        final int[] py  = new int[len];
        int[]       px1, py1;
        px1 = new int[len];
        py1 = new int[len];

        //Compute both paths
        BresenhamLine.plot(startX, startY, x1, y1, px, py);
        BresenhamLine.plot(x1, y1, startX, startY, px1, py1);

        boolean los           = false;
        boolean alternatePath = false;
        for (int i = 0; i < len; i++)
        {
            // Have we reached the end ? In that case quit
            if (px[i] == x1 && py[i] == y1)
            {
                if (calculateProject)
                {
                    path.add(new Point(px[i], py[i]));
                }
                los = true;
                break;
            }
            // if we are on alternate path, is the path clear ?
            if (alternatePath && !b.blocksLight(px1[len - i - 1], py1[len - i - 1]))
            {
                if (calculateProject)
                    path.add(new Point(px1[len - i - 1], py1[len - i - 1]));
                continue;
            }
            else
                alternatePath = false;//come back to ordinary path

            //if on ordinary path, or alternate path was not clear
            if (!b.blocksLight(px[i], py[i]))
            {
                if (calculateProject)
                {
                    path.add(new Point(px[i], py[i]));
                }
                continue;
            }
            //if ordinary path wasnt clear
            if (!b.blocksLight(px1[len - i - 1], py1[len - i - 1]))
            {
                if (calculateProject)
                    path.add(new Point(px1[len - i - 1], py1[len - i - 1]));
                alternatePath = true;//go on alternate path
                continue;
            }
            if (calculateProject)
                path.add(new Point(px1[len - i - 1], py1[len - i - 1]));
            break;
        }

        return los;
    }

    public List<Point> getProjectPath()
    {
        return path;
    }

}
