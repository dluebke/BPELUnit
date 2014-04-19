package net.bpelunit.framework.coverage.marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;


public class MarkerFactory {

	private static final String DEFAULT_MARKER_PREFIX = "COVERAGE_";
	private String markerPrefix;
	private int lastMarkerId = 0;
	private Map<String, Marker> issuedMarkers = new HashMap<String, Marker>();
	
	public MarkerFactory() {
		this(DEFAULT_MARKER_PREFIX);
		reset();
	}

	public MarkerFactory(String newMarkerPrefix) {
		this.markerPrefix = newMarkerPrefix;
	}
	
	public synchronized Marker createMarker(IActivity instrumentedActivity, IBpelObject measuredObject) {
		lastMarkerId++;
		String markerName = markerPrefix + lastMarkerId;
		Marker marker = new Marker(markerName, instrumentedActivity, measuredObject);
		
		issuedMarkers.put(markerName, marker);
		return marker;
	}

	public Marker getMarkerById(String markerName) {
		return issuedMarkers.get(markerName);
	}

	public List<Marker> getMarkersForInstrumentedActivity(IActivity activity) {
		List<Marker> result = new ArrayList<Marker>();
		for(Marker m : issuedMarkers.values()) {
			if(activity.equals(m.getInstrumentedActivity())) {
				result.add(m);
			}
		}
		
		return result;
	}
	
	/** 
	 * Method that should only be used in tests. Resets the internal state of this MarkerFactory as if it were
	 * a new instance.
	 */
	public void reset() {
		lastMarkerId = 0;
		issuedMarkers.clear();
	}
	
}
