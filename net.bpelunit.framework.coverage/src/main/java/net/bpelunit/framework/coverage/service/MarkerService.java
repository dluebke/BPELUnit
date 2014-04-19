package net.bpelunit.framework.coverage.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.bpelunit.framework.coverage.CoverageConstants;
import net.bpelunit.framework.coverage.marker.Marker;
import net.bpelunit.framework.coverage.marker.MarkerFactory;
import net.bpelunit.framework.coverage.marker.MarkerInstance;
import net.bpelunit.framework.coverage.marker.MarkerInstanceList;
import net.bpelunit.util.XMLUtil;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MarkerService extends AbstractHandler {

	private Logger logger = Logger.getLogger(getClass());
	private MarkerFactory markerfactory;
	private String currentTestCase;
	private MarkerInstanceList collectedMarkerInstances = new MarkerInstanceList();

	public MarkerService(MarkerFactory mf) {
		this.markerfactory = mf;
	}

	public void handle(String pathInContext, Request pathParams,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (pathInContext
				.endsWith(CoverageConstants.COVERAGE_SERVICE_BPELUNIT_NAME)
				&& request.getMethod().equals("POST")) {
			try {
				Document soapMessage = XMLUtil.parseXML(request.getInputStream());
				Element envelope = soapMessage.getDocumentElement();
				Element body = XMLUtil.getChildElementsByName(envelope, "Body").get(0);
				Element msgElement = XMLUtil.getChildElementsByName(
						body, CoverageConstants.COVERAGE_MSG_ELEMENT).get(0);
				for (Element markerElement : XMLUtil.getChildElementsByName(
						msgElement,
						CoverageConstants.COVERAGE_MSG_MARKER_ELEMENT)) {
					String markerId = XMLUtil
							.getContentsOfTextOnlyNode(markerElement);
					Marker m = markerfactory.getMarkerById(markerId);
					MarkerInstance mi = new MarkerInstance(m, currentTestCase);
					synchronized(collectedMarkerInstances) {
						collectedMarkerInstances.add(mi);
					}
				}
				response.setStatus(202);
				response.setContentLength(0);
				// Necessary because Jetty will issue a HTTP/404 if nothing is written to the output stream
				ServletOutputStream out = response.getOutputStream();
				out.write(new byte[0]);
				out.close();
			} catch (Exception e) {
				logger.error("Exception while processing marker message: ", e);
				response.setStatus(500);
				response.setContentLength(0);
				throw new ServletException(e);
			}
		}
	}

	/**
	 * Notifies this service of the currently executing test case
	 * 
	 * @param currentTestCase test case that will be executed
	 */
	public void setCurrentTestCase(String currentTestCase) {
		this.currentTestCase = currentTestCase;
	}

	/**
	 * @return list of received marker instances
	 */
	public MarkerInstanceList getCollectedMarkerInstances() {
		return collectedMarkerInstances;
	}
}
