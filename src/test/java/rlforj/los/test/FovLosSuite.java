package rlforj.los.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Currently not working
 * @author sdatta
 *
 */
public class FovLosSuite extends TestSuite
{

	public FovLosSuite() {
		addTestSuite(PrecisePermissiveTest.class);
	}
	public static Test suite() {
		TestSuite s=new TestSuite();
		s.addTestSuite(PrecisePermissiveTest.class);
		s.addTestSuite(ShadowCastingTest.class);
		return s;
	}
}
