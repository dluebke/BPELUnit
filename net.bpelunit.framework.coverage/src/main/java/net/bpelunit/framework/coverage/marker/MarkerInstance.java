package net.bpelunit.framework.coverage.marker;

public class MarkerInstance {

	private Marker marker;
	private long timestamp;
	private String testCase;

	public MarkerInstance(Marker marker, String testCase, long timestamp) {
		this.marker = marker;
		this.testCase = testCase;
		this.timestamp = timestamp;
	}
	
	public MarkerInstance(Marker marker, String testCase) {
		this(marker, testCase, System.currentTimeMillis());
	}
	
	public Marker getMarker() {
		return marker;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getTestCase() {
		return testCase;
	}

}
