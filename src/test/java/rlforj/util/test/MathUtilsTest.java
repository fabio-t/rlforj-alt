/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.util.test;

import junit.framework.TestCase;
import rlforj.util.MathUtils;

public class MathUtilsTest extends TestCase
{

    public void testISqrt()
    {
        final int LIM = 1000000;

        final long start = System.currentTimeMillis();
        for (int i = 0; i < LIM; i++)
        {
            final int j = MathUtils.isqrt(i);
            final int k = (int) Math.floor(Math.sqrt(i));

            assertTrue("Sqrt of " + i + " supposed to be " + k + " but is " + j, j == k);
        }
        final long end = System.currentTimeMillis();

        System.out.println("Time taken " + (end - start));
    }
}
