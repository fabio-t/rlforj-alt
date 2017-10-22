package rlforj.util.test;

import junit.framework.TestCase;
import rlforj.util.MathUtils;

public class MathUtilsTest extends TestCase
{

    public void testISqrt()
    {
        final int LIM = 1000000;

        long start = System.currentTimeMillis();
        for (int i = 0; i < LIM; i++)
        {
            int j = MathUtils.isqrt(i);
            int k = (int) Math.floor(Math.sqrt(i));

            assertTrue("Sqrt of " + i + " supposed to be " + k + " but is " + j, j == k);
        }
        long end = System.currentTimeMillis();

        System.out.println("Time taken " + (end - start));
    }
}
