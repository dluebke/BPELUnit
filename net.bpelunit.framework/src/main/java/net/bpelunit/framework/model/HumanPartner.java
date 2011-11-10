package net.bpelunit.framework.model;

import java.net.MalformedURLException;
import java.net.URL;

import net.bpelunit.framework.exception.SpecificationException;

public class HumanPartner extends AbstractPartner {
	private URL endPoint;
	private String username;
	private String password;
	
	public URL getEndPoint() {
		return endPoint;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public HumanPartner(String name, String basePath, URL baseURL, String endPoint, String username,
			String password) throws SpecificationException {
		
		super(name, basePath, baseURL.toString());
		
		try {
			this.endPoint = new URL(endPoint);
			this.username = username;
			this.password = password;
		} catch (MalformedURLException e) {
			throw new SpecificationException("Invalid WS-HT Endpoint (URL) for human partner " + name, e); 
		}
	}
}