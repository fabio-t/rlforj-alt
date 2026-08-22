/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.pathfinding.test;

import org.junit.jupiter.api.Test;
import rlforj.math.Point;
import rlforj.pathfinding.AStar;

import static org.junit.jupiter.api.Assertions.*;

public class AStarEdgeCaseTest
{
    private static final String OPEN_MAP = "          \n" +
                                           "          \n" +
                                           "          \n" +
                                           "          \n" +
                                           "          ";

    private static final String WALLED_MAP = "     #    \n" +
                                             "     #    \n" +
                                             "     #    \n" +
                                             "     #    \n" +
                                             "     #    ";

    @Test
    public void testStartEqualsEnd()
    {
        final MockBoard m    = new MockBoard(OPEN_MAP);
        final AStar     algo = new AStar(m, m.getWidth(), m.getHeight());

        final Point[] path = algo.findPath(3, 3, 3, 3);

        assertNotNull(path);
        assertEquals(1, path.length);
        assertEquals(new Point(3, 3), path[0]);
    }

    @Test
    public void testOutOfBoundsReturnsNull()
    {
        final MockBoard m    = new MockBoard(OPEN_MAP);
        final AStar     algo = new AStar(m, m.getWidth(), m.getHeight());

        assertNull(algo.findPath(-1, 0, 3, 3));
        assertNull(algo.findPath(0, 0, 30, 3));
    }

    @Test
    public void testNoPathThroughFullWall()
    {
        final MockBoard m    = new MockBoard(WALLED_MAP);
        final AStar     algo = new AStar(m, m.getWidth(), m.getHeight());

        assertNull(algo.findPath(0, 2, 9, 2));
    }

    @Test
    public void testEndOutsideRadiusReturnsNull()
    {
        final MockBoard m    = new MockBoard(OPEN_MAP);
        final AStar     algo = new AStar(m, m.getWidth(), m.getHeight());

        assertNull(algo.findPath(0, 0, 9, 4, 3));
    }

    @Test
    public void testZeroRadiusReturnsStart()
    {
        final MockBoard m    = new MockBoard(OPEN_MAP);
        final AStar     algo = new AStar(m, m.getWidth(), m.getHeight());

        final Point[] path = algo.findPath(2, 2, 8, 3, 0);

        assertNotNull(path);
        assertEquals(1, path.length);
        assertEquals(new Point(2, 2), path[0]);
    }

    @Test
    public void testNoDiagonalMovement()
    {
        final MockBoard m    = new MockBoard(OPEN_MAP);
        final AStar     algo = new AStar(m, m.getWidth(), m.getHeight(), false);

        final Point[] path = algo.findPath(0, 0, 4, 4);

        assertNotNull(path);
        assertEquals(9, path.length); // manhattan distance + 1

        Point last = null;
        for (final Point p : path)
        {
            if (last != null)
            {
                final int dx = Math.abs(p.x - last.x);
                final int dy = Math.abs(p.y - last.y);
                assertEquals(1, dx + dy, "diagonal step in a 4-way path: " + last + " -> " + p);
            }
            last = p;
        }
    }

    @Test
    public void testPathToObstacleEndPointIsAllowed()
    {
        // the end point is the only cell allowed to be an obstacle,
        // e.g. walking up to a closed door or a creature
        final MockBoard m    = new MockBoard("    \n" +
                                             "  # \n" +
                                             "    ");
        final AStar     algo = new AStar(m, m.getWidth(), m.getHeight());

        final Point[] path = algo.findPath(0, 1, 2, 1);

        assertNotNull(path);
        assertEquals(new Point(2, 1), path[path.length - 1]);
    }
}
