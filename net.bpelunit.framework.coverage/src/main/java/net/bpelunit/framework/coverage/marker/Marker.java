package net.bpelunit.framework.coverage.marker;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;

public class Marker {

	private String name;
	private IActivity instrumentedActivity;
	private IBpelObject measuredObject;

	Marker(String name, IActivity instrumentedActivity, IBpelObject measuredObject) {
		this.name = name;
		this.instrumentedActivity = instrumentedActivity;
		this.measuredObject = measuredObject;
	}

	public String getName() {
		return name;
	}
	
	public IActivity getInstrumentedActivity() {
		return instrumentedActivity;
	}

	public IBpelObject getMeasuredObject() {
		return measuredObject;
	}
}
