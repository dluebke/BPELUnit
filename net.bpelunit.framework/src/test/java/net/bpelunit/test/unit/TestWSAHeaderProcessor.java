/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathExpressionException;

import net.bpelunit.framework.control.ext.SendPackage;
import net.bpelunit.framework.control.soap.NamespaceContextImpl;
import net.bpelunit.framework.control.soap.WSAHeaderProcessor;
import net.bpelunit.framework.exception.HeaderProcessingException;
import net.bpelunit.framework.model.test.activity.ActivityContext;
import net.bpelunit.test.util.TestUtil;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * Tests the WSA Header Processor
 * 
 * @version $Id: TestWSAHeaderProcessor.java,v 1.5 2006/07/11 14:27:43 phil Exp$
 * @author Philip Mayer
 * 
 */
public class TestWSAHeaderProcessor extends SimpleTest {

	private static final String NAMESPACE_WSA_2003 = "http://schemas.xmlsoap.org/ws/2003/03/addressing";

	private static final String NAMESPACE_WSA_2004 = "http://schemas.xmlsoap.org/ws/2004/08/addressing";

	private static final String PATH_TO_FILES = "/wsahp/";

	ActivityContext activityContext;

	private MessageFactory factory;

	private WSAHeaderProcessor wsaProcessor;

	@Before
	public void before() throws SOAPException {
		this.activityContext = new ActivityContext("http://simulated.url");
		this.factory = MessageFactory.newInstance();
		this.wsaProcessor = new WSAHeaderProcessor();
	}
	
	@Test
	public void testWSA2003HeadersReceiveSend() throws Exception {
		testWSAHeadersReceiveSend(NAMESPACE_WSA_2003);
	}

	@Test
	public void testWSA2004HeadersReceiveSend() throws Exception {
		wsaProcessor.setWSAVersion("2004");
		testWSAHeadersReceiveSend(NAMESPACE_WSA_2004);
	}

	private void testWSAHeadersReceiveSend(String namespaceURI)
			throws IOException, SOAPException, HeaderProcessingException,
			XPathExpressionException {
		
		String messageId = "kjasdhfjsdbksjdgdsrhfdgjkdfjk";
		
		String fileName = "incomingSOAP.xml";
		if(namespaceURI.equals(NAMESPACE_WSA_2004)) {
			fileName = "incomingSOAP_WSA2004.xml";
		}
		SOAPMessage rcvMessage = createMessageFromFile(fileName);

		wsaProcessor.processReceive(activityContext, rcvMessage);

		assertEquals("true", activityContext.getUserData("WSA-Received"));
		assertEquals(messageId,
				activityContext.getUserData("WSA-Received-ID"));
		assertEquals("http://return.to.me/",
				activityContext.getUserData("WSA-Received-Address"));

		SOAPMessage sendMessage = factory.createMessage(null, this.getClass()
				.getResourceAsStream(PATH_TO_FILES + "outgoingSOAP.xml"));

		SendPackage p = new SendPackage("http://target.url", sendMessage);
		wsaProcessor.processSend(activityContext, p);

		assertEquals("http://return.to.me/", p.getTargetURL());
		Element header = p.getSoapMessage().getSOAPHeader();

		NamespaceContextImpl ns = createNamespaceContext(namespaceURI);

		Node msgId = TestUtil.getNode(header, ns, "wsa:RelatesTo");
		assertEquals(messageId, msgId.getTextContent());
	}

	@Test
	public void testWSA2003HeadersSendReceive() throws Exception {
		testWSAHeadersSendReceive(NAMESPACE_WSA_2003);
	}

	@Test
	public void testWSA2004HeadersSendReceive() throws Exception {
		wsaProcessor.setWSAVersion("2004");
		
		testWSAHeadersSendReceive(NAMESPACE_WSA_2004);
	}

	private void testWSAHeadersSendReceive(String namespaceURI) throws IOException, SOAPException,
			HeaderProcessingException, XPathExpressionException {
		SOAPMessage sendMessage = createMessageFromFile("outgoingSOAP.xml");

		SendPackage p = new SendPackage("http://target.url", sendMessage);
		wsaProcessor.processSend(activityContext, p);

		/*
		 * Processor needs to insert MessageID and Reply-To-Address into the SOAP
		 * header.
		 */

		Element header = p.getSoapMessage().getSOAPHeader();
		NamespaceContextImpl ns = createNamespaceContext(namespaceURI);
		
		Node msgId = TestUtil.getNode(header, ns, "wsa:MessageID");
		assertTrue(msgId.getTextContent().startsWith("BPELUnit-"));
		Node replyTo = TestUtil.getNode(header, ns, "wsa:ReplyTo/wsa:Address");
		assertEquals("http://simulated.url", replyTo.getTextContent());

		assertEquals("true", activityContext.getUserData("WSA-Sent"));
		assertTrue(activityContext.getUserData("WSA-Sent-ID").startsWith(
				"BPELUnit-"));

		// Receive something back (doesn't really matter actually)

		SOAPMessage rcvMessage = createMessageFromFile("incomingSOAP2.xml");
		wsaProcessor.processReceive(activityContext, rcvMessage);
	}

	@Test
	public void testFixedMessageId() throws Exception {
		String expectedMessageId = "MyNewMessageId";
		
		wsaProcessor.setMessageId(expectedMessageId);

		SOAPMessage sendMessage = createMessageFromFile("outgoingSOAP.xml");
		SendPackage p = new SendPackage("http://target.url", sendMessage);
		
		wsaProcessor.processSend(activityContext, p);
		
		Element header = p.getSoapMessage().getSOAPHeader();
		NamespaceContextImpl ns = createNamespaceContext(NAMESPACE_WSA_2003);
		Node msgId = TestUtil.getNode(header, ns, "wsa:MessageID");
		
		assertEquals(expectedMessageId, msgId.getTextContent());
	}

