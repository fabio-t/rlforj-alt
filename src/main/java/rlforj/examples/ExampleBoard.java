package rlforj.examples;

import rlforj.los.ILosBoard;
import rlforj.math.Point;

import java.util.HashMap;
import java.util.Map;

public class ExampleBoard implements ILosBoard
{

    public char visibleFloor = '.', invisibleFloor = ' ', invisibleWall = ' ';
    int w, h;
    boolean[][] obstacles;
    boolean[][] visited;
    Map<Point, Character> marks = new HashMap<Point, Character>();

    public ExampleBoard(int w, int h)
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

    public void mark(int x, int y, char c)
    {
        marks.put(new Point(x, y), c);
    }

    public void setObstacle(int x, int y)
    {
        obstacles[x][y] = true;
    }

    public boolean contains(int x, int y)
    {
        return x >= 0 && y >= 0 && x < w && y < h;
    }

    public boolean isObstacle(int x, int y)
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

    public void visit(int x, int y)
    {
        visited[x][y] = true;
    }

    public void print(int ox, int oy)
    {
        Point p = new Point(0, 0);
        for (int j = 0; j < h; j++)
        {
            for (int i = 0; i < w; i++)
            {
                p.x = i;
                p.y = j;
                Character c = marks.get(p);
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
