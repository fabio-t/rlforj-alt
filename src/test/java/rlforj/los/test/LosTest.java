package rlforj.los.test;

import junit.framework.TestCase;
import rlforj.los.ILosAlgorithm;
import rlforj.math.Point;

/**
 * Testing Los algorithms
 */
public abstract class LosTest extends TestCase
{
    ILosAlgorithm a;

    public void testEmpty()
    {
        final TestBoard b = new TestBoard(false);

        // b.print(5, 15, 5, 15);
        // System.out.println();

        assertTrue(a.existsLineOfSight(b, 10, 10, 11, 11, false));
        assertTrue(a.existsLineOfSight(b, 10, 10, 10, 11, false));
        assertTrue(a.existsLineOfSight(b, 10, 10, 11, 10, false));
        assertTrue(a.existsLineOfSight(b, 10, 10, 10, 15, false));
        assertTrue(a.existsLineOfSight(b, 10, 10, 15, 10, false));
    }

    public void testFull()
    {
        final TestBoard b = new TestBoard(true);

        // b.print(5, 15, 5, 15);
        // System.out.println();

        assertTrue(a.existsLineOfSight(b, 10, 10, 11, 11, false));
        assertTrue(a.existsLineOfSight(b, 10, 10, 10, 11, false));
        assertTrue(a.existsLineOfSight(b, 10, 10, 11, 10, false));
        assertFalse(a.existsLineOfSight(b, 10, 10, 10, 15, false));
        assertFalse(a.existsLineOfSight(b, 10, 10, 15, 10, false));
    }

    public void testLine()
    {
        final TestBoard b = new TestBoard(true);

        for (int i = 5; i < 11; i++)
        {
            b.exception.add(new Point(i, 10));
        }

        // b.print(5, 15, 5, 15);
        // System.out.println();

        assertTrue(a.existsLineOfSight(b, 10, 10, 11, 11, false));
        assertTrue(a.existsLineOfSight(b, 10, 10, 10, 11, false));
        assertTrue(a.existsLineOfSight(b, 10, 10, 11, 10, false));
        assertTrue(a.existsLineOfSight(b, 10, 10, 5, 10, false));
        assertFalse(a.existsLineOfSight(b, 10, 10, 15, 10, false));
    }

    public void testAcrossPillar()
    {
        final TestBoard b = new TestBoard(false);

        b.exception.add(new Point(10, 10));

        // b.print(4, 14, 4, 14);
        // System.out.println();

        assertTrue(a.existsLineOfSight(b, 9, 9, 10, 11, false));
        assertFalse(a.existsLineOfSight(b, 9, 9, 11, 11, false));
    }

    public void testDiagonalWall()
    {
        final TestBoard b = new TestBoard(false);

        b.exception.add(new Point(11, 11));
        b.exception.add(new Point(10, 10));

        // b.print(5, 15, 6, 16);
        // System.out.println();
        assertTrue(a.existsLineOfSight(b, 10, 11, 11, 10, false));

    }
}
