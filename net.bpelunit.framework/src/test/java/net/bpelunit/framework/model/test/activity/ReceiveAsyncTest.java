package net.bpelunit.framework.model.test.activity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import net.bpelunit.framework.control.run.TestCaseRunner;
import net.bpelunit.framework.control.soap.DocumentLiteralEncoder;
import net.bpelunit.framework.control.soap.NamespaceContextImpl;
import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.AbstractPartner;
import net.bpelunit.framework.model.ProcessUnderTest;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.TestCase;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.framework.model.test.data.ReceiveCondition;
import net.bpelunit.framework.model.test.data.ReceiveDataSpecification;
import net.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import net.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

public class ReceiveAsyncTest {

	private ReceiveAsync r;
	private ActivityContext context;

	@Test
	public void testCouldFinishSuccessfullyWithNoAssertions() throws Exception {
		ArrayList<ReceiveCondition> conditions = new ArrayList<ReceiveCondition>();

		setUp(conditions);

		String message = "<a><b /></a>";
		assertTrue(r.getStatus().isInitial());
		assertTrue(r.couldFinishSuccessfully(context, wrapInSoapMessage(message)));
		assertTrue(r.getStatus().isInitial());
	}
	
	@Test
	public void testCouldFinishSuccessfullyWithCorrectCondition() throws Exception {
		ArrayList<ReceiveCondition> conditions = new ArrayList<ReceiveCondition>();
		
		ReceiveCondition rc = new ReceiveCondition(null, "count(//b)", null, "1");
		conditions.add(rc);
		
		setUp(conditions);
		
		String message = "<a><b /></a>";
		assertTrue(r.getStatus().isInitial());
		assertTrue(r.couldFinishSuccessfully(context, wrapInSoapMessage(message)));
		assertTrue(r.getStatus().isInitial());
	}
	
	@Test
	public void testCouldFinishSuccessfullyWithWrongCondition() throws Exception {
		ArrayList<ReceiveCondition> conditions = new ArrayList<ReceiveCondition>();

		ReceiveCondition rc = new ReceiveCondition(null, "count(//c)", null, "1");
		conditions.add(rc);
		
		setUp(conditions);
		
		String message = "<a><b /></a>";
		assertTrue(r.getStatus().isInitial());
		assertFalse(r.couldFinishSuccessfully(context, wrapInSoapMessage(message)));
		assertTrue(r.getStatus().isInitial());
	}

	private void setUp(ArrayList<ReceiveCondition> conditions)
			throws ParserConfigurationException, SpecificationException,
			MalformedURLException, WSDLException, DataSourceException {
		BPELUnitUtil.initializeParsing();
		
		ProcessUnderTest put = new ProcessUnderTest("name", ".", null, null, "http://localhost:7777/ws");
		TestSuite suite = new TestSuite("name.bpts", new URL("http://localhost:7777/ws"), put );
		TestCase t = new TestCase(suite , "MyTest");
		TestCaseRunner runner = new TestCaseRunner(null, t);
		
		PartnerTrack partnerTrack = new PartnerTrack(t, new AbstractPartner("Test", "http://localhost:7777/ws"));
		partnerTrack.initialize(runner);
		t.addPartnerTrack(partnerTrack);
		r = new ReceiveAsync(partnerTrack);
		ReceiveDataSpecification spec = new ReceiveDataSpecification(r,
				new NamespaceContextImpl());

		WSDLFactory wsdlFactory = WSDLFactory.newInstance();
		WSDLReader wsdlReader = wsdlFactory.newWSDLReader();
		InputStream wsdlResource = getClass().getResourceAsStream("simple.wsdl");
		assertNotNull(wsdlResource);
		Definition wsdl = wsdlReader.readWSDL(null, new InputSource(wsdlResource));
		QName serviceName = new QName("http://www.bpelunit.net/simple/", "SimpleService");
		String port = "simpleSOAP";
		String operationName = "simple";
		SOAPOperationDirectionIdentifier direction = SOAPOperationDirectionIdentifier.INPUT;
		spec.initialize(new SOAPOperationCallIdentifier(wsdl,
				serviceName, port, operationName, direction), "document/literal",
				new DocumentLiteralEncoder(), conditions, null, null);
		r.initialize(spec);
		
		context = new ActivityContext(runner, partnerTrack);
		context.createVelocityContext();
	}

	private String wrapInSoapMessage(String message) {
		return String
				.format("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>%s</soap:Body></soap:Envelope>",
						message);
	}
}
