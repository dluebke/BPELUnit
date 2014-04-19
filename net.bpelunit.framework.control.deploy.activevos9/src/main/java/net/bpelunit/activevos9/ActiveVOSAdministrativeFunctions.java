/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.activevos9;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.BindingProvider;

import net.bpelunit.util.JAXBUtil;
import net.bpelunit.util.XMLUtil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import active_endpoints.docs.wsdl.activebpeladmin._2007._01.activebpeladmin.ActiveBpelAdmin;
import active_endpoints.docs.wsdl.activebpeladmin._2007._01.activebpeladmin.IAeAxisActiveBpelAdmin;

import com.active_endpoints.docs.wsdl.engineapi._2010._05.enginemanagement.AdminAPIFault;
import com.active_endpoints.docs.wsdl.engineapi._2010._05.enginemanagement.ContributionManagementService;
import com.active_endpoints.docs.wsdl.engineapi._2010._05.enginemanagement.IAeContributionManagement;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesDeployBprType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessFilter;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessFilterType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessInstanceDetail;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessListType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesStringResponseType;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContribution;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContributionListResult;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContributionSearchFilter;
import com.active_endpoints.schemas.engineapi._2010._05.enginemanagementtypes.AesDeleteContributionType;

public class ActiveVOSAdministrativeFunctions {

	@SuppressWarnings("serial")
	public class DeployException extends Exception {
		public DeployException(String message, Throwable t) {
			super(message, t);
		}

		public DeployException(String message) {
			super(message);
		}
	}
	
	private static final QName ACTIVE_BPEL_ADMIN_SERVICE_NAME = new QName(
			"http://docs.active-endpoints/wsdl/activebpeladmin/2007/01/activebpeladmin.wsdl",
			"ActiveBpelAdmin");
	private static final QName CONTRIBUTION_MANAGEMENT_SERVICE_NAME = new QName(
			"http://docs.active-endpoints.com/wsdl/engineapi/2010/05/EngineManagement.wsdl",
			"ContributionManagementService");

	private static final String ACTIVE_BPEL_ADMIN_WSDL_RESOURCE = "/ActiveBpelAdmin.wsdl";
	private static final String CONTRIBUTION_MANAGEMENT_SERVICE_WSDL_RESOURCE = "/AeContributionManagement.wsdl";
	
	private static final String ATTRIBUTE_ERROR_COUNT = "numErrors";
	private static final String ENDPOINT_PATH_ACTIVE_BPEL_ADMIN_SERVICE = "/ActiveBpelAdmin";
	private static final String ENDPOINT_PATH_CONTRIBUTION_MANAGEMENT_SERVICE = "/AeContributionManagement";
	private static final String ENDPOINT_PATH_ACTIVE_BPEL_DEPLOY_SERVICE = "/ActiveBpelDeployBPR";
	
	private String baseEndpoint;
	
	private IAeAxisActiveBpelAdmin activeBpelAdminPort;
	private IAeContributionManagement contributionManagementPort;

	public ActiveVOSAdministrativeFunctions(String endpoint, String username,
			String password) {
		calculateBaseEndpoint(endpoint);
		
		initializeActiveBpelAdminPort(username, password, baseEndpoint + ENDPOINT_PATH_ACTIVE_BPEL_ADMIN_SERVICE);
		initializeContributionManagementPort(username, password, baseEndpoint + ENDPOINT_PATH_CONTRIBUTION_MANAGEMENT_SERVICE);
	}

	private void calculateBaseEndpoint(String endpoint) {
		if(endpoint.endsWith(ENDPOINT_PATH_ACTIVE_BPEL_ADMIN_SERVICE)) {
			baseEndpoint = endpoint.substring(0, endpoint.length() - ENDPOINT_PATH_ACTIVE_BPEL_ADMIN_SERVICE.length() + 1);
		} else if(endpoint.endsWith(ENDPOINT_PATH_ACTIVE_BPEL_DEPLOY_SERVICE)) {
			baseEndpoint = endpoint.substring(0, endpoint.length() - ENDPOINT_PATH_ACTIVE_BPEL_DEPLOY_SERVICE.length() + 1);
		} else {
			baseEndpoint = endpoint;
		}
		
		if(baseEndpoint.endsWith("/")) {
			baseEndpoint = baseEndpoint.substring(0, baseEndpoint.length() - 1);
		}
	}

	private void initializeContributionManagementPort(String username,
			String password, String endpoint) {
		ContributionManagementService contributionManagementService = new
		ContributionManagementService(ActiveVOSAdministrativeFunctions.class
				.getResource(CONTRIBUTION_MANAGEMENT_SERVICE_WSDL_RESOURCE), CONTRIBUTION_MANAGEMENT_SERVICE_NAME);
		
		contributionManagementPort = contributionManagementService.getContributionManagementPort();
		setBasicAuthenticationForBindingProvider(contributionManagementPort, username,
				password);
		setEndpointForBindingProvider(contributionManagementPort, endpoint);
	}

