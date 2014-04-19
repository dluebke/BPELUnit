package net.bpelunit.framework.control.deploy.activevos9;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import net.bpelunit.activevos9.ActiveVOSAdministrativeFunctions;

import active_endpoints.docs.wsdl.activebpeladmin._2007._01.activebpeladmin.AdminFaultMsg;
import active_endpoints.docs.wsdl.activebpeladmin._2007._01.activebpeladmin.IAeAxisActiveBpelAdmin;

import com.active_endpoints.docs.wsdl.engineapi._2010._05.enginemanagement.AdminAPIFault;
import com.active_endpoints.docs.wsdl.engineapi._2010._05.enginemanagement.IAeContributionManagement;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesAddAttachmentDataType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesAddAttachmentResponseType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesBreakpointRequestType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesCompleteActivityType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesConfigurationType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesDeployBprType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesDigestType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesEngineRequestType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesGetVariableDataType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessContainerType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessCountType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessDetailType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessFilterType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessListType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessObjectType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessRequestType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesRemoveAttachmentDataType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesRemoveBreakpointRequestType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesRetryActivityType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesServerLogFilterType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesServerLogListType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesServerSettingsType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesSetCorrelationType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesSetPartnerLinkType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesSetVariableDataType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesStringResponseType;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContribution;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContributionDetail;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContributionListResult;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContributionSearchFilter;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesVoidType;
import com.active_endpoints.schemas.engineapi._2010._05.enginemanagementtypes.AesDeleteContributionType;
import com.active_endpoints.schemas.engineapi._2010._05.enginemanagementtypes.AesSetOnlineType;

