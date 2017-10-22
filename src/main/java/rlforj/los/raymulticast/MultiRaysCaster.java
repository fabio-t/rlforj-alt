package rlforj.los.raymulticast;

import rlforj.los.IFovAlgorithm;
import rlforj.los.ILosBoard;
import rlforj.math.Point2I;

import java.util.LinkedList;
import java.util.Queue;

/**
 * TODO: It works now but with many undesirable behaviors.
 * <p>
 * Casts rays from a single point through a {@link World} object, which
 * describes impassable locations. Fills in a 2D array of {@link RayData}
 * objects, describing the visibility status of each point in the world as
 * viewed from the defined origin point.
 * <p>
 * Rays take full advantage of work done by previous ray casts, and do not get
 * cast into occluded areas, making for near-optimal efficiency.
 * <p>
 * The results array is not necessary for the algorithm's operation - it is
 * generated for external use.
 * <p>
 * http://www.geocities.com/temerra/los_rays.html argus2
 */
public class MultiRaysCaster implements IFovAlgorithm
{

    private ILosBoard      world;     // holds obstruction data
    private Point2I        origin;    // the point at which the rays will be
    // cast from
    private Point2I        offset;    // offset for storing in results
    private Queue<RayData> perimeter; // rays currently on the search frontier
    private RayData[][]    results;   // stores calculated data for external use
    private int            dsq;

    public MultiRaysCaster(ILosBoard world, int originX, int originY, int radius)
    {
        this.world = world;
        this.origin = new Point2I(originX, originY);
        this.perimeter = new LinkedList<>();
        this.results = new RayData[2 * radius + 1][2 * radius + 1];

        offset = new Point2I(radius, radius);
        dsq = radius * radius;
    }

    public MultiRaysCaster()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void visitFieldOfView(ILosBoard b, int x, int y, int distance)
    {
        this.world = b;
        this.origin = new Point2I(x, y);
        this.perimeter = new LinkedList<>();
        this.results = new RayData[2 * distance + 1][2 * distance + 1];

        offset = new Point2I(distance, distance);
        dsq = distance * distance;
        b.visit(x, y);
        castRays();
        // printResults();
    }

    public Point2I getOrigin()
    {
        return this.origin;
    }

    public RayData[][] getResults()
    {
        return results;
    }

    /**
     * Executes the ray casting operation by running a breadth-first traversal
     * (flood) of the world, beginning at the origin.
     */
    public void castRays()
    {
        expandPerimeterFrom(new RayData(0, 0));
        RayData currentData;
        while (!perimeter.isEmpty())
        {
            currentData = perimeter.remove();

            // since we are traversing breadth-first, all inputs are guaranteed
            // to be added to current data by the time it is removed.
            mergeInputs(currentData);

            if (!currentData.obscure())
                world.visit(origin.x + currentData.xLoc, origin.y + currentData.yLoc);

            if (!currentData.ignore)
                expandPerimeterFrom(currentData);
        }
    }

    // Expands by the unit length in each component's current direction.
    // If a component has no direction, then it is expanded in both of its
    // positive and negative directions.
    private void expandPerimeterFrom(RayData from)
    {
        if (from.xLoc >= 0)
            processRay(new RayData(from.xLoc + 1, from.yLoc), from);
        if (from.xLoc <= 0)
            processRay(new RayData(from.xLoc - 1, from.yLoc), from);
        if (from.yLoc >= 0)
            processRay(new RayData(from.xLoc, from.yLoc + 1), from);
        if (from.yLoc <= 0)
            processRay(new RayData(from.xLoc, from.yLoc - 1), from);
    }

    // Does bounds checking, marks obstructions, assigns inputs, and adds the
    // ray to the perimeter if it is valid.
    private void processRay(RayData newRay, RayData inputRay)
    {
        if (dsq < newRay.xLoc * newRay.xLoc + newRay.yLoc * newRay.yLoc)
            return;

        int mapX = (origin.x + newRay.xLoc);
        int mapY = (origin.y + newRay.yLoc);

        // bounds check
        if (!world.contains(mapX, mapY))
            return;
        // if((mapX < 0) || (mapX > (world.getSize() - 1))) return;
        // if((mapY < 0) || (mapY > (world.getSize() - 1))) return;

        // Since there are multiple inputs to each new ray, we need to check if
        // the new ray has already been set up.
        // Here we use the results table as lookup, but we could easily use
        // a different structure, such as a hashset keyed point data.
        if (results[mapX - origin.x + offset.x][mapY - origin.y + offset.y] != null)
            newRay = results[mapX - origin.x + offset.x][mapY - origin.y + offset.y];

        // Setting the reference from the new ray to this input ray.
        boolean isXInput = (newRay.yLoc == inputRay.yLoc);
        if (isXInput)
            newRay.xInput = inputRay;
        else
            newRay.yInput = inputRay;

        // Adding the new ray to the perimeter if it hasn't already been added.
        if (!newRay.added)
        {
            perimeter.add(newRay);
            newRay.added = true;
            results[offset.x + newRay.xLoc][offset.y + newRay.yLoc] = newRay;

        }
    }

