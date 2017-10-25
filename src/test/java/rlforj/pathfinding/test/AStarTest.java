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

            final int       radius = rand.nextInt(50) + 20; // 20-70
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
                assertFalse("Path existed but A* failed", floodFillTest(m, pStart, pEnd, radius));
            }
        }
    }

    /**
     * FloodFill the board from point 1 and see if point2 is same color. If not,
     * points are not reachable from each other.
     *
     * @param mb
     * @param start
     * @param end
     * @param radius
     * @return
     */
    private boolean floodFillTest(final MockBoard mb, final Point2I start, final Point2I end, final int radius)
    {
        final int EMPTY = 0, FULL = 1, COLOR = 2;

        final int width = mb.getWidth();
        final int height = mb.getHeight();

        final int[][] board  = new int[width][];
        for (int i = 0; i < width; i++)
        {
            board[i] = new int[height];
            for (int j = 0; j < height; j++)
            {
                if (mb.isObstacle(i, j))
                    board[i][j] = FULL;
                else
                    board[i][j] = EMPTY;
            }
        }

        final ArrayList<Point2I> l = new ArrayList<>(width * height);
        l.add(start);
        while (!l.isEmpty())
        {
            final Point2I p1 = l.remove(l.size() - 1);
            for (final Directions d : Directions.N8)
            {
                final Point2I p2 = new Point2I(p1.x + d.dx(), p1.y + d.dy());
                if (start.distance(p2) >= radius || !mb.contains(p2.x, p2.y) || board[p2.x][p2.y] != EMPTY)
                    continue;

                board[p2.x][p2.y] = COLOR;
                l.add(p2);
            }
        }

        // if start and end point are both coloured, a path exists in the start-point radius
        return (board[start.x][start.y] == COLOR && board[end.x][end.y] == COLOR);
    }
}
