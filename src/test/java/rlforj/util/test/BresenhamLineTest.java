/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.util.test;

import org.junit.jupiter.api.Test;
import rlforj.util.BresenhamLine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BresenhamLineTest
{
    private int[][] plot(final int x1, final int y1, final int x2, final int y2)
    {
        final int   len = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1)) + 1;
        final int[] x   = new int[len];
        final int[] y   = new int[len];
        final int   n   = BresenhamLine.plot(x1, y1, x2, y2, x, y);
        assertEquals(len, n, "line length");
        return new int[][] { x, y };
    }

    @Test
    public void testHorizontalLine()
    {
        final int[][] line = plot(0, 0, 4, 0);
        for (int i = 0; i <= 4; i++)
        {
            assertEquals(i, line[0][i]);
            assertEquals(0, line[1][i]);
        }
    }

    @Test
    public void testVerticalLine()
    {
        final int[][] line = plot(2, 5, 2, 1);
        for (int i = 0; i <= 4; i++)
        {
            assertEquals(2, line[0][i]);
            assertEquals(5 - i, line[1][i]);
        }
    }

    @Test
    public void testDiagonalLine()
    {
        final int[][] line = plot(0, 0, -3, -3);
        for (int i = 0; i <= 3; i++)
        {
            assertEquals(-i, line[0][i]);
            assertEquals(-i, line[1][i]);
        }
    }

    @Test
    public void testEndpointsAndContinuity()
    {
        for (int x2 = -8; x2 <= 8; x2 += 3)
        {
            for (int y2 = -8; y2 <= 8; y2 += 3)
            {
                final int[][] line = plot(0, 0, x2, y2);
                final int     len  = Math.max(Math.abs(x2), Math.abs(y2)) + 1;

                assertEquals(0, line[0][0]);
                assertEquals(0, line[1][0]);
                assertEquals(x2, line[0][len - 1]);
                assertEquals(y2, line[1][len - 1]);

                for (int i = 1; i < len; i++)
                {
                    final int dx = Math.abs(line[0][i] - line[0][i - 1]);
                    final int dy = Math.abs(line[1][i] - line[1][i - 1]);
                    assertTrue(dx <= 1 && dy <= 1 && dx + dy >= 1, "discontinuous line");
                }
            }
        }
    }

    @Test
    public void testSinglePoint()
    {
        final int[][] line = plot(3, 3, 3, 3);
        assertEquals(3, line[0][0]);
        assertEquals(3, line[1][0]);
    }
}
