/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.los.test;

import org.junit.jupiter.api.Test;
import rlforj.los.IFovAlgorithm;
import rlforj.los.PrecisePermissive;
import rlforj.los.ShadowCasting;
import rlforj.math.Point;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FOV radius handling, including radii larger than any precomputed cache.
 */
public class FovRadiusTest
{
    private void checkRadius(final IFovAlgorithm a, final int radius)
    {
        final TestBoard b = new TestBoard(false);

        a.visitFoV(b, 200, 200, radius);

        assertTrue(b.visited.contains(new Point(200, 200)), "origin visited");
        assertTrue(b.visited.contains(new Point(200 + radius, 200)), "cell at radius visited");
        assertTrue(b.visited.contains(new Point(200 - radius, 200)), "cell at radius visited");
        assertTrue(b.visited.contains(new Point(200, 200 + radius)), "cell at radius visited");
        assertFalse(b.visited.contains(new Point(200 + radius + 2, 200)), "cell beyond radius not visited");
        assertTrue(b.visiterr.isEmpty(), "cells visited more than once: " + b.visiterr);
    }

    @Test
    public void testShadowCastingSmallRadius()
    {
        checkRadius(new ShadowCasting(), 10);
    }

    @Test
    public void testShadowCastingLargeRadius()
    {
        // regression: used to throw NullPointerException for
        // any radius above the precomputed 40
        checkRadius(new ShadowCasting(), 60);
    }

    @Test
    public void testPrecisePermissiveSmallRadius()
    {
        checkRadius(new PrecisePermissive(), 10);
    }

    @Test
    public void testPrecisePermissiveLargeRadius()
    {
        checkRadius(new PrecisePermissive(), 60);
    }
}
