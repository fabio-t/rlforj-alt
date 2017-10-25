package rlforj.los;

import rlforj.math.Point;
import rlforj.util.BresenhamLine;

import java.util.List;
import java.util.Vector;

/**
 * Bresenham LOS class.
 * Checks if a bresenham line can be drawn from
 * source to destination. If symmetric, also checks
 * the alternate Bresenham line from destination to
 * source.
 *
 * @author sdatta
 */
public class BresLos implements ILosAlgorithm
{

    boolean symmetricEnabled = false;

    private Vector<Point> path;

    public BresLos(boolean symmetric)
    {
        symmetricEnabled = symmetric;
    }

    public boolean existsLineOfSight(ILosBoard b, int startX, int startY, int x1, int y1, boolean calculateProject)
    {
        int dx  = startX - x1, dy = startY - y1;
        int adx = dx > 0 ? dx : -dx, ady = dy > 0 ? dy : -dy;
        int len = (adx > ady ? adx : ady) + 1;//Max number of points on the path.

        if (calculateProject)
            path = new Vector<Point>(len);

        // array to store path.
        int[] px = new int[len], py = new int[len];

        //Start to finish path
        BresenhamLine.plot(startX, startY, x1, y1, px, py);

        boolean los = false;
        for (int i = 0; i < len; i++)
        {
            if (calculateProject)
            {
                path.add(new Point(px[i], py[i]));
            }
            if (px[i] == x1 && py[i] == y1)
            {
                los = true;
                break;
            }
            if (b.blocksLight(px[i], py[i]))
                break;
        }
        // Direct path couldnt find LOS so try alternate path
        if (!los && symmetricEnabled)
        {
            int[] px1 = null, py1 = null;
            // allocate space for alternate path
            px1 = new int[len];
            py1 = new int[len];
            // finish to start path.
            BresenhamLine.plot(x1, y1, startX, startY, px1, py1);

            Vector<Point> oldpath = path;
            path = new Vector<Point>(len);
            for (int i = len - 1; i > -1; i--)
            {
                if (calculateProject)
                {
                    path.add(new Point(px1[i], py1[i]));
                }
                if (px1[i] == x1 && py1[i] == y1)
                {
                    los = true;
                    break;
                }
                if (b.blocksLight(px1[i], py1[i]))
                    break;
            }

            if (calculateProject)
                path = oldpath.size() > path.size() ? oldpath : path;
        }

        return los;
    }

    public List<Point> getProjectPath()
    {
        return path;
    }
}
