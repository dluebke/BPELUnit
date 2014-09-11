package net.bpelunit.framework.control.soap.genericsoapheaderprocessor;

import static org.junit.Assert.assertEquals;
import net.bpelunit.framework.control.ext.IHeaderProcessor;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.test.util.TestTestRunner;

import org.junit.Test;

public class GenericSOAPHeaderProcessorIntegrationTest {

	private static final String BASEPATH = "src/test/resources";

	@Test
	public void successfully_runs_test_suite() throws Exception {
		TestTestRunner runner = new TestTestRunner(BASEPATH, "suite-genericspoapheaderprocessor.bpts") {
			@Override
			public IHeaderProcessor createNewHeaderProcessor(String name) throws SpecificationException {
				if("generic".equals(name)) {
					return new GenericSOAPHeaderProcessor();
				} else {
					return null;
				}
			}
		};
		
		runner.testRun();
		
		assertEquals(2, runner.getPassed());
		assertEquals(0, runner.getProblems());
	}
	
}
