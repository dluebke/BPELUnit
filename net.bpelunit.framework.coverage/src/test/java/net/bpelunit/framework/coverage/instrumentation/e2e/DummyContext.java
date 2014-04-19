package net.bpelunit.framework.coverage.instrumentation.e2e;

import java.net.URL;
import java.util.List;

import org.eclipse.jetty.server.handler.AbstractHandler;

import net.bpelunit.framework.control.deploy.IDeployment;
import net.bpelunit.framework.execution.IBPELUnitContext;
import net.bpelunit.framework.execution.ITestLifeCycleElement;
import net.bpelunit.framework.model.test.TestSuite;

public class DummyContext implements IBPELUnitContext {

	@Override
	public TestSuite getTestSuite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDeployment getDeployment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL addService(String name, AbstractHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getURLForService(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends ITestLifeCycleElement> getElementsInLoad() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends ITestLifeCycleElement> getElementsInPrepareProcesses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends ITestLifeCycleElement> getElementsInStartMocks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends ITestLifeCycleElement> getElementsInDeploy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends ITestLifeCycleElement> getElementsInRunTests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends ITestLifeCycleElement> getElementsInStopMocks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends ITestLifeCycleElement> getElementsInReport() {
		// TODO Auto-generated method stub
		return null;
	}

}
