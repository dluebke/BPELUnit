package net.bpelunit.framework.coverage.marker;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.model.bpel.IBpelObject;

@SuppressWarnings("serial")
public class MarkerInstanceList extends ArrayList<MarkerInstance> {

	public MarkerInstanceList getMarkerInstancessForMeasuredObject(IBpelObject o) {
		MarkerInstanceList result = new MarkerInstanceList();
		
		for(MarkerInstance i : this) {
			if(o.equals(i.getMarker().getMeasuredObject())) {
				result.add(i);
			}
		}
		
		return result;
	}
	
	public MarkerInstanceList getMarkerInstancesForMarkers(List<?super Marker> markers) {
		MarkerInstanceList result = new MarkerInstanceList();
		
		for(MarkerInstance i : this) {
			if(markers.contains(i.getMarker())) {
				result.add(i);
			}
		}
		
		return result;
	}
	
	public MarkerInstanceList getMarkerInstancesForTestCase(String testCase) {
		MarkerInstanceList result = new MarkerInstanceList();
		
		for(MarkerInstance i : this) {
			if(testCase.equals(i.getTestCase())) {
				result.add(i);
			}
		}
		
		return result;
	}
	
}
