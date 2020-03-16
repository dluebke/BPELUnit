package net.bpelunit.framework.verify;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLActivity;
import net.bpelunit.framework.xml.suite.XMLParallelActivity;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.framework.xml.suite.XMLTrack;

public class ParallelActivityMustNotHaveDependsOnValidator implements ITestSuiteValidator {

	@Override
	public void validate(XMLTestSuiteDocument suite) throws SpecificationException {
		for(XMLTestCase tc : suite.getTestSuite().getTestCases().getTestCaseList()) {
			List<XMLTrack> partnerTracks = new ArrayList<XMLTrack>();
			partnerTracks.add(tc.getClientTrack());
			partnerTracks.addAll(tc.getPartnerTrackList());
			
			List<XMLParallelActivity> parallelActivitiesToProcess = new ArrayList<XMLParallelActivity>();
			for(XMLTrack t : partnerTracks) {
				parallelActivitiesToProcess.clear();
				parallelActivitiesToProcess.addAll(t.getParallelList());
				while(!parallelActivitiesToProcess.isEmpty()) {
					for(XMLParallelActivity p : parallelActivitiesToProcess) {
						validateNoDependsOn(p);
						for(XMLTrack s : p.getSequenceList()) {
							parallelActivitiesToProcess.addAll(s.getParallelList());
						}
					}
				}
			}
		}
	}

	private void validateNoDependsOn(XMLActivity p) throws SpecificationException {
		if(p.getDependsOn() != null && !p.getDependsOn().isEmpty()) {
			throw new SpecificationException("Activity " + p.toString() + " with id='"+ p.getId() + "' got a dependsOn dependency, which is not supported for this activity type!");
		}
	}

}
