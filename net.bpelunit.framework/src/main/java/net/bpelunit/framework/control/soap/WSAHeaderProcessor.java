/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.soap;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import net.bpelunit.framework.control.ext.IHeaderProcessor;
import net.bpelunit.framework.control.ext.SendPackage;
import net.bpelunit.framework.exception.HeaderProcessingException;
import net.bpelunit.framework.model.test.activity.ActivityContext;

/**
 * This class implements the WS-Adressing asynchronous call handling, as
 * specified in the WS-A 2003/03 standard.
 * 
 * Added 2004 version and many more options (dluebke)
 * 
 * @version $Id$
 * @author Philip Mayer, Daniel Luebke
 * 
 */
public class WSAHeaderProcessor implements IHeaderProcessor {

	private static final String WSA_TAG_RELATES_TO = "RelatesTo";
	private static final String WSA_TAG_ADDRESS = "Address";
	private static final String WSA_TAG_REPLY_TO = "ReplyTo";
	private static final String WSA_TAG_FAULT_TO = "FaultTo";
	private static final String WSA_TAG_MESSAGE_ID = "MessageID";
	private static final String WSA2003_NAMESPACE = "http://schemas.xmlsoap.org/ws/2003/03/addressing";
	private static final String WSA2004_NAMESPACE = "http://schemas.xmlsoap.org/ws/2004/08/addressing";
	private static final String WSA_RECEIVED = "WSA-Received";
	private static final String WSA_RECEIVED_ADDRESS = "WSA-Received-Address";
	private static final String WSA_RECEIVED_ID = "WSA-Received-ID";
	private static final String WSA_SENT = "WSA-Sent";
	private static final String WSA_SENT_ID = "WSA-Sent-ID";

	private String messageId = "BPELUnit-"
			+ Math.abs((long) (Math.random() * 100000000));
	private String expectedRelatesTo = null; // filled in when message is sent
	private boolean validateRelatesTo = false;
	private String wsaNamespace = WSA2003_NAMESPACE;
	private String replyToURI = null;
	private String faultToURI = null;

	public WSAHeaderProcessor() {
	}

	public void processReceive(ActivityContext context, SOAPMessage message)
			throws HeaderProcessingException {

		SOAPHeader header = getSOAPHeader(message);

		if (validateRelatesTo) {
			String relatesTo = extractRelatesTo(header);

			if (relatesTo == null || expectedRelatesTo == null
					|| !expectedRelatesTo.equals(relatesTo)) {
				throw new HeaderProcessingException("Wrong <RelatesTo> in WS-Addressing header: '" + expectedRelatesTo + "' but was '" + relatesTo + "'");
			}
		}

		// Two options: Either this is part one in a receive-send, or part two
		// in a send/receive

		if (context.getUserData(WSA_SENT).equals("true")) {
			// this is part two
			// nothing to do
		} else {
			// this is (presumably) part one
			processReceiveOfReceiveSend(context, message);
		}
	}

	private void processReceiveOfReceiveSend(ActivityContext context,
			SOAPMessage message) throws HeaderProcessingException {
		SOAPHeader header = getSOAPHeader(message);

		String messageID = extractMessageId(header);
		String replyTo = extractReplyTo(header);

		if (messageID == null) {
			messageID = "";
		}

		if (replyTo == null)
			throw new HeaderProcessingException(
					"Reply-To address not found in incoming message.");

		context.setUserData(WSA_RECEIVED, "true");
		context.setUserData(WSA_RECEIVED_ID, messageID);
		context.setUserData(WSA_RECEIVED_ADDRESS, replyTo);
	}

	private SOAPHeader getSOAPHeader(SOAPMessage message)
			throws HeaderProcessingException {
		try {
			return message.getSOAPHeader();
		} catch (SOAPException e) {
			throw new HeaderProcessingException(
					"Incoming SOAP message did not have a SOAP Header.", e);
		}
	}

	private String extractReplyTo(SOAPHeader header) {
		String replyTo = null;
		for (Iterator<?> i = header.getChildElements(new QName(wsaNamespace,
				WSA_TAG_REPLY_TO)); i.hasNext();) {
			SOAPElement soapElement = (SOAPElement) i.next();
			for (Iterator<?> j = soapElement.getChildElements(new QName(
					wsaNamespace, WSA_TAG_ADDRESS)); j.hasNext();) {
				SOAPElement soapElement2 = (SOAPElement) j.next();
				replyTo = soapElement2.getTextContent();
			}
		}
		return replyTo;
	}

	private String extractMessageId(SOAPHeader header) {
		for (Iterator<?> i = header.getChildElements(new QName(wsaNamespace,
				WSA_TAG_MESSAGE_ID)); i.hasNext();) {
			SOAPElement soapElement = (SOAPElement) i.next();
			return soapElement.getTextContent();
		}

		return null;
	}

	private String extractRelatesTo(SOAPHeader header) {
		for (Iterator<?> i = header.getChildElements(new QName(wsaNamespace,
				WSA_TAG_RELATES_TO)); i.hasNext();) {
			SOAPElement soapElement = (SOAPElement) i.next();
			return soapElement.getTextContent();
		}

		return null;
	}

