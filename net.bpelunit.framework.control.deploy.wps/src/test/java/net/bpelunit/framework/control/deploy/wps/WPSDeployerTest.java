/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.framework.control.deploy.wps;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class WPSDeployerTest {

	private WPSDeployer deployer;
	
	@Before
	public void setUp() {
		this.deployer = new WPSDeployer();
	}
	
	@After
	public void tearDown() {
		this.deployer = null;
	}
	
	@Test
	@Ignore("To be implemented")
	public void testDeploy() throws Exception {
		
	}
	
}
