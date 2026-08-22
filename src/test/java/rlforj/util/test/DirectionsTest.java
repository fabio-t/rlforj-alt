/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.util.test;

import org.junit.jupiter.api.Test;
import rlforj.util.Directions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectionsTest
{
    @Test
    public void testOffsets()
    {
        assertEquals(0, Directions.NORTH.dx());
        assertEquals(1, Directions.NORTH.dy());
        assertEquals(0, Directions.SOUTH.dx());
        assertEquals(-1, Directions.SOUTH.dy());
        assertEquals(1, Directions.EAST.dx());
        assertEquals(0, Directions.EAST.dy());
        assertEquals(-1, Directions.WEST.dx());
        assertEquals(0, Directions.WEST.dy());
    }

    @Test
    public void testNeighbourhoods()
    {
        assertEquals(4, Directions.N4.length);
        assertEquals(8, Directions.N8.length);

        // every direction is a unit step
        for (final Directions d : Directions.N8)
        {
            assertTrue(Math.abs(d.dx()) <= 1 && Math.abs(d.dy()) <= 1);
            assertTrue(Math.abs(d.dx()) + Math.abs(d.dy()) >= 1);
        }
    }
}