public class ActiveVOSAdministrativeFunctionsMock extends
		ActiveVOSAdministrativeFunctions {

	private String password;
	private String username;
	private String endpoint;
	private List<MethodCall> methodCalls = new ArrayList<MethodCall>();
	private AeAxisActiveBpelAdminMock adminMock = new AeAxisActiveBpelAdminMock();
	private AeContributionManagementMock contributionManagementMock = new AeContributionManagementMock();

	private class AeAxisActiveBpelAdminMock implements IAeAxisActiveBpelAdmin {

		@Override
		public AesStringResponseType deployBpr(AesDeployBprType deployBprInput) {
			methodCalls.add(new MethodCall("deployBpr", deployBprInput));

			AesStringResponseType response = new AesStringResponseType();
			response.setResponse("<deploymentSummary numErrors=\"0\" numWarnings=\"0\"><globalMessages>[bpelunit-tc1.bpr] [bpelunit-tc1.bpr] Skipping BPR archive deployment as the current version is already up to date.</globalMessages></deploymentSummary>");
			return response;
		}

		@Override
		@WebMethod(operationName = "GetConfiguration")
		@WebResult(name = "getConfigurationOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesConfigurationType getConfiguration(
				@WebParam(name = "getConfigurationInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesVoidType input)
				throws AdminFaultMsg {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "SetConfiguration")
		public void setConfiguration(
				@WebParam(name = "setConfigurationInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesConfigurationType input)
				throws AdminFaultMsg {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "GetServerSettings")
		@WebResult(name = "getServerSettingsOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesServerSettingsType getServerSettings(
				@WebParam(name = "getServerSettingsInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesVoidType input)
				throws AdminFaultMsg {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "SetServerSettings")
		public void setServerSettings(
				@WebParam(name = "setServerSettingsInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesServerSettingsType input)
				throws AdminFaultMsg {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "SuspendProcess")
		public void suspendProcess(
				@WebParam(name = "suspendProcessInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessType input)
				throws AdminFaultMsg {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "ResumeProcess")
		public void resumeProcess(
				@WebParam(name = "resumeProcessInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessType input)
				throws AdminFaultMsg {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "GotoProcessObject")
		public void gotoProcessObject(
				@WebParam(name = "gotoProcessObjectInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessObjectType input)
				throws AdminFaultMsg {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "ResumeProcessObject")
		public void resumeProcessObject(
				@WebParam(name = "resumeProcessObjectInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessObjectType input)
				throws AdminFaultMsg {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "ResumeProcessContainer")
		public void resumeProcessContainer(
				@WebParam(name = "resumeProcessContainerInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessContainerType input)
				throws AdminFaultMsg {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "TerminateProcess")
		public void terminateProcess(
				@WebParam(name = "terminateProcessInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessType input)
				throws AdminFaultMsg {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "AddEngineListener")
		public void addEngineListener(
				@WebParam(name = "addEngineListenerInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesEngineRequestType input) {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "AddBreakpointListener")
		public void addBreakpointListener(
				@WebParam(name = "addBreakpointListenerInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesBreakpointRequestType input) {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "UpdateBreakpointList")
		public void updateBreakpointList(
				@WebParam(name = "updateBreakpointListenerInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesBreakpointRequestType input) {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "RemoveEngineListener")
		public void removeEngineListener(
				@WebParam(name = "removeEngineListenerInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesEngineRequestType input) {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "RemoveBreakpointListener")
		public void removeBreakpointListener(
				@WebParam(name = "removeBreakpointListenerInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesRemoveBreakpointRequestType input) {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "AddProcessListener")
		public void addProcessListener(
				@WebParam(name = "addProcessListenerInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessRequestType input) {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "RemoveProcessListener")
		public void removeProcessListener(
				@WebParam(name = "removeProcessListenerInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessRequestType input) {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "GetVariable")
		@WebResult(name = "getVariableDataOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesStringResponseType getVariable(
				@WebParam(name = "getVariableDataInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesGetVariableDataType input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "SetVariable")
		@WebResult(name = "setVariableDataOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesStringResponseType setVariable(
				@WebParam(name = "setVariableDataInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesSetVariableDataType input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "AddAttachment")
		@WebResult(name = "addAttachmentDataOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesAddAttachmentResponseType addAttachment(
				@WebParam(name = "addAttachmentDataInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesAddAttachmentDataType input,
				@WebParam(name = "attachment", targetNamespace = "", partName = "attachment") byte[] attachment) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "RemoveAttachments")
		@WebResult(name = "removeAttachmentDataOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesStringResponseType removeAttachments(
				@WebParam(name = "removeAttachmentDataInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesRemoveAttachmentDataType input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "GetProcessList")
		@WebResult(name = "getProcessListOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesProcessListType getProcessList(
				@WebParam(name = "getProcessListInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessFilterType input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "GetProcessCount")
		@WebResult(name = "getProcessCountOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesProcessCountType getProcessCount(
				@WebParam(name = "getProcessCountInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessFilterType input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "GetProcessDetail")
		@WebResult(name = "getProcessDetailOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesProcessDetailType getProcessDetail(
				@WebParam(name = "getProcessDetailInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessType input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "GetProcessState")
		@WebResult(name = "getProcessStateOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesStringResponseType getProcessState(
				@WebParam(name = "getProcessStateInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessType input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "GetProcessDigest")
		@WebResult(name = "getProcessDigestOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesDigestType getProcessDigest(
				@WebParam(name = "getProcessDigestInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessType input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "GetProcessDef")
		@WebResult(name = "getProcessDefOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesStringResponseType getProcessDef(
				@WebParam(name = "getProcessDefInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessType input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "GetProcessLog")
		@WebResult(name = "getProcessLogOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesStringResponseType getProcessLog(
				@WebParam(name = "getProcessLogInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesProcessType input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "GetAPIVersion")
		@WebResult(name = "getAPIVersionOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesStringResponseType getAPIVersion(
				@WebParam(name = "getAPIVersionInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesVoidType input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "SetPartnerLinkData")
		public void setPartnerLinkData(
				@WebParam(name = "setPartnerLinkDataInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesSetPartnerLinkType input) {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "SetCorrelationSetData")
		public void setCorrelationSetData(
				@WebParam(name = "setCorrelationDataInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesSetCorrelationType input) {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "RetryActivity")
		public void retryActivity(
				@WebParam(name = "retryActivityInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesRetryActivityType input) {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "CompleteActivity")
		public void completeActivity(
				@WebParam(name = "completeActivityInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesCompleteActivityType input) {
			// TODO Auto-generated method stub
			
		}

		@Override
		@WebMethod(operationName = "GetServerLogList")
		@WebResult(name = "getServerLogListOutput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "output")
		public AesServerLogListType getServerLogList(
				@WebParam(name = "getServerLogListInput", targetNamespace = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd", partName = "input") AesServerLogFilterType input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "ClearServerLog")
		public void clearServerLog() throws AdminFaultMsg {
			// TODO Auto-generated method stub
			
		}
	}

	private final class AeContributionManagementMock implements
			IAeContributionManagement {
		private AesContributionListResult result = new AesContributionListResult();

		@Override
		public AesVoidType takePlanOffline(int input) throws AdminAPIFault {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AesVoidType takeContributionOffline(int input)
				throws AdminAPIFault {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AesVoidType setPlanOnline(int input) throws AdminAPIFault {
			return null;
		}

		@Override
		public AesVoidType setContributionOnline(AesSetOnlineType input)
				throws AdminAPIFault {
			return null;
		}

		@Override
		public AesContributionListResult searchContributions(
				AesContributionSearchFilter input) {
			result.setCompleteRowCount(true);
			result.setTotalRowCount(1);

			AesContribution contribution = new AesContribution();
			contribution.setId(1000);
			result.getContributionItem().add(contribution);
			return result;
		}

		@Override
		public int purgeOffline(AesVoidType input) throws AdminAPIFault {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getDeploymentLog(int input) throws AdminAPIFault {
			return null;
		}

		@Override
		public AesContributionDetail getContributionDetail(int input) {
			return null;
		}

		@Override
		public AesVoidType deletePlan(int input) throws AdminAPIFault {
			return null;
		}

		@Override
		public AesVoidType deleteContribution(AesDeleteContributionType input)
				throws AdminAPIFault {
			methodCalls.add(new MethodCall("deleteContribution", input));
			return new AesVoidType();
		}
	}

	public static class MethodCall {
		public MethodCall(String methodName, Object... parameters) {
			super();
			this.methodName = methodName;
			this.parameters = parameters;
		}

		public String methodName;
		public Object[] parameters;
	}

	public ActiveVOSAdministrativeFunctionsMock(String endpoint,
			String username, String password) {
		super(endpoint, username, password);

		this.endpoint = endpoint;
		this.username = username;
		this.password = password;
	}

	protected String getPassword() {
		return password;
	}

	protected String getUsername() {
		return username;
	}

	protected String getEndpoint() {
		return endpoint;
	}

	@Override
	protected IAeAxisActiveBpelAdmin getActiveBpelAdminPort() {
		return this.adminMock;
	}

	@Override
	protected IAeContributionManagement getContributionManagementPort() {
		return this.contributionManagementMock;
	}
	
	public List<MethodCall> getMethodsCalls() {
		return this.methodCalls;
	}

}
