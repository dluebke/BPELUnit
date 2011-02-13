/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.framework.control.soap;

public class WSA2003HeaderProcessor extends WSAHeaderProcessor {

	private static final String WSA2003_NAMESPACE = "http://schemas.xmlsoap.org/ws/2003/03/addressing";
	
	@Override
	protected String getWsaNamespace() {
		return WSA2003_NAMESPACE;
	}

}
