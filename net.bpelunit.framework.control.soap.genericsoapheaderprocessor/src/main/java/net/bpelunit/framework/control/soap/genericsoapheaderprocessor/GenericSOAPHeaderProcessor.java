package net.bpelunit.framework.control.soap.genericsoapheaderprocessor;

import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.bpelunit.framework.control.ext.IHeaderProcessor;
import net.bpelunit.framework.control.ext.SendPackage;
import net.bpelunit.framework.exception.HeaderProcessingException;
import net.bpelunit.framework.model.test.activity.ActivityContext;
import net.bpelunit.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class GenericSOAPHeaderProcessor implements IHeaderProcessor {

	public static final String CONFIG_KEY_ASSERTION = "Assertion";
	public static final String CONFIG_KEY_SOAPHEADER = "SOAPHeader";
	
	private String assertionXPath;
	private Document soapHeaderToInject;
	
	
	@Override
	public void setProperty(String name, String value) {
		if(CONFIG_KEY_ASSERTION.equals(name)) {
			assertionXPath = value;
		} else if (CONFIG_KEY_SOAPHEADER.equals(name)) {
			try {
				soapHeaderToInject = XMLUtil.parseXML(value);
			} catch (Exception e) {
				throw new RuntimeException("Cannot parse XML specified in Generic SOAP Header Processor: " + e.getMessage(), e);
			}
		} else {
			throw new RuntimeException("Unknown config key for " + this.getClass().getCanonicalName() + ": " + name);
		}
	}

	@Override
	public void processSend(ActivityContext context, SendPackage sendPackage)
			throws HeaderProcessingException {
		if(soapHeaderToInject != null) {
			try {
				SOAPHeader soapHeader = sendPackage.getSoapMessage().getSOAPHeader();
				Node newChild = soapHeader.getOwnerDocument().importNode(soapHeaderToInject.getDocumentElement(), true);
				soapHeader.insertBefore(newChild, null);
			} catch (Exception e) {
				throw new HeaderProcessingException("Cannot add SOAP Header: " + e.getMessage(), e);
			}
		}
	}

	@Override
	public void processReceive(ActivityContext context,
			SOAPMessage receivedPackage) throws HeaderProcessingException {
		if(assertionXPath != null) {
			XPath xpath = XPathFactory.newInstance().newXPath();
			try {
				Object result = xpath.evaluate(assertionXPath, receivedPackage.getSOAPHeader(), XPathConstants.BOOLEAN);
				if(!result.equals(Boolean.TRUE)) {
					throw new HeaderProcessingException("Assertion did not hold: " + assertionXPath); 
				}
			} catch (Exception e) {
				throw new HeaderProcessingException("Cannot evaluate XPath: " + e.getMessage(), e); 
			}
		}
	}
}
