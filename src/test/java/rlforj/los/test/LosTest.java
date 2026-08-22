/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.los.test;

import org.junit.jupiter.api.Test;
import rlforj.los.ILosAlgorithm;
import rlforj.math.Point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing Los algorithms
 */
public abstract class LosTest
{
    protected ILosAlgorithm a;

    /**
     * Whether the algorithm can see a diagonally adjacent cell "around"
     * a pillar. True for all algorithms except plain (asymmetric)
     * Bresenham, which only follows the single start-to-end line.
     */
    protected boolean seesAroundPillar()
    {
        return true;
    }

    @Test
    public void testEmpty()
    {
        final TestBoard b = new TestBoard(false);

        // b.print(5, 15, 5, 15);
        // System.out.println();

        assertTrue(a.exists(b, 10, 10, 11, 11, false));
        assertTrue(a.exists(b, 10, 10, 10, 11, false));
        assertTrue(a.exists(b, 10, 10, 11, 10, false));
        assertTrue(a.exists(b, 10, 10, 10, 15, false));
        assertTrue(a.exists(b, 10, 10, 15, 10, false));
    }

    @Test
    public void testFull()
    {
        final TestBoard b = new TestBoard(true);

        // b.print(5, 15, 5, 15);
        // System.out.println();

        assertTrue(a.exists(b, 10, 10, 11, 11, false));
        assertTrue(a.exists(b, 10, 10, 10, 11, false));
        assertTrue(a.exists(b, 10, 10, 11, 10, false));
        assertFalse(a.exists(b, 10, 10, 10, 15, false));
        assertFalse(a.exists(b, 10, 10, 15, 10, false));
    }

    @Test
    public void testLine()
    {
        final TestBoard b = new TestBoard(true);

        for (int i = 5; i < 11; i++)
        {
            b.exception.add(new Point(i, 10));
        }

        // b.print(5, 15, 5, 15);
        // System.out.println();

        assertTrue(a.exists(b, 10, 10, 11, 11, false));
        assertTrue(a.exists(b, 10, 10, 10, 11, false));
        assertTrue(a.exists(b, 10, 10, 11, 10, false));
        assertTrue(a.exists(b, 10, 10, 5, 10, false));
        assertFalse(a.exists(b, 10, 10, 15, 10, false));
    }

    @Test
    public void testAcrossPillar()
    {
        final TestBoard b = new TestBoard(false);

        b.exception.add(new Point(10, 10));

        // b.print(4, 14, 4, 14);
        // System.out.println();

        assertEquals(seesAroundPillar(), a.exists(b, 9, 9, 10, 11, false));
        assertFalse(a.exists(b, 9, 9, 11, 11, false));
    }

    @Test
    public void testDiagonalWall()
    {
        final TestBoard b = new TestBoard(false);

        b.exception.add(new Point(11, 11));
        b.exception.add(new Point(10, 10));

        // b.print(5, 15, 6, 16);
        // System.out.println();
        assertTrue(a.exists(b, 10, 11, 11, 10, false));

    }
}
