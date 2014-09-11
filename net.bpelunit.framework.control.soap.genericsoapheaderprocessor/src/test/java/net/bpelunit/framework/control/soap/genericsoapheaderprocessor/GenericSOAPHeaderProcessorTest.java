package net.bpelunit.framework.control.soap.genericsoapheaderprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

import net.bpelunit.framework.control.ext.SendPackage;
import net.bpelunit.framework.exception.HeaderProcessingException;
import net.bpelunit.util.XMLUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class GenericSOAPHeaderProcessorTest {

	private GenericSOAPHeaderProcessor genericSOAPHeaderProcessor;
	
	@Before
	public void setUp() {
		genericSOAPHeaderProcessor = new GenericSOAPHeaderProcessor();
	}
	
	@After
	public void tearDown() {
		genericSOAPHeaderProcessor = null;
	}
	
	@Test
	public void injects_SOAP_Header() throws Exception {
		genericSOAPHeaderProcessor.setProperty(GenericSOAPHeaderProcessor.CONFIG_KEY_SOAPHEADER, "<ns:MyHeader xmlns:ns=\"someURI\">MyHeaderValue</ns:MyHeader>");
		
		MessageFactory mFactory = MessageFactory.newInstance();
		SOAPMessage message = mFactory.createMessage();
		SendPackage sendPackage = new SendPackage("", message);
		
		genericSOAPHeaderProcessor.processSend(null, sendPackage);
		
		assertNotNull(message.getSOAPHeader());
		assertTrue(message.getSOAPHeader().getChildElements(new QName("someURI", "MyHeader")).hasNext());
		Node n = (Node)(message.getSOAPHeader().getChildElements(new QName("someURI", "MyHeader")).next());
		assertEquals("MyHeaderValue", XMLUtil.getContentsOfTextOnlyNode(n));
	}
	
	@Test
	public void proceeds_With_Valid_Assertion() throws Exception {
		genericSOAPHeaderProcessor.setProperty(GenericSOAPHeaderProcessor.CONFIG_KEY_ASSERTION, "//*[local-name(.)='MyHeader'] = 'MyHeaderValue'");
		
		MessageFactory mFactory = MessageFactory.newInstance();
		SOAPMessage message = mFactory.createMessage();
		
		Element mySOAPHeader = message.getSOAPHeader().getOwnerDocument().createElementNS("someURI", "MyHeader");
		mySOAPHeader.setTextContent("MyHeaderValue");
		message.getSOAPHeader().appendChild(mySOAPHeader);
		
		genericSOAPHeaderProcessor.processReceive(null, message);
	}
	
	@Test(expected=HeaderProcessingException.class)
	public void fails_With_Invalid_Assertion() throws Exception {
		genericSOAPHeaderProcessor.setProperty(GenericSOAPHeaderProcessor.CONFIG_KEY_ASSERTION, "//*[local-name(.)='MyHeader'] = 'MyHeaderValue'");
		
		MessageFactory mFactory = MessageFactory.newInstance();
		SOAPMessage message = mFactory.createMessage();
		
		Element mySOAPHeader = message.getSOAPHeader().getOwnerDocument().createElementNS("someURI", "MyHeader");
		mySOAPHeader.setTextContent("MyHeaderValueWrong");
		message.getSOAPHeader().appendChild(mySOAPHeader);
		
		genericSOAPHeaderProcessor.processReceive(null, message);
	}
}
