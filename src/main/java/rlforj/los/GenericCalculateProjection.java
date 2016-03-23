package rlforj.los;

import java.util.Vector;

import rlforj.math.Point2I;

/**
 * Given a set of squares that we are allowed to visit, and two points
 * A and B, calculates a monotonic path from A to B, if it exists.
 * Else it stops after as far as it can go
 * It is useful to calculate a path along which sight runs from A to B
 * given B is visible from A. An arrow or bolt can fly along this path.
 * @author sdatta
 *
 */
public class GenericCalculateProjection
{

	public static Vector<Point2I> calculateProjecton(int startX, int startY, int x1, int y1, 
			VisitedBoard fb)
	{
		Vector<Point2I> path= new Vector<Point2I>();
		
		// calculate usual Bresenham values required.
		int dx=x1-startX;
		int dy=y1-startY;
		int signX, signY;
		int adx, ady;
		if(dx>0) {
			adx=dx;
			signX=1;
		} else {
			adx=-dx;
			signX=-1;
		}
		if(dy>0) {
			ady=dy;
			signY=1;
		} else {
			ady=-dy;
			signY=-1;
		}
		boolean axesSwapped = false;
		if (adx < ady)
		{
			axesSwapped = true;
			final int tmp = adx;
			adx = ady;
			ady = tmp;
		}

		// System.out.println("adx ady "+adx+" "+ady);
		//calculate the two error values.
		int incE = 2 * ady; //error diff if x++
		int incNE = 2 * ady - 2 * adx; // error diff if x++ and y++
		int d = 2 * ady - adx; // starting error 
		Point2I p = new Point2I(0, 0);
		int lasti = 0, lastj = 0;
		int j = 0;
		for (int i = 0; i <= adx;)
		{
			lasti = i;
			lastj = j;
			if(axesSwapped){
				i=p.y; j=p.x;
			} else {
				i=p.x; j=p.y;
			}
//			 System.out.println("GCP loop "+i+" "+j+" d "+d);
//			 if(d<-2*adx) System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			if(i>adx || j>ady) // searching outside range
			{
//				System.out.println("Outside range "+i+" "+j+" "+adx+" "+ady);
				break;
			}
			
			if (axesSwapped) {
				path.add(new Point2I(
						(j * signX + startX),
						(i * signY + startY)));
			} else {
				path.add(new Point2I(
				(i * signX + startX),
				(j * signY + startY)));
			}
//			System.out.println("Added to path "+path.lastElement());
			if(i==adx && j==ady)//end reached and recorded
			{
//				System.out.println("End reached and recorded ");
				break;
			}
			
			boolean ippNotrecommended = false;//whether i++ is recommended
			if (d <= 0)
			{
				// try to just inc x
				if (axesSwapped)
				{
					p.y = i + 1;
					p.x = j;
				} else
				{
					p.x = i + 1;
					p.y = j;
				}
				if (fb.wasVisited(p.x, p.y) || /* end */ (i==adx && j==ady))
				{
					d += incE;
//					i++;
					continue;
				}
				// System.out.println("cannot i++ "+p+"
				// "+fb.visitedNotObs.contains(p));
			} else
			{
				// System.out.println("i++ not recommended ");
				ippNotrecommended = true;
			}

			// try to inc x and y
			if (axesSwapped)
			{
				p.y = i + 1;
				p.x = j + 1;
			} else
			{
				p.x = i + 1;
				p.y = j + 1;
			}
			if (fb.wasVisited(p.x, p.y) || /* end */ (i==adx && j==ady))
			{
				d += incNE;
//				j++;
//				i++;
				continue;
			}
			// System.out.println("cannot i++ j++ "+p+"
			// "+fb.visitedNotObs.contains(p));
			if (ippNotrecommended)
			{ // try it even if not recommended
				if (axesSwapped)
				{
					p.y = i + 1;
					p.x = j;
				} else
				{
					p.x = i + 1;
					p.y = j;
				}
				if (fb.wasVisited(p.x, p.y) || /* end */ (i==adx && j==ady))
				{
					d += incE;
//					i++;
					continue;
				}
				// System.out.println("cannot i++ "+p+"
				// "+fb.visitedNotObs.contains(p));
			}
			// last resort
			// try to inc just y
			if (axesSwapped)
			{
				p.y = i;
				p.x = j + 1;
			} else
			{
				p.x = i;
				p.y = j + 1;
			}
			if (fb.wasVisited(p.x, p.y) || /* end */ (i==adx && j==ady))
			{
//				System.out.println("GCP y++ "+i+" "+j+" last "+lasti+" "+lastj);
//				if (lasti == i - 1 && lastj == j)// last step was 1 to the
					// right
//					System.out.println("<<- GenericCalculateProj check code");
				// this step is 1 step to up,
				// together 1 diagonal
				// => we dont need last point
				
				d += -incE + incNE;// as if we went 1 step left then took 1
				// step up right
//				j++;
				continue;
			}
			// System.out.println("cannot j++ "+p+"
			// "+fb.visitedNotObs.contains(p));
			// no path, end here, after adding last point.
			if (axesSwapped) {
				path.add(new Point2I(
						(j * signX + startX),
						(i * signY + startY)));
			} else {
				path.add(new Point2I(
				(i * signX + startX),
				(j * signY + startY)));
			}
			break;
		}
		
		return path;
	}
	
	public static interface VisitedBoard {
		public boolean wasVisited(int x, int y);
	}
}
