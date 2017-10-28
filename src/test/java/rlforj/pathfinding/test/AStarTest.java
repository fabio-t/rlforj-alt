package rlforj.pathfinding.test;

import org.junit.Test;
import rlforj.math.Point;
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

            int startx, starty, endx, endy;

            // we want to check all possible cases of start and end point being, or not, obstacles
            final boolean startNoObstacle = rand.nextBoolean();
            final boolean endNoObstacle   = rand.nextBoolean();
            while (true)
            {
                startx = rand.nextInt(w);
                starty = rand.nextInt(h);
                endx = rand.nextInt(w);
                endy = rand.nextInt(h);

                if ((startNoObstacle && m.isObstacle(startx, starty)) ||
                    (!startNoObstacle && !m.isObstacle(startx, starty)))
                    break;

                if ((endNoObstacle && m.isObstacle(endx, endy)) || (!endNoObstacle && !m.isObstacle(endx, endy)))
                    break;
            }

            final AStar algo = new AStar(m, w, h);

            final Point pStart = new Point(startx, starty);
            final Point pEnd   = new Point(endx, endy);

            final int     radius = rand.nextInt(80) + 20; // 20-100
            final Point[] path   = algo.findPath(startx, starty, endx, endy, radius);
            if (path != null)
            {
                // Check path
                for (int pi = 0; pi < path.length; pi++)
                {
                    final Point step = path[pi];

                    if (pi == 0)
                        assertEquals("Path did not start with the starting point", step, pStart);
                    else if (pi == path.length - 1)
                        assertEquals("Path did not end with the ending point", step, pEnd);
                    else
                        assertFalse("A point on A* path was an obstacle", m.isObstacle(step.x, step.y));
                }

                // Check continuity
                Point lastStep = null;
                for (final Point step : path)
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
     * @param mb board
     * @param start start point (can be a obstacle)
     * @param end end point (can be a obstacle)
     * @param radius radius of search
     * @return true if there exists a path between start and end point, false otherwise
     */
    private boolean floodFillTest(final MockBoard mb, final Point start, final Point end, final int radius)
    {
        final int EMPTY = 0, FULL = 1, COLOR = 2;

        final int width  = mb.getWidth();
        final int height = mb.getHeight();

        final int[][] board = new int[width][];
        for (int i = 0; i < width; i++)
        {
            board[i] = new int[height];
            for (int j = 0; j < height; j++)
            {
                // Special handling for start and end point: start it's always coloured,
                // while end is always empty, even if they are both obstacles
                if (start.x == i && start.y == j)
                    board[i][j] = COLOR;
                else if (end.x == i && end.y == j)
                    board[i][j] = EMPTY;
                else if (mb.isObstacle(i, j))
                    board[i][j] = FULL;
                else
                    board[i][j] = EMPTY;
            }
        }

        final ArrayList<Point> l = new ArrayList<>(width * height);
        l.add(start);
        while (!l.isEmpty())
        {
            final Point p1 = l.remove(l.size() - 1);
            for (final Directions d : Directions.N8)
            {
                final Point p2 = new Point(p1.x + d.dx(), p1.y + d.dy());
                if (start.distance(p2) >= radius || !mb.contains(p2.x, p2.y) || board[p2.x][p2.y] != EMPTY)
                    continue;

                board[p2.x][p2.y] = COLOR;
                l.add(p2);
            }
        }

        // if the end point is coloured, then there's a path between the start and end point
        return board[end.x][end.y] == COLOR;
    }
}
