package rlforj.pathfinding.test;

import org.junit.Test;
import rlforj.math.Point2I;
import rlforj.pathfinding.AStar;
import rlforj.util.Directions;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class AStarTest
{

    /**
     * 1000 times
     * Build a random board. Pick 2 random points. Find a path. If a path is
     * returned, check that:
     * 1. It is a valid path (all points are adjacent to each other)
     * 2. No point on the path is an obstacle.
     * 3. If pathfindfind fails, floodfill the map startinf from the start point.
     * If endpoint is not the same color, path does not exist. Hence check
     * pathfinding failure.
     * <p>
     * Not tested:
     * 1. It is the shortest path.
     */
    @Test
    public void testAStarBasic()
    {
        final Random rand = new Random();
        for (int i = 0; i < 1000; i++)
        {
            final int w = rand.nextInt(80) + 20; //20 - 100
            final int h = rand.nextInt(80) + 20; //20 - 100

            final StringBuilder sb = new StringBuilder();
            // Create mockboard
            for (int k = 0; k < h; k++)
            {
                for (int j = 0; j < w; j++)
                    if (rand.nextInt(100) < 30)// 30% coverage
                        sb.append('#');
                    else
                        sb.append(' ');
                sb.append('\n');
            }
            final MockBoard m = new MockBoard(sb.toString());

            int startx = -1, starty = -1, endx = -1, endy = -1;

            startx = rand.nextInt(w);
            starty = rand.nextInt(h);
            endx = rand.nextInt(w);
            endy = rand.nextInt(h);

            final AStar algo = new AStar(m, w, h);

            final Point2I pStart = new Point2I(startx, starty);
            final Point2I pEnd   = new Point2I(endx, endy);

            // System.out.println("start: " + pStart);
            // System.out.println("end: " + pEnd);

            final int       radius = 50;
            final Point2I[] path   = algo.findPath(startx, starty, endx, endy, radius);
            if (path != null)
            {
                // Check path
                for (int pi = 0; pi < path.length; pi++)
                {
                    final Point2I step = path[pi];

                    if (pi == 0)
                        assertEquals("Path did not start with the starting point", step, pStart);
                    else if (pi == path.length - 1)
                        assertNotEquals("Last step of path was equal to the ending point", step, pEnd);
                    else
                        assertFalse("A point on A* path was an obstacle", m.isObstacle(step.x, step.y));
                }

                // Check continuity
                Point2I lastStep = null;
                for (final Point2I step : path)
                {
                    if (lastStep == null)
                    {
                        lastStep = step;
                        continue;
                    }

                    assertTrue("Discontinuous path in A*",
                               step.x - lastStep.x <= 1 && step.x - lastStep.x >= -1 && step.y - lastStep.y <= 1 &&
                               step.y - lastStep.y >= -1);

                    lastStep = step;
                }
            }
            else
            {
                assertFalse("Path existed but A* failed", floodFillTest(m, startx, starty, endx, endy, radius));
            }
        }
    }

    /**
     * FloodFill the board from point 1 and see if point2 is same color. If not,
     * points are not reachable from each other.
     *
     * @param mb
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private boolean floodFillTest(final MockBoard mb, final int x1, final int y1, final int x2, final int y2, final int radius)
    {
        final int     EMPTY  = 0, FULL = 1, COLOR = 2;

        final int width;
        final int height;
        final int minX, minY, maxX, maxY;

        if (radius <= 0)
        {
            width = mb.getWidth();
            height = mb.getHeight();

            minX = 0;
            minY = 0;
            maxX = width;
            maxY = height;
        }
        else
        {
            minX = Math.max(0, x1 - radius);
            minY = Math.max(0, y1 - radius);
            maxX = Math.min(mb.getWidth(), x1 + radius);
            maxY = Math.min(mb.getHeight(), y1 + radius);

            width = maxX - minX;
            height = maxY - minY;
        }

        final int[][] board  = new int[width][];
        for (int i = 0; i < width; i++)
        {
            board[i] = new int[height];
            for (int j = 0; j < height; j++)
            {
                if (mb.isObstacle(minX+i, minY+j))
                    board[i][j] = FULL;
                else
                    board[i][j] = EMPTY;
            }
        }

        final ArrayList<Point2I> l = new ArrayList<>(width * height);
        l.add(new Point2I(x1, y1));
        while (!l.isEmpty())
        {
            final Point2I p1 = l.remove(l.size() - 1);
            for (final Directions d : Directions.N8)
            {
                final Point2I p2 = new Point2I(p1.x + d.dx(), p1.y + d.dy());
                if (!mb.contains(p2.x, p2.y) || board[p2.x][p2.y] != EMPTY)
                    continue;

                board[p2.x][p2.y] = COLOR;
                l.add(p2);
            }
        }

        return (board[x1][y1] == board[x2][y2] && board[x1][y1] == COLOR);
    }
}