	public void processSend(ActivityContext context, SendPackage sendSpec)
			throws HeaderProcessingException {

		// Two options: Either this is part one in a send/receive, or part two
		// in a receive/send.
		if (context.getUserData(WSA_RECEIVED).equals("true")) {
			processSendOfReceiveSend(context, sendSpec);
		} else {
			processSendOfSendReceive(context, sendSpec);
		}
	}

	private void processSendOfSendReceive(ActivityContext context,
			SendPackage sendSpec) throws HeaderProcessingException {

		// we are first, so this is a send-receive activity
		// add reply-to and message ID to the SOAP message
		try {
			SOAPHeader header = getSOAPHeader(sendSpec.getSoapMessage());

			addMessageId(header);
			this.expectedRelatesTo = this.messageId;

			if (replyToURI == null) {
				addReplyTo(header, context.getPartnerURL());
			} else {
				addReplyTo(header, replyToURI);
			}

			if (faultToURI != null) {
				addFaultTo(header, faultToURI);
			}

			// store this
			context.setUserData(WSA_SENT, "true");
			context.setUserData(WSA_SENT_ID, messageId);
		} catch (SOAPException e) {
			throw new HeaderProcessingException(
					"Could not add MessageID, ReplyTo or Address child element to outgoing SOAP message.",
					e);
		}
	}

	private void addMessageId(SOAPHeader header) throws SOAPException {
		SOAPElement msgId = header.addChildElement(new QName(wsaNamespace,
				WSA_TAG_MESSAGE_ID));
		msgId.setTextContent(messageId);
	}

	private void addReplyTo(SOAPHeader header, String replyToURI)
			throws SOAPException {
		SOAPElement replyTo = header.addChildElement(new QName(wsaNamespace,
				WSA_TAG_REPLY_TO));
		SOAPElement address = replyTo.addChildElement(new QName(wsaNamespace,
				WSA_TAG_ADDRESS));

		address.setTextContent(replyToURI);
	}

	private void addFaultTo(SOAPHeader header, String faultToURI)
			throws SOAPException {
		SOAPElement faultTo = header.addChildElement(new QName(wsaNamespace,
				WSA_TAG_FAULT_TO));
		SOAPElement address = faultTo.addChildElement(new QName(wsaNamespace,
				WSA_TAG_ADDRESS));

		address.setTextContent(faultToURI);
	}

	private void processSendOfReceiveSend(ActivityContext context,
			SendPackage sendSpec) throws HeaderProcessingException {
		String targetURL = context.getUserData(WSA_RECEIVED_ADDRESS);
		String messageID = context.getUserData(WSA_RECEIVED_ID);

		if (targetURL == null)
			throw new HeaderProcessingException(
					"Target URL from presumed receive was empty.");

		if (messageID == null)
			throw new HeaderProcessingException(
					"Message ID from presumed receive was empty.");

		SOAPHeader header = getSOAPHeader(sendSpec.getSoapMessage());

		if (!messageID.equals("")) {
			addRelatesTo(messageID, header);
		}

		sendSpec.setTargetURL(targetURL);
	}

	private void addRelatesTo(String messageID, SOAPHeader header)
			throws HeaderProcessingException {
		try {
			SOAPElement msgId = header.addChildElement(new QName(wsaNamespace,
					WSA_TAG_RELATES_TO));
			msgId.setTextContent(messageID);
		} catch (SOAPException e) {
			throw new HeaderProcessingException(
					"Could not add relatesTo child element to outgoing SOAP message.",
					e);
		}
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public void setWSAVersion(String version) {
		if ("2003".equals(version) || "".equals(version)) {
			wsaNamespace = WSA2003_NAMESPACE;
		} else if ("2004".equals(version)) {
			wsaNamespace = WSA2004_NAMESPACE;
		} else {
			throw new IllegalArgumentException(
					"WS-Addressing Version may only be 2003 or 2004");
		}
	}

	public void setProperty(String name, String value) {
		// TODO JDK 7: Switch to switch(name)
		// TODO Switch API to deployer-like annotations
		if ("MessageId".equals(name)) {
			setMessageId(value);
		}

		if ("WSAVersion".equals(name)) {
			setWSAVersion(value);
		}

		if ("ExpectedRelatesTo".equals(name)) {
			setExpectedRelatesTo(value);
		}

		if ("ReplyToURI".equals(name)) {
			setReplyToURI(value);
		}

		if ("FaultToURI".equals(name)) {
			setFaultToURI(value);
		}
		
		if("ValidateRelatesTo".equals(name)) {
			setValidateRelatesTo(value);
		}
	}

	public void setExpectedRelatesTo(String expectedRelateToId) {
		this.expectedRelatesTo = expectedRelateToId;
		this.validateRelatesTo = true;
	}

	public void setReplyToURI(String replyToURL) {
		this.replyToURI = replyToURL;
	}

	public void setFaultToURI(String faultToURL) {
		this.faultToURI = faultToURL;
	}
	
	public void setValidateRelatesTo(String validateRelatesTo) {
		if(validateRelatesTo.toLowerCase().equals("true")) {
			setValidateRelatesTo(true);
		} else if(validateRelatesTo.toLowerCase().equals("false")) {
			setValidateRelatesTo(false);
		} else {
			throw new IllegalArgumentException("Wrong value for ValidateRelatesTo: " + validateRelatesTo + ": must be 'true' or 'false'");
		}
	}
	
	public void setValidateRelatesTo(boolean validateRelatesTo) {
		this.validateRelatesTo = validateRelatesTo;
	}
}
