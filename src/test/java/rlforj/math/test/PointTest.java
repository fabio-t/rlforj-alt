/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.math.test;

import org.junit.jupiter.api.Test;
import rlforj.math.Point;

import static org.junit.jupiter.api.Assertions.*;

public class PointTest
{
    @Test
    public void testEqualsAndHashCodeContract()
    {
        final Point a = new Point(3, 7);
        final Point b = new Point(3, 7);
        final Point c = new Point(7, 3);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, new Object());
    }

    @Test
    public void testHashCodeIsStableForNegativeAndLargeValues()
    {
        // regression: the old hashCode used a bit-shift with a
        // data-dependent shift amount, an operator precedence bug
        final Point a = new Point(-1000000, 999999);
        final Point b = new Point(-1000000, 999999);

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testChebyshevDistance()
    {
        final Point origin = new Point(0, 0);

        assertEquals(0, origin.distance(new Point(0, 0)));
        assertEquals(5, origin.distance(new Point(5, 3)));
        assertEquals(5, origin.distance(new Point(-5, 3)));
        assertEquals(7, origin.distance(new Point(2, -7)));
    }

    @Test
    public void testEuclideanDistance()
    {
        final Point origin = new Point(0, 0);

        assertEquals(0, origin.distance2(new Point(0, 0)));
        assertEquals(5, origin.distance2(new Point(3, 4)));
        assertEquals(5, origin.distance2(new Point(-3, -4)));
        assertEquals(1, origin.distance2(new Point(1, 1))); // floor(sqrt(2))
    }
}
