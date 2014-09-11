Generic SOAP Header Processor
=============================

BPELUnit can inject SOAP Header in sent SOAP messages and make assertions on headers in incoming SOAP messages by utilizing a Generic SOAP Header Processor. Because this functionality was not planned for there are some limitations to this approach, which however should offer everyting that is needed in practice. This document explains how to use these features.

Syntax
------

Header Processor Name: generic
Configuration Values:
- SOAPHeader: SOAP Header as XML Fragment to inject into the message
- Assertion: XPath expression without any namespaces that is valid if it returns true

Injecting a SOAP Header
-----------------------

You can put a Generic SOAP Header Processor on any send and two-way activity in order to inject an outgoing SOAP Header:

    <tes:receiveSendAsynchronous service="new:NewWSDLFile" port="NewWSDLFileSOAP" operation="NewOperation">
	<tes:send .../>
	<tes:receive .../>
	<tes:headerProcessor name="generic">
	    <tes:property name="SOAPHeader">&lt;ns:MyHeader xmlns:ns="myNamespace"&gt;MyHeaderValue&lt;/ns:MyHeader&gt;</tes:property>
	</tes:headerProcessor>
    </tes:receiveSendAsynchronous>

Because Header Processors in general can only deal with strings, you need to escape all < and > in the XML. Also, namespaces are _not_ used from the test suite so you need to declare them in your XML fragment.

Assertions on SOAP Headers
--------------------------

Likewise, assertions can be put for a SOAP header: Just place a Generic SOAP Header Processor on any receive or two-way activity in order to make an assertions. However, you cannot use namespace prefixes yet, so you need to use local-name() and namespace-uri() where required.

    <tes:receiveSendAsynchronous service="new:NewWSDLFile" port="NewWSDLFileSOAP" operation="NewOperation">
	<tes:send .../>
	<tes:receive .../>
	<tes:headerProcessor name="generic">
	    <tes:property name="Assertion">//*[local-name(.)='MyHeader' and namespace-uri(.) = 'myNamespace'] = 'MyHeaderValue'</tes:property>
	</tes:headerProcessor>
    </tes:receiveSendAsynchronous>

Assertion and Injection in Two-Way-Activities
---------------------------------------------

You can also use assertions and injections in two-way-activities with the same Generic SOAP Header Processor. For this, you specify both an assertion and a SOAP header. The assertion will be applied to the receive part of the two-way activity and the SOAP header will be injected in the message sent by it.

    <tes:receiveSendAsynchronous service="new:NewWSDLFile" port="NewWSDLFileSOAP" operation="NewOperation">
	<tes:send .../>
	<tes:receive .../>
	<tes:headerProcessor name="generic">
	    <tes:property name="Assertion">//*[local-name(.)='MyHeader' and namespace-uri(.) = 'myNamespace'] = 'MyHeaderValue'</tes:property>
	    <tes:property name="SOAPHeader">&lt;ns:MyHeader xmlns:ns="myNamespace"&gt;MyHeaderValue&lt;/ns:MyHeader&gt;</tes:property>
	</tes:headerProcessor>
    </tes:receiveSendAsynchronous>

Limitations
-----------

- Because SOAP Header Processors in BPELUnit have no access to the namespaces in the BPTS, XPath expressions have no access to any namespace definitions and must use neutral constructs.
- Because SOAP Header Processors in BPELUnit have string-only configuration values, the XML fragment must be encoded with XML escape characters
- Assertion errors cannot be cleanly reported because the only way for a Header Processor to report an error is to throw an exception. So assertion failures are only reported as an exception