    // Once all inputs are known to be assigned, mergeInputs performs the key
    // task of populating the new ray with the correct data.
    private void mergeInputs(RayData newRay)
    {

        // if(newRay.obscure())
        // world.visit(origin.x + newRay.xLoc, origin.y + newRay.yLoc);
        // Obstructions must propagate obscurity.
        if (world.blocksLight((origin.x + newRay.xLoc), (origin.y + newRay.yLoc)))
        {
            int absXLoc = Math.abs(newRay.xLoc);
            int absYLoc = Math.abs(newRay.yLoc);
            newRay.xObsc = absXLoc;
            newRay.yObsc = absYLoc;
            newRay.xErrObsc = newRay.xObsc;
            newRay.yErrObsc = newRay.yObsc;
            return;
        }

        RayData xInput     = newRay.xInput;
        RayData yInput     = newRay.yInput;
        boolean xInputNull = (xInput == null);
        boolean yInputNull = (yInput == null);

        // Process individual input information.
        if (!xInputNull)
            processXInput(newRay, xInput);
        if (!yInputNull)
            processYInput(newRay, yInput);

        // Culling handled here.
        // If both inputs are null, the point is never checked, so ignorance
        // is propagated trivially in that case.
        if (xInputNull)
        {
            // cut point (inside edge)
            if (yInput.obscure())
                newRay.ignore = true;
        }
        else if (yInputNull)
        {
            // cut point (inside edge)
            if (xInput.obscure())
                newRay.ignore = true;
        }
        else
        { // both y and x inputs are valid
            // cut point (within arc of obscurity)
            if (xInput.obscure() && yInput.obscure())
            {
                newRay.ignore = true;
                return;
            }
        }
    } // END mergeInputs(RayData)

    // The X input can provide two main pieces of information:
    // 1. Progressive X obscurity.
    // 2. Recessive Y obscurity.
    private void processXInput(RayData newRay, RayData xInput)
    {
        if ((xInput.xObsc == 0) && (xInput.yObsc == 0))
            return;

        // Progressive X obscurity
        if (xInput.xErrObsc > 0)
        {
            if (newRay.xObsc == 0)
            { // favouring recessive input angle
                newRay.xErrObsc = (xInput.xErrObsc - xInput.yObsc);
                newRay.yErrObsc = (xInput.yErrObsc + xInput.yObsc);
                newRay.yObsc = xInput.yObsc;
                newRay.xObsc = xInput.xObsc;
            }
        }
        // Recessive Y obscurity
        if (xInput.yErrObsc <= 0)
        {
            if ((xInput.yObsc > 0) && (xInput.xErrObsc > 0))
            {
                newRay.yErrObsc = (xInput.yObsc + xInput.yErrObsc);
                newRay.xErrObsc = (xInput.xErrObsc - xInput.yObsc);
                newRay.xObsc = xInput.xObsc;
                newRay.yObsc = xInput.yObsc;
            }
        }
    }

    // The Y input can provide two main pieces of information:
    // 1. Progressive Y obscurity.
    // 2. Recessive X obscurity.
    private void processYInput(RayData newRay, RayData yInput)
    {
        if ((yInput.xObsc == 0) && (yInput.yObsc == 0))
            return;

        // Progressive Y obscurity
        if (yInput.yErrObsc > 0)
        {
            if (newRay.yObsc == 0)
            { // favouring recessive input angle
                newRay.yErrObsc = (yInput.yErrObsc - yInput.xObsc);
                newRay.xErrObsc = (yInput.xErrObsc + yInput.xObsc);
                newRay.xObsc = yInput.xObsc;
                newRay.yObsc = yInput.yObsc;
            }
        }
        // Recessive X obscurity
        if (yInput.xErrObsc <= 0)
        {
            if ((yInput.xObsc > 0) && (yInput.yErrObsc > 0))
            {
                newRay.xErrObsc = (yInput.xObsc + yInput.xErrObsc);
                newRay.yErrObsc = (yInput.yErrObsc - yInput.xObsc);
                newRay.xObsc = yInput.xObsc;
                newRay.yObsc = yInput.yObsc;
            }
        }
    }

    public void printResults()
    {
        for (RayData[] rdr : results)
        {
            for (RayData rd : rdr)
            {
                System.out.print(rd == null ? "N" : rd.toChar());
            }
            System.out.println();
        }
        System.out.println();
    }

    // public static void main(String[] args)
    // {
    // TestBoard1 b=new TestBoard1(false);
    // b.exception.add(new Point2I(11, 11));
    // b.exception.add(new Point2I(7, 8));
    // MultiRaysCaster m=new MultiRaysCaster();
    // m.visitFieldOfView(b, 10, 10, 10);
    // System.out.println(b.obsNotVisited);
    // b.mark(10, 10, '@');
    // b.print(0, 20, 0, 20);
    //
    //// System.out.println(b.chkb4visit);
    //
    //// b.visited.clear();
    //// IFovAlgorithm pp=new ShadowCasting();
    //// pp.visitFieldOfView(b, 10, 10, 3);
    //// b.mark(10, 10, '@');
    //// b.print(0, 20, 0, 20);
    // }
    //
    // private static class TestBoard1 extends TestBoard {
    //
    // public Set<Point2I> obsNotVisited=new HashSet<Point2I>();
    //
    // public TestBoard1(boolean defaultObscured)
    // {
    // super(defaultObscured);
    // }
    //
    // @Override
    // public void visit(int x, int y)
    // {
    // super.visit(x, y);
    // System.out.println("Visiting "+x+" "+y);
    // obsNotVisited.remove(new Point2I(x, y));
    // }
    //
    // @Override
    // public boolean isObstacle(int x, int y)
    // {
    // System.out.println("isObs "+x+" "+y);
    // obsNotVisited.add(new Point2I(x, y));
    // return super.isObstacle(x, y);
    // }
    // }

}
