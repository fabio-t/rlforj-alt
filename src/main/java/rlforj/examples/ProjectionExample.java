package rlforj.examples;

import rlforj.los.*;
import rlforj.math.Point;

import java.util.List;
import java.util.Random;

public class ProjectionExample
{

    public static void main(final String[] args)
    {
        final ExampleBoard b    = new ExampleBoard(21, 21);
        final Random       rand = new Random();
        for (int i = 0; i < 30; i++)
        {
            b.setObstacle(rand.nextInt(21), rand.nextInt(21));
        }
        final int x1 = rand.nextInt(21);
        final int y1 = rand.nextInt(21);
        b.invisibleFloor = '.';
        b.invisibleWall = '#';

        displayProjection(new ShadowCasting(), "Shadowcasting", b, x1, y1);
        displayProjection(new PrecisePermissive(), "Precise Permissive", b, x1, y1);
        displayProjection(new BresLos(false), "Bresenham", b, x1, y1);
        final BresLos bl = new BresLos(true);
        displayProjection(bl, "Symmetric Bresenham", b, x1, y1);
        displayProjection(new BresOpportunisticLos(), "Opportunistic Bresenham", b, x1, y1);
    }

    /**
     * @param a algorithm instance
     * @param algoName The name of the algorithm
     * @param b board
     * @param x1 x position
     * @param y1 y position
     */
    private static void displayProjection(final ILosAlgorithm a, final String algoName, final ExampleBoard b, final int x1, final int y1)
    {
        final boolean     los;
        final List<Point> path;
        b.resetVisitedAndMarks();
        System.out.println(algoName);
        los = a.existsLineOfSight(b, 10, 10, x1, y1, true);

        path = a.getProjectPath();
        markProjectPath(b, path);
        if (los)
            b.mark(x1, y1, '*');
        else
            b.mark(x1, y1, '?');

        System.out.println("Los " + (los ? "exists" : "does not exist"));
        b.print(10, 10);
    }

    private static void markProjectPath(final ExampleBoard b, final List<Point> path)
    {
        if (path.size() < 1)
            return;

        int lastx = path.get(0).x, lasty = path.get(0).y;
        for (int i = 1; i < path.size(); i++)
        {
            final Point p = path.get(i);
            final int   x = p.x;
            final int   y = p.y;
            if (x != lastx)
            {
                if (y != lasty)
                {
                    b.mark(x, y, ((x - lastx) * (y - lasty) > 0) ? '\\' : '/');
                }
                else
                    b.mark(x, y, '-');
            }
            else
                b.mark(x, y, '|');

            lastx = x;
            lasty = y;
        }
    }
}
