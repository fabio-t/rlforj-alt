package rlforj.los;

import rlforj.math.Line2I;
import rlforj.math.Point2I;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Precise permissive visibility algorithm.
 * <p>
 * Refer to
 * {@link http://roguebasin.roguelikedevelopment.org/index.php?title=Precise_Permissive_Field_of_View}
 * Copyright (c) 2007, Jonathon Duerig. Licensed under the BSD
 * license. See LICENSE.txt for details.
 * <p>
 * TODO : Do multitile organism by replacing offsetT(0,1)(1, 0) by offsetT(0,
 * size.y) (size.x, 0). Also need to consider border tiles.
 *
 * @author sdatta
 */
public class PrecisePermissive implements IFovAlgorithm, ILosAlgorithm
{

    private Vector<Point2I> path;

    //	class offsetT
    //	{
    //		public offsetT(int newX, int newY)
    //		{
    //			x = newX;
    //			y = newY;
    //		}
    //
    //		public int x;
    //
    //		public int y;
    //
    //		public String toString()
    //		{
    //			return "(" + x + ", " + y + ")";
    //		}
    //	}
    private ILosAlgorithm fallBackLos = new BresLos(true);

    ;

    void calculateFovQuadrant(final fovStateT state)
    {
        // System.out.println("calcfovq called");
        LinkedList<bumpT> steepBumps   = new LinkedList<bumpT>();
        LinkedList<bumpT> shallowBumps = new LinkedList<bumpT>();
        // activeFields is sorted from shallow-to-steep.
        LinkedList<fieldT> activeFields = new LinkedList<fieldT>();
        activeFields.addLast(new fieldT());
        activeFields.getLast().shallow.near = new Point2I(0, 1);
        activeFields.getLast().shallow.far = new Point2I(state.extent.x, 0);
        activeFields.getLast().steep.near = new Point2I(1, 0);
        activeFields.getLast().steep.far = new Point2I(0, state.extent.y);

        Point2I dest = new Point2I(0, 0);

        // Visit the source square exactly once (in quadrant 1).
        if (state.quadrant.x == 1 && state.quadrant.y == 1)
        {
            actIsBlocked(state, dest);
        }

        CLikeIterator<fieldT> currentField = new CLikeIterator<fieldT>(activeFields.listIterator());
        int                   i            = 0;
        int                   j            = 0;
        int                   maxI         = state.extent.x + state.extent.y;
        // For each square outline
        for (i = 1; i <= maxI && !activeFields.isEmpty(); ++i)
        {
            int startJ = max(0, i - state.extent.x);
            int maxJ   = min(i, state.extent.y);
            // System.out.println("Startj "+startJ+" maxj "+maxJ);
            // Visit the nodes in the outline
            for (j = startJ; j <= maxJ && !currentField.isAtEnd(); ++j)
            {
                // System.out.println("i j "+i+" "+j);
                dest.x = i - j;
                dest.y = j;
                visitSquare(state, dest, currentField, steepBumps, shallowBumps, activeFields);
            }
            // System.out.println("Activefields size "+activeFields.size());
            currentField = new CLikeIterator<fieldT>(activeFields.listIterator());
        }
    }

    private final int max(int i, int j)
    {
        return i > j ? i : j;
    }

    private final int min(int i, int j)
    {
        return i < j ? i : j;
    }

    void visitSquare(final fovStateT state, final Point2I dest, CLikeIterator<fieldT> currentField,
                     LinkedList<bumpT> steepBumps, LinkedList<bumpT> shallowBumps, LinkedList<fieldT> activeFields)
    {
        //		System.out.println("-> "+steepBumps+" - "+shallowBumps);
        // System.out.println("visitsq called "+dest);
        // The top-left and bottom-right corners of the destination square.
        Point2I topLeft     = new Point2I(dest.x, dest.y + 1);
        Point2I bottomRight = new Point2I(dest.x + 1, dest.y);

        // fieldT currFld=null;

        while (!currentField.isAtEnd() && currentField.getCurrent().steep.isBelowOrContains(bottomRight))
        {
            //			System.out.println("currFld.steep.isBelowOrContains(bottomRight) "
            //					+ currentField.getCurrent().steep
            //							.isBelowOrContains(bottomRight));
            // case ABOVE
            // The square is in case 'above'. This means that it is ignored
            // for the currentField. But the steeper fields might need it.
            // ++currentField;
            currentField.gotoNext();
        }
        if (currentField.isAtEnd())
        {
            //			System.out.println("currentField.isAtEnd()");
            // The square was in case 'above' for all fields. This means that
            // we no longer care about it or any squares in its diagonal rank.
            return;
        }

        // Now we check for other cases.
        if (currentField.getCurrent().shallow.isAboveOrContains(topLeft))
        {
            // case BELOW
            // The shallow line is above the extremity of the square, so that
            // square is ignored.
            //			System.out.println("currFld.shallow.isAboveOrContains(topLeft) "
            //					+ currentField.getCurrent().shallow);
            return;
        }
        // The square is between the lines in some way. This means that we
        // need to visit it and determine whether it is blocked.
        boolean isBlocked = actIsBlocked(state, dest);
        if (!isBlocked)
        {
            // We don't care what case might be left, because this square does
            // not obstruct.
            return;
        }

        if (currentField.getCurrent().shallow.isAbove(bottomRight) && currentField.getCurrent().steep.isBelow(topLeft))
        {
            // case BLOCKING
            // Both lines intersect the square. This current field has ended.
            currentField.removeCurrent();
        }
        else if (currentField.getCurrent().shallow.isAbove(bottomRight))
        {
            // case SHALLOW BUMP
            // The square intersects only the shallow line.
            addShallowBump(topLeft, currentField.getCurrent(), steepBumps, shallowBumps);
            checkField(currentField);
        }
        else if (currentField.getCurrent().steep.isBelow(topLeft))
        {
            // case STEEP BUMP
            // The square intersects only the steep line.
            addSteepBump(bottomRight, currentField.getCurrent(), steepBumps, shallowBumps);
            checkField(currentField);
        }
        else
        {
            // case BETWEEN
            // The square intersects neither line. We need to split into two
            // fields.
            fieldT steeperField   = currentField.getCurrent();
            fieldT shallowerField = new fieldT(currentField.getCurrent());
            currentField.insertBeforeCurrent(shallowerField);
            addSteepBump(bottomRight, shallowerField, steepBumps, shallowBumps);
            currentField.gotoPrevious();
            if (!checkField(currentField)) // did not remove
                currentField.gotoNext();// point to the original element
            addShallowBump(topLeft, steeperField, steepBumps, shallowBumps);
            checkField(currentField);
        }
    }

    boolean checkField(CLikeIterator<fieldT> currentField)
    {
        // If the two slopes are colinear, and if they pass through either
        // extremity, remove the field of view.
        fieldT  currFld = currentField.getCurrent();
        boolean ret     = false;

        if (currFld.shallow.doesContain(currFld.steep.near) && currFld.shallow.doesContain(currFld.steep.far) &&
            (currFld.shallow.doesContain(new Point2I(0, 1)) || currFld.shallow.doesContain(new Point2I(1, 0))))
        {
            //			System.out.println("removing "+currentField.getCurrent());
            currentField.removeCurrent();
            ret = true;
        }
        // System.out.println("CheckField "+ret);
        return ret;
    }

    void addShallowBump(final Point2I point, fieldT currFld, LinkedList<bumpT> steepBumps,
                        LinkedList<bumpT> shallowBumps)
    {
        //		System.out.println("Adding shallow "+point);
        // First, the far point of shallow is set to the new point.
        currFld.shallow.far = point;
        // Second, we need to add the new bump to the shallow bump list for
        // future steep bump handling.
        shallowBumps.addLast(new bumpT());
        shallowBumps.getLast().location = point;
        shallowBumps.getLast().parent = currFld.shallowBump;
        currFld.shallowBump = shallowBumps.getLast();
        // Now we have too look through the list of steep bumps and see if
        // any of them are below the line.
        // If there are, we need to replace near point too.
        bumpT currentBump = currFld.steepBump;
        while (currentBump != null)
        {
            if (currFld.shallow.isAbove(currentBump.location))
            {
                currFld.shallow.near = currentBump.location;
            }
            currentBump = currentBump.parent;
        }
    }

    void addSteepBump(final Point2I point, fieldT currFld, LinkedList<bumpT> steepBumps, LinkedList<bumpT> shallowBumps)
    {
        //		System.out.println("Adding steep "+point);
        currFld.steep.far = point;
        steepBumps.addLast(new bumpT());
        steepBumps.getLast().location = point;
        steepBumps.getLast().parent = currFld.steepBump;
        currFld.steepBump = steepBumps.getLast();
        // Now look through the list of shallow bumps and see if any of them
        // are below the line.
        bumpT currentBump = currFld.shallowBump;
        while (currentBump != null)
        {
            if (currFld.steep.isBelow(currentBump.location))
            {
                currFld.steep.near = currentBump.location;
            }
            currentBump = currentBump.parent;
        }
    }

    boolean actIsBlocked(final fovStateT state, final Point2I pos)
    {
        Point2I adjustedPos = new Point2I(pos.x * state.quadrant.x + state.source.x,
                                          pos.y * state.quadrant.y + state.source.y);

        if (!state.board.contains(adjustedPos.x, adjustedPos.y))
            return false;//we are getting outside the board

        // System.out.println("actIsBlocked "+adjustedPos.x+" "+adjustedPos.y);

        // if ((state.quadrant.x * state.quadrant.y == 1
        // && pos.x == 0 && pos.y != 0)
        // || (state.quadrant.x * state.quadrant.y == -1
        // && pos.y == 0 && pos.x != 0)
        // || doesPermissiveVisit(state.mask, pos.x*state.quadrant.x,
        // pos.y*state.quadrant.y) == 0)
        // {
        // // return result;
        // }
        // else
        // {
        // board.visit(adjustedPos.x, adjustedPos.y);
        // // return result;
        // }
        /*
         * ^ | 2 | <-3-+-1-> | 4 | v
		 *
		 * To ensure all squares are visited before checked ( so that we can
		 * decide obstacling at visit time, eg walls destroyed by explosion) ,
		 * visit axes 1,2 only in Q1, 3 in Q2, 4 in Q3
		 */
        if (state.isLos // In LOS calculation all visits allowed
            || state.quadrantIndex == 0 // can visit anything from Q1
            || (state.quadrantIndex == 1 && pos.x != 0) // Q2 : no Y axis
            || (state.quadrantIndex == 2 && pos.y != 0) // Q3 : no X axis
            || (state.quadrantIndex == 3 && pos.x != 0 && pos.y != 0)) // Q4
            // no X
            // or Y
            // axis
            if (doesPermissiveVisit(state.mask, pos.x * state.quadrant.x, pos.y * state.quadrant.y) == 1)
            {
                state.board.visit(adjustedPos.x, adjustedPos.y);
            }
        return state.board.blocksLight(adjustedPos.x, adjustedPos.y);
    }

    void permissiveFov(int sourceX, int sourceY, permissiveMaskT mask)
    {
        fovStateT state = new fovStateT();
        state.source = new Point2I(sourceX, sourceY);
        state.mask = mask;
        state.board = mask.board;
        // state.isBlocked = isBlocked;
        // state.visit = visit;
        // state.context = context;

        final int     quadrantCount = 4;
        final Point2I quadrants[]   = { new Point2I(1, 1), new Point2I(-1, 1), new Point2I(-1, -1),
                                        new Point2I(1, -1) };

        Point2I extents[] = { new Point2I(mask.east, mask.north), new Point2I(mask.west, mask.north),
                              new Point2I(mask.west, mask.south), new Point2I(mask.east, mask.south) };
        int quadrantIndex = 0;
        for (; quadrantIndex < quadrantCount; ++quadrantIndex)
        {
            state.quadrant = quadrants[quadrantIndex];
            state.extent = extents[quadrantIndex];
            state.quadrantIndex = quadrantIndex;
            calculateFovQuadrant(state);
        }
    }

    int doesPermissiveVisit(permissiveMaskT mask, int x, int y)
    {
        if (mask.fovType == FovType.SQUARE)
            return 1;
        else if (mask.fovType == FovType.CIRCLE)
        {
            if (x * x + y * y < mask.distPlusOneSq)
                return 1;
            else
                return 0;
        }
        return 1;
    }

    public void visitFieldOfView(ILosBoard b, int x, int y, int distance)
    {
        permissiveMaskT mask = new permissiveMaskT();
        mask.east = mask.north = mask.south = mask.west = distance;
        mask.mask = null;
        mask.fovType = FovType.CIRCLE;
        mask.distPlusOneSq = (distance + 1) * (distance + 1);
        mask.board = b;
        permissiveFov(x, y, mask);
    }

    /**
     * Algorithm inspired by
     * http://groups.google.com/group/rec.games.roguelike.development/browse_thread/thread/f3506215be9d9f9a/2e543127f705a278#2e543127f705a278
     *
     * @see rlforj.los.ILosAlgorithm1#existsLineOfSight(rlforj.los.ILosBoard, int, * int, int, int, boolean)
     */
    public boolean existsLineOfSight(ILosBoard b, int startX, int startY, int x1, int y1, boolean calculateProject)
    {
        permissiveMaskT          mask = new permissiveMaskT();
        int                      dx   = x1 - startX;
        int                      adx  = dx > 0 ? dx : -dx;
        int                      dy   = y1 - startY;
        int                      ady  = dy > 0 ? dy : -dy;
        RecordQuadrantVisitBoard fb   = new RecordQuadrantVisitBoard(b, startX, startY, x1, y1, calculateProject);
        mask.east = mask.west = adx;
        mask.north = mask.south = ady;
        mask.mask = null;
        mask.fovType = FovType.SQUARE;
        mask.distPlusOneSq = 0;
        mask.board = fb;

        fovStateT state = new fovStateT();
        state.source = new Point2I(startX, startY);
        state.mask = mask;
        state.board = fb;
        state.isLos = true;
        state.quadrant = new Point2I(dx < 0 ? -1 : 1, dy < 0 ? -1 : 1);
        state.quadrantIndex = 0;

        LinkedList<bumpT> steepBumps   = new LinkedList<bumpT>();
        LinkedList<bumpT> shallowBumps = new LinkedList<bumpT>();
        // activeFields is sorted from shallow-to-steep.
        LinkedList<fieldT> activeFields = new LinkedList<fieldT>();
        activeFields.addLast(new fieldT());
        activeFields.getLast().shallow.near = new Point2I(0, 1);
        activeFields.getLast().shallow.far = new Point2I(adx + 1, 0);
        activeFields.getLast().steep.near = new Point2I(1, 0);
        activeFields.getLast().steep.far = new Point2I(0, ady + 1);

        Point2I dest = new Point2I(0, 0);

        Line2I stopLine = new Line2I(new Point2I(0, 1),
                                     new Point2I(adx, ady + 1)), startLine = new Line2I(new Point2I(1, 0),
                                                                                        new Point2I(adx + 1, ady));

        // Visit the source square exactly once (in quadrant 1).
        actIsBlocked(state, dest);

        CLikeIterator<fieldT> currentField = new CLikeIterator<fieldT>(activeFields.listIterator());
        int                   maxI         = adx + ady;
        // For each square outline
        int     lastStartJ = -1;
        Point2I topLeft    = new Point2I(0, 0), bottomRight = new Point2I(0, 0);
        for (int i = 1; i <= maxI && !activeFields.isEmpty(); ++i)
        {
            // System.out.println("i "+i);
            int startJ = max(0, i - adx);
            startJ = max(startJ, lastStartJ - 1);
            int maxJ = min(i, ady);

            // System.out.println("Startj "+startJ+" maxj "+maxJ);
            // Visit the nodes in the outline
            int thisStartJ = -1;
            // System.out.println("startJ "+startJ+" maxJ "+maxJ);
            for (int j = startJ; j <= maxJ && !currentField.isAtEnd(); ++j)
            {
                // System.out.println("i j "+i+" "+j);
                dest.x = i - j;
                dest.y = j;
                topLeft.x = dest.x;
                topLeft.y = dest.y + 1;
                bottomRight.x = dest.x + 1;
                bottomRight.y = dest.y;
                // System.out.println(startLine+" "+topLeft+" "+stopLine+"
                // "+bottomRight);
                // System.out.println("isbelow "+startLine.isBelow(topLeft)+"
                // isabove "+stopLine.isAbove(bottomRight));
                if (startLine.isAboveOrContains(topLeft))
                {
                    // not in range, continue
                    // System.out.println("below start");
                    continue;
                }
                if (stopLine.isBelowOrContains(bottomRight))
                {
                    // done
                    // System.out.println("Above stop ");
                    break;
                }
                // in range
                if (thisStartJ == -1)
                    thisStartJ = j;
                visitSquare(state, dest, currentField, steepBumps, shallowBumps, activeFields);
            }
            lastStartJ = thisStartJ;
            // System.out.println("Activefields size "+activeFields.size());
            currentField = new CLikeIterator<fieldT>(activeFields.listIterator());
        }

        if (calculateProject)
        {
            if (fb.endVisited)
                path = GenericCalculateProjection.calculateProjecton(startX, startY, x1, y1, fb);
            else
            {
                fallBackLos.existsLineOfSight(b, startX, startY, x1, y1, true);
                path = (Vector<Point2I>) fallBackLos.getProjectPath();
            }
            //			calculateProjecton(startX, startY, adx, ady, fb, state);
        }
        return fb.endVisited;
    }

    /*
     * (non-Javadoc)
     *
     * @see sid.los.ILosAlgorithm1#getProjectPath()
     */
    public List<Point2I> getProjectPath()
    {
        return path;
    }

    class permissiveMaskT
    {
        public FovType fovType;
        public int     distPlusOneSq;
        /*
         * Do not interact with the members directly. Use the provided
         * functions.
         */ int north;
        int       south;
        int       east;
        int       west;
        // int width;
        // int height;
        int[]     mask;
        ILosBoard board;
    }

    class fovStateT
    {
        public int quadrantIndex;
        public boolean isLos = false;
        Point2I         source;
        permissiveMaskT mask;
        Object          context;
        Point2I         quadrant;
        Point2I         extent;
        ILosBoard       board;
    }

    class bumpT
    {
        Point2I location;
        bumpT parent = null;

        public bumpT()
        {
        }

        public String toString()
        {
            return location.toString() + " p( " + parent + " ) ";
        }
    }

    class fieldT
    {
        Line2I steep   = new Line2I(new Point2I(0, 0), new Point2I(0, 0));
        Line2I shallow = new Line2I(new Point2I(0, 0), new Point2I(0, 0));
        bumpT steepBump;
        bumpT shallowBump;

        public fieldT(fieldT f)
        {
            steep = new Line2I(new Point2I(f.steep.near.x, f.steep.near.y), new Point2I(f.steep.far.x, f.steep.far.y));
            shallow = new Line2I(new Point2I(f.shallow.near.x, f.shallow.near.y),
                                 new Point2I(f.shallow.far.x, f.shallow.far.y));
            steepBump = f.steepBump;
            shallowBump = f.shallowBump;
        }

        public fieldT()
        {
            // TODO Auto-generated constructor stub
        }

        public String toString()
        {
            return "[ steep " + steep + ",  shallow " + shallow + "]";
        }
    }

    //	public static void main(String[] args)
    //	{
    //		ILosAlgorithm pp = new PrecisePermissive();
    //		TestBoard b = new TestBoard(false);
    //		// b.exception.add(new Point2I(10, 11));
    //		// b.exception.add(new Point2I(11, 10));
    //		// b.exception.add(new Point2I(14, 15));
    //		b.exception.add(new Point2I(14, 18));
    //		System.out.println(pp.existsLineOfSight(b, -10, -10, -19, -15, true));
    //
    //		System.out.println(pp.getProjectPathX());
    //		System.out.println(pp.getProjectPathY());
    //
    //		System.out.println(b.visited);
    //	}

}
