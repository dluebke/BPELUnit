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
			+ Math.abs((long)Math.random() * 100000000);
	private String expectedMessageId = null;
	private String expectedRelatesTo = null; // filled in when message is sent
	private boolean validateRelatesTo = false;
	private String wsaNamespace = WSA2003_NAMESPACE;
	private String replyToPartnerTrack = null;
	private String replyToURI = null;
	private String faultToPartnerTrack = null;
	private String faultToURI = null;

	public WSAHeaderProcessor() {
	}

	public void processReceive(ActivityContext context, SOAPMessage message)
			throws HeaderProcessingException {

		// Two options: Either this is part one in a receive-send, or part two
		// in a send/receive

		if (context.getUserData(WSA_SENT).equals("true")) {
			// this is part two
			// nothing to do
		} else {
			// this is (presumably) part one
			// get message ID and reply-To-Adress
			SOAPHeader header;
			try {
				header = message.getSOAPHeader();
			} catch (SOAPException e) {
				throw new HeaderProcessingException(
						"Incoming SOAP message did not have a SOAP Header.", e);
			}

			// Retrieve message ID
			String messageID = null;
			for (Iterator<?> i = header.getChildElements(new QName(
					wsaNamespace, WSA_TAG_MESSAGE_ID)); i.hasNext();) {
				SOAPElement soapElement = (SOAPElement) i.next();
				messageID = soapElement.getTextContent();
			}

			// Retrieve reply to
			String replyTo = null;
			for (Iterator<?> i = header.getChildElements(new QName(
					wsaNamespace, WSA_TAG_REPLY_TO)); i.hasNext();) {
				SOAPElement soapElement = (SOAPElement) i.next();
				for (Iterator<?> j = soapElement.getChildElements(new QName(
						wsaNamespace, WSA_TAG_ADDRESS)); j.hasNext();) {
					SOAPElement soapElement2 = (SOAPElement) j.next();
					replyTo = soapElement2.getTextContent();
				}
			}

			if (messageID == null)
				messageID = "";
			// throw new
			// HeaderProcessingException("Message ID not found in incoming message.");

			if (replyTo == null)
				throw new HeaderProcessingException(
						"Reply-To address not found in incoming message.");

			context.setUserData(WSA_RECEIVED, "true");
			context.setUserData(WSA_RECEIVED_ID, messageID);
			context.setUserData(WSA_RECEIVED_ADDRESS, replyTo);
		}
	}

	public void processSend(ActivityContext context, SendPackage sendSpec)
			throws HeaderProcessingException {

		// Two options: Either this is part one in a send/receive, or part two
		// in a receive/send.
		if (context.getUserData(WSA_RECEIVED).equals("true")) {
			// we are sending back
			String targetURL = context.getUserData(WSA_RECEIVED_ADDRESS);
			String messageID = context.getUserData(WSA_RECEIVED_ID);

			if (targetURL == null)
				throw new HeaderProcessingException(
						"Target URL from presumed receive was empty.");

			if (messageID == null)
				throw new HeaderProcessingException(
						"Message ID from presumed receive was empty.");

			SOAPMessage msg = sendSpec.getSoapMessage();
			SOAPHeader header;
			try {
				header = msg.getSOAPHeader();
			} catch (SOAPException e) {
				throw new HeaderProcessingException(
						"No SOAP header in outgoing SOAP message.", e);
			}

			if (!messageID.equals("")) {
				SOAPElement msgId;
				try {
					msgId = header.addChildElement(new QName(wsaNamespace,
							WSA_TAG_RELATES_TO));
				} catch (SOAPException e) {
					throw new HeaderProcessingException(
							"Could not add relatesTo child element to outgoing SOAP message.",
							e);
				}
				msgId.setTextContent(messageID);
			}

			sendSpec.setTargetURL(targetURL);

		} else {
			// we are first, so this is a send-receive activity
			// add reply-to and message ID to the SOAP message
			SOAPMessage msg = sendSpec.getSoapMessage();
			SOAPHeader header;
			try {
				header = msg.getSOAPHeader();
			} catch (SOAPException e) {
				throw new HeaderProcessingException(
						"No SOAP header in outgoing SOAP message.", e);
			}

			try {
				// Message ID
				SOAPElement msgId = header.addChildElement(new QName(
						wsaNamespace, WSA_TAG_MESSAGE_ID));
				msgId.setTextContent(messageId);

				// Reply To
				SOAPElement replyTo = header.addChildElement(new QName(
						wsaNamespace, WSA_TAG_REPLY_TO));
				SOAPElement address = replyTo.addChildElement(new QName(
						wsaNamespace, WSA_TAG_ADDRESS));

				address.setTextContent(context.getPartnerURL());

				// store this
				context.setUserData(WSA_SENT, "true");
				context.setUserData(WSA_SENT_ID, messageId);
			} catch (SOAPException e) {
				throw new HeaderProcessingException(
						"Could not add MessageID, ReplyTo or Address child element to outgoing SOAP message.",
						e);
			}
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
		if ("MessageId".equals(name)) {
			setMessageId(value);
		}

		if ("WSAVersion".equals(name)) {
			setWSAVersion(value);
		}

		if ("ExpectedMessageId".equals(name)) {
			setExpectedMessageId(value);
		}

		if ("ExpectedRelatesTo".equals(name)) {
			setExpectedRelatesTo(value);
		}
	}

	public void setExpectedMessageId(String expectedMessageId) {
		this.expectedMessageId = expectedMessageId;
	}

	public void setExpectedRelatesTo(String expectedRelateToId) {
		this.expectedRelatesTo = expectedRelateToId;
	}

	public void setReplyToPartnerTrack(String replyToPartnerTrack) {
		this.replyToPartnerTrack = replyToPartnerTrack;

		if (replyToURI != null) {
			throw new IllegalArgumentException(
					"Only one of ReplyToPartnerTrack and ReplyToURL is allowed.");
		}
	}

	public void setReplyToURI(String replyToURL) {
		this.replyToURI = replyToURL;

		if (replyToPartnerTrack != null) {
			throw new IllegalArgumentException(
					"Only one of ReplyToPartnerTrack and ReplyToURL is allowed.");
		}
	}

	public void setFaultToPartnerTrack(String faultToPartnerTrack) {
		this.faultToPartnerTrack = faultToPartnerTrack;

		if (faultToURI != null) {
			throw new IllegalArgumentException(
					"Only one of FaultToPartnerTrack and FaultToURL is allowed.");
		}
	}

	public void setFaultToURI(String faultToURL) {
		this.faultToURI = faultToURL;

		if (faultToPartnerTrack != null) {
			throw new IllegalArgumentException(
					"Only one of FaultToPartnerTrack and FaultToURL is allowed.");
		}
	}
}
