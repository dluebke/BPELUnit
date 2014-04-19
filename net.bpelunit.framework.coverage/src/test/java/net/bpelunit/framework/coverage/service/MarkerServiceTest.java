package net.bpelunit.framework.coverage.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import net.bpelunit.framework.coverage.CoverageConstants;
import net.bpelunit.framework.coverage.marker.MarkerFactory;
import net.bpelunit.framework.coverage.marker.MarkerInstanceList;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class MarkerServiceTest {

	private static final int TEST_PORT = 9999;
	private static final String CONTEXT = "ws";
	
	private static Server httpServer;
	private static MarkerFactory markerFactory = new MarkerFactory("M");
	private static MarkerService markerService;

	@BeforeClass
	public static void setUpClass() throws Exception {
		httpServer = new Server(TEST_PORT);
		markerService = new MarkerService(markerFactory);
		httpServer.setHandler(markerService);
		httpServer.start();
	}
	
	@Before
	public void setUp() {
		markerFactory.reset();
	}
	
	@AfterClass
	public static void tearDownClass() throws Exception {
		httpServer.stop();
	}
	
	@Test
	public void testHandleWithCorrectConfiguration() throws Exception {
		// create three markers that are referenced in the mark.soap.xml (M1, M2, M3)
		for(int i = 0; i < 3; i++) {
			markerFactory.createMarker(null, null);
		}
		
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod();
		post.setURI(new URI("http://localhost:" + TEST_PORT + "/" + CONTEXT + "/" + CoverageConstants.COVERAGE_SERVICE_BPELUNIT_NAME, false));
		post.setRequestHeader("Content-Type", "text/xml");
		InputStream soapMsgStream = getClass().getResourceAsStream("mark.soap.xml");
		assertNotNull("mark.soap.xml exists in test resources", soapMsgStream);
		post.setRequestEntity(new InputStreamRequestEntity(soapMsgStream));
		int result = client.executeMethod(post);
		
		assertEquals(202, result);
		
		MarkerInstanceList collectedMarkerInstances = markerService.getCollectedMarkerInstances();
		assertEquals(3, collectedMarkerInstances.size());
		assertEquals("M1", collectedMarkerInstances.get(0).getMarker().getName());
		assertEquals("M2", collectedMarkerInstances.get(1).getMarker().getName());
		assertEquals("M3", collectedMarkerInstances.get(2).getMarker().getName());
	}
	
}
