/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.los.test;

import org.junit.jupiter.api.Test;
import rlforj.los.ConePrecisePermissive;
import rlforj.los.IConeFovAlgorithm;
import rlforj.los.ShadowCasting;
import rlforj.math.Point;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConeFovTest
{
    private void checkCone(final IConeFovAlgorithm a)
    {
        final TestBoard b = new TestBoard(false);

        b.exception.add(new Point(15, 15));
        b.exception.add(new Point(15, 16));
        b.exception.add(new Point(16, 15));

        // angles grow clockwise, positive y is down: this cone points down-right
        a.visitConeFieldOfView(b, 10, 10, 10, -10, 100);

        // origin always visited
        assertTrue(b.visited.contains(new Point(10, 10)));

        // cells well within the cone are visited
        assertTrue(b.visited.contains(new Point(14, 10)));
        assertTrue(b.visited.contains(new Point(10, 14)));
        assertTrue(b.visited.contains(new Point(13, 13)));

        // cells in the opposite direction are not
        assertFalse(b.visited.contains(new Point(5, 10)));
        assertFalse(b.visited.contains(new Point(10, 5)));
        assertFalse(b.visited.contains(new Point(6, 6)));

        // no cell is visited twice
        assertTrue(b.visiterr.isEmpty(), "Cells visited more than once: " + b.visiterr);
    }

    @Test
    public void testConeShadowCasting()
    {
        checkCone(new ShadowCasting());
    }

    @Test
    public void testConePrecisePermissive()
    {
        checkCone(new ConePrecisePermissive());
    }
}
