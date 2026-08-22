/*
 * Copyright (c) 2017, Fabio Ticconi, fabio.ticconi@gmail.com
 * Copyright (c) 2013, kba
 * All rights reserved.
 */

package rlforj.util.test;

import org.junit.jupiter.api.Test;
import rlforj.util.MathUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathUtilsTest
{
    @Test
    public void testISqrt()
    {
        final int LIM = 1000000;

        for (int i = 0; i < LIM; i++)
        {
            final int j = MathUtils.isqrt(i);
            final int k = (int) Math.floor(Math.sqrt(i));

            assertEquals(k, j, "Sqrt of " + i + " supposed to be " + k + " but is " + j);
        }
    }
}
