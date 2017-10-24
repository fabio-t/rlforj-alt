package rlforj.los.test;

import com.sun.org.apache.xpath.internal.SourceTree;
import junit.framework.TestCase;
import rlforj.los.IFovAlgorithm;
import rlforj.los.PrecisePermissive;
import rlforj.math.Point2I;

import java.util.Random;

/**
 * Testing FOV algorithms
 *
 * @author sdatta
 */
public abstract class FovTest extends TestCase
{
    IFovAlgorithm a;

    public void testEmpty()
    {
        final TestBoard b = new TestBoard(false);

        a.visitFieldOfView(b, 10, 10, 5);
        // b.print(5, 15, 5, 15);
        // System.out.println();

        assertTrue(b.visited.contains(new Point2I(11, 11)));
        assertTrue(b.visited.contains(new Point2I(10, 11)));
        assertTrue(b.visited.contains(new Point2I(11, 10)));
        assertTrue(b.visited.contains(new Point2I(10, 15)));
        assertTrue(b.visited.contains(new Point2I(15, 10)));
    }

    public void testFull()
    {
        final TestBoard b = new TestBoard(true);

        a.visitFieldOfView(b, 10, 10, 5);
        // b.print(5, 15, 5, 15);
        // System.out.println();

        assertTrue(b.visited.contains(new Point2I(11, 11)));
        assertTrue(b.visited.contains(new Point2I(10, 11)));
        assertTrue(b.visited.contains(new Point2I(11, 10)));
        assertFalse(b.visited.contains(new Point2I(10, 15)));
        assertFalse(b.visited.contains(new Point2I(15, 10)));
    }

    public void testLine()
    {
        final TestBoard b = new TestBoard(true);

        for (int i = 5; i < 11; i++)
        {
            b.exception.add(new Point2I(i, 10));
        }

        a.visitFieldOfView(b, 10, 10, 5);
        // b.print(5, 15, 5, 15);
        // System.out.println();

        assertTrue(b.visited.contains(new Point2I(11, 11)));
        assertTrue(b.visited.contains(new Point2I(10, 11)));
        assertTrue(b.visited.contains(new Point2I(11, 10)));
        assertTrue(b.visited.contains(new Point2I(5, 10)));
        assertFalse(b.visited.contains(new Point2I(15, 10)));
    }

    public void testAcrossPillar()
    {
        final TestBoard b = new TestBoard(false);

        b.exception.add(new Point2I(10, 10));

        a.visitFieldOfView(b, 9, 9, 5);
        // b.print(4, 14, 4, 14);
        // System.out.println();

        assertTrue(b.visited.contains(new Point2I(10, 11)));
        assertFalse(b.visited.contains(new Point2I(11, 11)));
    }

    public void testDiagonalWall()
    {
        final TestBoard b = new TestBoard(false);

        b.exception.add(new Point2I(11, 11));
        b.exception.add(new Point2I(10, 10));

        a.visitFieldOfView(b, 10, 11, 5);
        // b.print(5, 15, 6, 16);
        // System.out.println();

        assertTrue(b.visited.contains(new Point2I(11, 10)));
    }

    public void testLarge()
    {
        final TestBoard b = new TestBoard(false);

        final Random rand = new Random();
        for (int i = 0; i < 100; i++)
        {
            b.exception.add(new Point2I(rand.nextInt(81) + 60, rand.nextInt(81) + 60));
        }

        final long t1 = System.currentTimeMillis();
        a.visitFieldOfView(b, 100, 100, 40);
        final long t2 = System.currentTimeMillis();

        System.out.println("Large Test took " + (t2 - t1));
        System.out.println("Chk b4 visit " + b.chkb4visit.size());
        System.out.println("Chk b4 visit fails for circular fov in PrecisePermissive");
        // System.out.println(b.chkb4visit);
        System.out.println("Error visit " + b.visiterr.size());
    }

}
