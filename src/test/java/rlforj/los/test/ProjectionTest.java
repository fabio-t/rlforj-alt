/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.los.test;

import org.junit.jupiter.api.Test;
import rlforj.los.ILosAlgorithm;
import rlforj.los.PrecisePermissive;
import rlforj.los.ShadowCasting;
import rlforj.math.Point;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The projection path must always start at the start point and be a
 * contiguous line. On unobstructed boards it must reach the end point.
 * <p>
 * Note: permissive algorithms can see targets through diagonal gaps
 * that no cell-to-cell path can pass, so on obstructed boards the
 * projection may legitimately stop early.
 */
public class ProjectionTest
{
    private void checkProjectionOnEmptyBoard(final ILosAlgorithm alg)
    {
        final TestBoard tb = new TestBoard(false);

        for (int x1 = 0; x1 <= 20; x1 += 5)
        {
            for (int y1 = 0; y1 <= 20; y1 += 5)
            {
                assertTrue(alg.exists(tb, 10, 10, x1, y1, true));

                final List<Point> path = alg.getPath();
                assertNotNull(path);
                assertEquals(new Point(10, 10), path.get(0), "path must start at the start point");
                assertEquals(new Point(x1, y1), path.get(path.size() - 1), "path must end at the end point");
            }
        }
    }

    private void checkProjectionsOnRandomBoards(final ILosAlgorithm alg)
    {
        final Random rand = new Random(42);

        for (int run = 0; run < 200; run++)
        {
            final TestBoard tb = new TestBoard(false);

            for (int i = 0; i < 50; i++)
            {
                tb.exception.add(new Point(rand.nextInt(21), rand.nextInt(21)));
            }

            final int x1 = rand.nextInt(21);
            final int y1 = rand.nextInt(21);

            if (!alg.exists(tb, 10, 10, x1, y1, true))
                continue;

            final List<Point> path = alg.getPath();
            assertNotNull(path, "path should never be null when savePath is true");
            assertFalse(path.isEmpty());
            assertEquals(new Point(10, 10), path.get(0), "path must start at the start point");

            Point last = null;
            for (final Point p : path)
            {
                if (last != null)
                {
                    assertTrue(last.distance(p) <= 1, "discontinuous path: " + last + " -> " + p);
                }
                last = p;
            }
        }
    }

    @Test
    public void testShadowCastingProjection()
    {
        checkProjectionOnEmptyBoard(new ShadowCasting());
        checkProjectionsOnRandomBoards(new ShadowCasting());
    }

    @Test
    public void testPrecisePermissiveProjection()
    {
        checkProjectionOnEmptyBoard(new PrecisePermissive());
        checkProjectionsOnRandomBoards(new PrecisePermissive());
    }
}
