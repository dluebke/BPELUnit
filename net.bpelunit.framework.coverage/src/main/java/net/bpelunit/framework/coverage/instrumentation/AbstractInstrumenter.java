package net.bpelunit.framework.coverage.instrumentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import net.bpelunit.framework.coverage.marker.Marker;
import net.bpelunit.framework.coverage.marker.MarkerFactory;
import net.bpelunit.framework.coverage.marker.MarkerInstanceList;
import net.bpelunit.framework.coverage.result.IMetricCoverage;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IDocumentation;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IVisitor;
import net.bpelunit.util.XMLUtil;

import org.w3c.dom.Element;

public abstract class AbstractInstrumenter implements IVisitor  {

	static final QName QNAME_MARKER = new QName("http://www.bpelunit.net/instrumentation", "marker");
	private MarkerFactory markerFactory;
	private List<Marker> issuedMarkers = new ArrayList<Marker>();

	public AbstractInstrumenter(MarkerFactory markerFactoryToUse) {
		markerFactory = markerFactoryToUse;
	}
	
	public void addCoverageMarkers(IProcess p) {
		p.visit(this);
	}
	
	protected Marker addCoverageMarker(IActivity a) {
		return this.addCoverageMarker(a, a);
	}
	
	protected Marker addCoverageMarker(IActivity a, IBpelObject measuredObject) {
		Marker newMarker = markerFactory.createMarker(a, measuredObject);
		issuedMarkers.add(newMarker);
		
		addCoverageMarker(a, newMarker);
		
		return newMarker;
	}

	/**
	 * Extracted so that MarkerToActivityConverterTest can easily add
	 * markers to activities during test.
	 * 
	 * @param a Activity to which marker m should be added
	 * @param m Marker to be added
	 */
	static void addCoverageMarker(IActivity a, Marker m) {
		if(a.getDocumentation().size() == 0) {
			a.addDocumentation();
		}
		IDocumentation firstDocumentation = a.getDocumentation().get(0);
		Element e = firstDocumentation.addDocumentationElement(QNAME_MARKER);
		XMLUtil.appendTextNode(e, m.getName());
	}
	
	/**
	 * Calculates the coverage results for a given set of markers. The markers are selected and passed by the framework
	 * and can be arbitrary subsets, e.g. based on test cases.
	 * 
	 * Markers can appear 0 to multiple times, depending on the execution count.
	 * 
	 * @param markers
	 * @return
	 */
	public abstract IMetricCoverage getCoverageResult(MarkerInstanceList markers);

	/**
	 * @return a list of all markers that have been created/issued by this instrumenter.
	 */
	public List<Marker> getIssuedMarkers() {
		return Collections.unmodifiableList(issuedMarkers);
	}

}