	private void initializeActiveBpelAdminPort(String username, String password, String endpoint) {
		ActiveBpelAdmin activeBpelAdmin = new ActiveBpelAdmin(
				ActiveVOSAdministrativeFunctions.class
						.getResource(ACTIVE_BPEL_ADMIN_WSDL_RESOURCE),
				ACTIVE_BPEL_ADMIN_SERVICE_NAME);

		activeBpelAdminPort = activeBpelAdmin.getActiveBpelAdmin();
		setBasicAuthenticationForBindingProvider(activeBpelAdminPort, username,
				password);
		setEndpointForBindingProvider(activeBpelAdminPort, endpoint);
	}

	private void setEndpointForBindingProvider(Object port, String endpoint) {
		Map<String, Object> requestContext = ((BindingProvider) port)
				.getRequestContext();
		requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
	}

	private void setBasicAuthenticationForBindingProvider(Object port,
			String username, String password) {
		Map<String, Object> requestContext = ((BindingProvider) port)
				.getRequestContext();
		requestContext.put(BindingProvider.USERNAME_PROPERTY, username);
		requestContext.put(BindingProvider.PASSWORD_PROPERTY, password);
	}

	public List<AesContribution> getAllContributions() {
		AesContributionSearchFilter input = new AesContributionSearchFilter();
		AesContributionListResult searchContributions = getContributionManagementPort().searchContributions(input);
				
		return searchContributions.getContributionItem();
	}
	
	public List<Integer> extractContributionIds(List<AesContribution> contributions) {
		List<Integer> result = new ArrayList<Integer>();
		for(AesContribution c : contributions) {
			result.add(c.getId());
		}
		
		return result;
	}

	public void takeContributionOffline(int contributionId) throws AdminAPIFault {
		contributionManagementPort.takeContributionOffline(contributionId);
	}
	
	public void deleteContribution(int contributionId, boolean deleteProcesses) throws AdminAPIFault {
		AesDeleteContributionType input = new AesDeleteContributionType();
		input.setContributionId(contributionId);
		input.setDeleteProcesses(deleteProcesses);
		contributionManagementPort.deleteContribution(input);
	}
	
	public void deployBpr(String bprFileName, InputStream contents) throws DeployException {
		try {
			deployBpr(bprFileName, IOUtils.toByteArray(contents));
		} catch (IOException e) {
			throw new DeployException("Error reading BPR : " + e.getMessage(), e);
		}
	}

	public void deployBpr(String bprFileName, byte[] contents) throws DeployException {
		AesDeployBprType deployBprInput = new AesDeployBprType();
		AesStringResponseType deployBpr;
		
		deployBprInput.setBprFilename(bprFileName);
		deployBprInput.setBase64File(new String(Base64.encodeBase64(contents)));
		deployBpr = getActiveBpelAdminPort().deployBpr(deployBprInput);

		String responseMessage = deployBpr.getResponse();
		try {
			Document xml = XMLUtil.parseXML(responseMessage);
			Node item = xml.getFirstChild().getAttributes()
			.getNamedItem(ATTRIBUTE_ERROR_COUNT);
			if (Integer.parseInt(item.getTextContent()) != 0) {
				throw new DeployException("Errors while deploying process: "
					+ responseMessage);
			}
		} catch (ParserConfigurationException e) {
			throw new DeployException("Internal error: " + e.getMessage(), e);
		} catch (SAXException e) {
			throw new DeployException("Internal error while reading response XML: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new DeployException(e.getMessage(), e);
		} 
	}

	public void terminateAllProcessInstances() {
		// TODO Auto-generated method stub
	}
	
	public List<AesProcessInstanceDetail> getProcessInstancesStartedBetween(Date from, Date till) {
		AesProcessFilter filter = new AesProcessFilter();
		filter.setProcessCreateStart(JAXBUtil.convertToXMLDateTime(from));
		filter.setProcessCreateEnd(JAXBUtil.convertToXMLDateTime(till));
		AesProcessFilterType input = new AesProcessFilterType();
		input.setFilter(filter);
		AesProcessListType processList = activeBpelAdminPort.getProcessList(input);
		return processList.getResponse().getRowDetails().getItem();
	}
	
	public String getLogForProcessInstance(long pid) {
		AesProcessType input = new AesProcessType();
		input.setPid(pid);
		AesStringResponseType processLog = activeBpelAdminPort.getProcessLog(input);
		return processLog.getResponse();
	}
	

	public String getActiveBPELURL() {
		return baseEndpoint + "/";
	}
	
	//=================================
	// Accessors for testing only
	//=================================

	/**
	 * For testing only
	 */
	protected IAeAxisActiveBpelAdmin getActiveBpelAdminPort() {
		return activeBpelAdminPort;
	}

	/**
	 * For testing only
	 */
	protected IAeContributionManagement getContributionManagementPort() {
		return contributionManagementPort;
	}

}
