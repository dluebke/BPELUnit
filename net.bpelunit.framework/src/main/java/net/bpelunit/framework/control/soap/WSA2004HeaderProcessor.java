/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.framework.control.soap;

public class WSA2004HeaderProcessor extends WSAHeaderProcessor {

	private static final String WSA2004_NAMESPACE = "http://schemas.xmlsoap.org/ws/2004/08/addressing";
	
	@Override
	protected String getWsaNamespace() {
		return WSA2004_NAMESPACE;
	}

}
