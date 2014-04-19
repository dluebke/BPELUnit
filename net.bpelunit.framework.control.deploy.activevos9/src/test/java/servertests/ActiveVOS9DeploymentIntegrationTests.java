package servertests;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;

import net.bpelunit.framework.control.deploy.IBPELProcess;
import net.bpelunit.framework.control.deploy.IDeployment;
import net.bpelunit.framework.control.deploy.activevos9.ActiveVOS9Deployer;
import net.bpelunit.framework.exception.DeploymentException;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContribution;

public class ActiveVOS9DeploymentIntegrationTests {

	private static final String activevos9Endpoint = "http://localhost:8080/active-bpel/services/";
	
	private ActiveVOS9Deployer deployer;

	private List<AesContribution> previouslyDeployedContributions;
	
	@Before
	public void setUp() {
		this.deployer = new ActiveVOS9Deployer();
		deployer.setDeploymentServiceEndpoint(activevos9Endpoint);
		try {
			previouslyDeployedContributions = deployer.getAdministrativeFunctions().getAllContributions();
		} catch(Exception e) {
			Assume.assumeNoException(e);
		}
	}
	
	@Test
	public void sucessfully_deploy_and_undeploy_unmodified_bpr() throws DeploymentException, InterruptedException {
		deployer.setDoUndeploy("true");
		deployer.setDeploymentLocation("src/test/resources/e2e/simple-process.bpr");
		
		deployer.deploy(".", null);
		
		List<AesContribution> currentlyDeployedContributions = deployer.getAdministrativeFunctions().getAllContributions();
		assertEquals(previouslyDeployedContributions.size() + 1, currentlyDeployedContributions.size());
		
		deployer.undeploy(".", null);
		
		// ActiveVOS uses deferred deletion of contributions. We must wait before checking if it was indeed deleted
		Thread.sleep(5000);
		currentlyDeployedContributions = deployer.getAdministrativeFunctions().getAllContributions();
		assertEquals(previouslyDeployedContributions.size(), currentlyDeployedContributions.size());
	}
	
	@Test
	public void sucessfully_deploy_and_undeploy_bpr_after_adding_partnerlink_with_process_role() throws Exception {
		deployer.setDoUndeploy("true");
		deployer.setDeploymentLocation("src/test/resources/e2e/simple-process.bpr");
		
		IDeployment deployment = deployer.getDeployment(null);
		List<? extends IBPELProcess> processes = deployment.getBPELProcesses();
		assertEquals(1, processes.size());
		URL processEndpoint = processes.get(0).addPartnerlink("NewPL", new QName("http://test.bpelunit.net/simple/", "Test"), "test", "BPELUnit_Test2", null, null, null, null);
		assertEquals(new URL(activevos9Endpoint + "BPELUnit_Test2"), processEndpoint);
		
		deployer.deploy(".", null);
		
		List<AesContribution> currentlyDeployedContributions = deployer.getAdministrativeFunctions().getAllContributions();
		assertEquals(previouslyDeployedContributions.size() + 1, currentlyDeployedContributions.size());
		
		deployer.undeploy(".", null);
		
		// ActiveVOS uses deferred deletion of contributions. We must wait before checking if it was indeed deleted
		Thread.sleep(5000);
		currentlyDeployedContributions = deployer.getAdministrativeFunctions().getAllContributions();
		assertEquals(previouslyDeployedContributions.size(), currentlyDeployedContributions.size());
	}
	
	@Test
	public void sucessfully_deploy_and_undeploy_bpr_after_adding_partnerlink_with_partner_role() throws Exception {
		deployer.setDoUndeploy("true");
		deployer.setDeploymentLocation("src/test/resources/e2e/simple-process.bpr");
		
		IDeployment deployment = deployer.getDeployment(null);
		List<? extends IBPELProcess> processes = deployment.getBPELProcesses();
		assertEquals(1, processes.size());
		processes.get(0).addPartnerlink("NewPL", new QName("http://test.bpelunit.net/simple/", "Test"), null, null, "test", new QName("http://test.bpelunit.net/simple/", "simple"), "simpleSOAP", "http://localhost:7777/ws/simple");
		
		deployer.deploy(".", null);
		
		List<AesContribution> currentlyDeployedContributions = deployer.getAdministrativeFunctions().getAllContributions();
		assertEquals(previouslyDeployedContributions.size() + 1, currentlyDeployedContributions.size());
		
		deployer.undeploy(".", null);
		
		// ActiveVOS uses deferred deletion of contributions. We must wait before checking if it was indeed deleted
		Thread.sleep(5000);
		currentlyDeployedContributions = deployer.getAdministrativeFunctions().getAllContributions();
		assertEquals(previouslyDeployedContributions.size(), currentlyDeployedContributions.size());
	}

}
