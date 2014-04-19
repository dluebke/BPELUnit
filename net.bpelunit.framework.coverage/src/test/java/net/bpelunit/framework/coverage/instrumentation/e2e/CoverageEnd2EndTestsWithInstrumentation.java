package net.bpelunit.framework.coverage.instrumentation.e2e;

import net.bpelunit.framework.coverage.CoverageLifeCycle;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.execution.IBPELUnitContext;

import org.junit.Test;

public class CoverageEnd2EndTestsWithInstrumentation {

	@Test
	public void testSimpleReceiveAssignReply() throws DeploymentException {
		IBPELUnitContext context = new DummyContext();
		CoverageLifeCycle c = new CoverageLifeCycle();
		c.doLoad(context);
		c.doMarkProcesses(context);
		c.doPrepareProcesses(context);
		c.doRegisterMocks(context);
		
	}

	
}
