/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.framework.control.deploy.wps;

import net.bpelunit.framework.control.deploy.IBPELDeployer;
import net.bpelunit.framework.control.deploy.IBPELDeployer.IBPELDeployerCapabilities;
import net.bpelunit.framework.control.deploy.IDeployment;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.model.ProcessUnderTest;

/**
 * This class is the deployer for Websphere Process Server. It contains the
 * logic to deploy and undeploy WPS Deployments as part of a BPELUnit test run
 * 
 * @author Daniel Luebke <bpelunit@daniel-luebke.de>
 * @author Anup
 */
@IBPELDeployerCapabilities(canDeploy = true, canIntroduceMocks = false, canMeasureTestCoverage = false)
public class WPSDeployer implements IBPELDeployer {

	private String deploymentLocation;

	@IBPELDeployerOption(description="The file path to the deployment", testSuiteSpecific=false)
	public void setDeploymentLocation(String deploymentLocation) {
		this.deploymentLocation = deploymentLocation;
	}
	
	@Override
	public void deploy(String pathToTest, ProcessUnderTest processUnderTest)
			throws DeploymentException {
		checkThatSpecified(deploymentLocation, "The DeploymentLocation option must be specified in the test suite to refer to the deployment package.");
		
		// TODO Implement setter with @@IBPELDeployerOption for transfering options, e.g. server etc. like setDeploymentLocation
		
		// TODO Implement deployment logic
	}

	/**
	 * This is a helper method that can be used to check whether a mandatory deployment option is set. It will
	 * throw a DeploymentException with the given message if value is null or empty.
	 * 
	 * @param value value to be checked
	 * @param message message of the DeploymentException to be thrown if value is null or empty
	 * @throws DeploymentException thrown if value is null or empty
	 */
	private void checkThatSpecified(String value, String message)
			throws DeploymentException {
		if (value == null || "".equals(value)) {
			throw new DeploymentException(message);
		}
	}

	@Override
	public void undeploy(String testPath, ProcessUnderTest processUnderTest)
			throws DeploymentException {
		// TODO Implement if necessary
	}

	public IDeployment getDeployment(ProcessUnderTest processUnderTest)
			throws DeploymentException {
		// TODO Implement for enabling test coverage measurement and mock support
		throw new DeploymentException("The WPS deployer currently does not allow to change the Deployment Unit!");
	}

	@Override
	public void cleanUpAfterTestCase() {
		// TODO Implement if necessary, for basic functionality this should not be necessary
	}

}