	private NamespaceContextImpl createNamespaceContext(String wsaNamespace) {
		NamespaceContextImpl ns = new NamespaceContextImpl();
		ns.setNamespace("wsa", wsaNamespace);
		return ns;
	}

	private SOAPMessage createMessageFromFile(String fileName)
			throws SOAPException, IOException {
		SOAPMessage sendMessage = factory.createMessage(null, this.getClass()
				.getResourceAsStream(PATH_TO_FILES + fileName));
		return sendMessage;
	}
	
	@Test
	public void testImplicitReplyTo() throws Exception {
		SOAPMessage sendMessage = createMessageFromFile("outgoingSOAP.xml");
		SendPackage p = new SendPackage("http://target.url", sendMessage);
		
		wsaProcessor.processSend(activityContext, p);
		
		NamespaceContextImpl ns = createNamespaceContext(NAMESPACE_WSA_2003);
		Node replyToAddress = TestUtil.getNode(sendMessage.getSOAPHeader(), ns, "//wsa:ReplyTo/wsa:Address");
		
		assertEquals("http://simulated.url", replyToAddress.getTextContent());
	}
	
	@Test
	public void testSetReplyToWithoutFaultTo() throws Exception {
		wsaProcessor.setReplyToURI("http://localhost");
		
		SOAPMessage sendMessage = createMessageFromFile("outgoingSOAP.xml");
		SendPackage p = new SendPackage("http://target.url", sendMessage);
		
		wsaProcessor.processSend(activityContext, p);
		
		NamespaceContextImpl ns = createNamespaceContext(NAMESPACE_WSA_2003);
		Node replyToAddress = TestUtil.getNode(sendMessage.getSOAPHeader(), ns, "//wsa:ReplyTo/wsa:Address");
		
		assertEquals("http://localhost", replyToAddress.getTextContent());
	}
	
	@Test
	public void testSetReplyToAndFaultTo() throws Exception {
		wsaProcessor.setReplyToURI("http://localhost/reply");
		wsaProcessor.setFaultToURI("http://localhost/fault");
		
		SOAPMessage sendMessage = createMessageFromFile("outgoingSOAP.xml");
		SendPackage p = new SendPackage("http://target.url", sendMessage);
		
		wsaProcessor.processSend(activityContext, p);
		
		NamespaceContextImpl ns = createNamespaceContext(NAMESPACE_WSA_2003);
		Node replyToAddress = TestUtil.getNode(sendMessage.getSOAPHeader(), ns, "//wsa:ReplyTo/wsa:Address");
		Node faultToAddress = TestUtil.getNode(sendMessage.getSOAPHeader(), ns, "//wsa:FaultTo/wsa:Address");
		
		assertEquals("http://localhost/reply", replyToAddress.getTextContent());
		assertEquals("http://localhost/fault", faultToAddress.getTextContent());
	}
	
	@Test
	public void testSuccessfulExpectedRelatesTo() throws Exception {
		String relatesToFromFile = "kjasdhfjsdbksjdgdsrhfdgjkdfjk2";
		wsaProcessor.setExpectedRelatesTo(relatesToFromFile);
		
		SOAPMessage message = createMessageFromFile("incomingSOAP.xml");
		
		wsaProcessor.processReceive(activityContext, message);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateRelatesTo() throws Exception {
		wsaProcessor.setValidateRelatesTo("some-wrong-value");
	}
	
	@Test
	public void testSuccessfulRelatesToFromOwnSend() throws Exception {
		wsaProcessor.setValidateRelatesTo("true");
		
		SOAPMessage message = createMessageFromFile("outgoingSOAP.xml");
		SendPackage p = new SendPackage("http://simulated.url", message);
		
		wsaProcessor.processSend(activityContext, p);
		NamespaceContextImpl ns = createNamespaceContext(NAMESPACE_WSA_2003);
		String msgId = TestUtil.getNode(message.getSOAPHeader(), ns, "wsa:MessageID").getTextContent();
		
		message = createMessageFromFile("incomingSOAP.xml");
		TestUtil.getNode(message.getSOAPHeader(), ns, "wsa:RelatesTo").setTextContent(msgId);
		
		wsaProcessor.processReceive(activityContext, message);
	}
	
	@Test(expected=HeaderProcessingException.class)
	public void testUnsuccessfulRelatesToFromOwnSend() throws Exception {
		wsaProcessor.setValidateRelatesTo("true");
		
		SOAPMessage message = createMessageFromFile("outgoingSOAP.xml");
		SendPackage p = new SendPackage("http://simulated.url", message);
		
		wsaProcessor.processSend(activityContext, p);
		NamespaceContextImpl ns = createNamespaceContext(NAMESPACE_WSA_2003);
		String msgId = TestUtil.getNode(message.getSOAPHeader(), ns, "wsa:MessageID").getTextContent();
		msgId += "wrongId";
		
		message = createMessageFromFile("incomingSOAP.xml");
		TestUtil.getNode(message.getSOAPHeader(), ns, "wsa:RelatesTo").setTextContent(msgId);
		
		wsaProcessor.processReceive(activityContext, message);
	}

	@Test(expected=HeaderProcessingException.class)
	public void testUnsuccessfulExpectedRelatesTo() throws Exception {
		String relatesToFromFile = "kjasdhfjsdbksjdgdsrhfdgjkdfjk3";
		wsaProcessor.setExpectedRelatesTo(relatesToFromFile);
		
		SOAPMessage message = createMessageFromFile("incomingSOAP.xml");
		
		wsaProcessor.processReceive(activityContext, message);
	}
}
