package rlforj.los.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import rlforj.los.ILosBoard;
import rlforj.math.Point2I;

public class TestBoard implements ILosBoard
{

	public boolean def; // true => obstacle

	public Set<Point2I> exception = new HashSet<Point2I>();

	public Set<Point2I> visited = new HashSet<Point2I>();

	public Set<Point2I> chkb4visit = new HashSet<Point2I>();

	public Set<Point2I> visiterr = new HashSet<Point2I>();

	public Set<Point2I> prjPath = new HashSet<Point2I>();
	
	public Map<Point2I, Character> marks = new HashMap<Point2I, Character>();
	
	public TestBoard(boolean defaultObscured)
	{
		this.def = defaultObscured;
	}

	public void mark(int x, int y, char c) {
		marks.put(new Point2I(x, y), c);
	}
	
	public boolean contains(int x, int y)
	{
		return true;
	}

	public boolean isObstacle(int x, int y)
	{
		Point2I p = new Point2I(x, y);
		if (!visited.contains(p))
			chkb4visit.add(p);
		return def ^ exception.contains(new Point2I(x, y));
	}

	public void visit(int x, int y)
	{
		Point2I p = new Point2I(x, y);
		if (visited.contains(p))
			visiterr.add(p);
		visited.add(new Point2I(x, y));
	}

	public void print(int fromx, int tox, int fromy, int toy)
	{
		for (int y = fromy; y <= toy; y++)
		{
			for (int x = fromx; x <= tox; x++)
			{
				Point2I point = new Point2I(x, y);
				Character c=marks.get(point);
				if(c==null) {
					if (isObstacle(x, y))
						c=(visited.contains(point) ? '#'
								: 'x');
					else {
						c=(visited.contains(point) ? 'o'
								: '.');
					}
				}
				System.out.print(c);
			}
			System.out.println();
		}
	}

}
