package rlforj.examples;

import rlforj.IBoard;
import rlforj.math.Point;

import java.util.HashMap;
import java.util.Map;

public class ExampleBoard implements IBoard
{

    public char visibleFloor = '.', invisibleFloor = ' ', invisibleWall = ' ';
    int w, h;
    boolean[][] obstacles;
    boolean[][] visited;
    Map<Point, Character> marks = new HashMap<>();

    public ExampleBoard(final int w, final int h)
    {
        this.w = w;
        this.h = h;

        obstacles = new boolean[w][h];
        visited = new boolean[w][h];
    }

    public void resetVisitedAndMarks()
    {
        marks.clear();
        visited = new boolean[w][h];
    }

    public void mark(final int x, final int y, final char c)
    {
        marks.put(new Point(x, y), c);
    }

    public void setObstacle(final int x, final int y)
    {
        obstacles[x][y] = true;
    }

    public boolean contains(final int x, final int y)
    {
        return x >= 0 && y >= 0 && x < w && y < h;
    }

    public boolean isObstacle(final int x, final int y)
    {
        return obstacles[x][y];
    }

    @Override
    public boolean blocksLight(final int x, final int y)
    {
        return isObstacle(x, y);
    }

    @Override
    public boolean blocksStep(final int x, final int y)
    {
        return isObstacle(x, y);
    }

    public void visit(final int x, final int y)
    {
        visited[x][y] = true;
    }

    public void print(final int ox, final int oy)
    {
        final Point p = new Point(0, 0);
        for (int j = 0; j < h; j++)
        {
            for (int i = 0; i < w; i++)
            {
                p.x = i;
                p.y = j;
                final Character c = marks.get(p);
                if (c != null)
                    System.out.print(c);
                else if (i == ox && j == oy)
                    System.out.print('@');
                else
                    System.out.print(visited[i][j] ?
                                         (obstacles[i][j] ? '#' : visibleFloor) :
                                         (obstacles[i][j] ? invisibleWall : invisibleFloor));
            }
            System.out.println();
        }
    }
}
