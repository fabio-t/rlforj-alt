package rlforj.los.test;

import rlforj.los.ILosBoard;
import rlforj.math.Point;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestBoard implements ILosBoard
{

    public boolean def; // true => obstacle

    public Set<Point> exception = new HashSet<Point>();

    public Set<Point> visited = new HashSet<Point>();

    public Set<Point> chkb4visit = new HashSet<Point>();

    public Set<Point> visiterr = new HashSet<Point>();

    public Set<Point> prjPath = new HashSet<Point>();

    public Map<Point, Character> marks = new HashMap<Point, Character>();

    public TestBoard(boolean defaultObscured)
    {
        this.def = defaultObscured;
    }

    public void mark(int x, int y, char c)
    {
        marks.put(new Point(x, y), c);
    }

    public boolean contains(int x, int y)
    {
        return true;
    }

    public boolean isObstacle(int x, int y)
    {
        Point p = new Point(x, y);
        if (!visited.contains(p))
            chkb4visit.add(p);
        return def ^ exception.contains(new Point(x, y));
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
        Point p = new Point(x, y);
        if (visited.contains(p))
            visiterr.add(p);
        visited.add(new Point(x, y));
    }

    public void print(int fromx, int tox, int fromy, int toy)
    {
        for (int y = fromy; y <= toy; y++)
        {
            for (int x = fromx; x <= tox; x++)
            {
                Point     point = new Point(x, y);
                Character c     = marks.get(point);
                if (c == null)
                {
                    if (blocksLight(x, y))
                        c = (visited.contains(point) ? '#' : 'x');
                    else
                    {
                        c = (visited.contains(point) ? 'o' : '.');
                    }
                }
                System.out.print(c);
            }
            System.out.println();
        }
    }

}
