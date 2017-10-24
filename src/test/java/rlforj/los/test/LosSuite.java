package rlforj.los.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Author: Fabio Ticconi
 * Date: 24/10/17
 */
public class LosSuite extends TestSuite
{
    public LosSuite()
    {
        addTestSuite(LosPrecisePermissiveTest.class);
    }

    public static Test suite()
    {
        TestSuite s = new TestSuite();
        s.addTestSuite(LosPrecisePermissiveTest.class);
        // s.addTestSuite(LosShadowCastingTest.class);
        return s;
    }
}
