/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.examples;

import rlforj.los.*;
import rlforj.math.Point;

import java.util.List;
import java.util.Random;

public class LosExample
{
    final static int width  = 17;
    final static int height = 17;

    public static void main(final String[] args)
    {
        final ExampleBoard b    = new ExampleBoard(width, height);
        final Random       rand = new Random();
        for (int i = 0; i < 30; i++)
        {
            b.setObstacle(rand.nextInt(width), rand.nextInt(height));
        }
        final int x1 = rand.nextInt(width);
        final int y1 = rand.nextInt(height);
        b.invisibleFloor = '.';
        b.invisibleWall = '#';

        displayLos(new ShadowCasting(), "Shadowcasting", b, x1, y1);
        displayLos(new PrecisePermissive(), "Precise Permissive", b, x1, y1);
        displayLos(new BresLos(false), "Bresenham", b, x1, y1);
        final BresLos bl = new BresLos(true);
        displayLos(bl, "Symmetric Bresenham", b, x1, y1);
        displayLos(new BresOpportunisticLos(), "Opportunistic Bresenham", b, x1, y1);
    }

    /**
     * @param a        algorithm instance
     * @param algoName The name of the algorithm
     * @param b        board
     * @param x1       x position
     * @param y1       y position
     */
    private static void displayLos(final ILosAlgorithm a, final String algoName, final ExampleBoard b, final int x1,
                                   final int y1)
    {
        final boolean     los;
        final List<Point> path;
        b.resetVisitedAndMarks();
        System.out.println(algoName);
        los = a.exists(b, width / 2, height / 2, x1, y1, true);

        path = a.getPath();
        markProjectPath(b, path);
        if (los)
            b.mark(x1, y1, '*');
        else
            b.mark(x1, y1, '?');

        System.out.println("Los " + (los ? "exists" : "does not exist"));
        b.print(width / 2, height / 2);
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
