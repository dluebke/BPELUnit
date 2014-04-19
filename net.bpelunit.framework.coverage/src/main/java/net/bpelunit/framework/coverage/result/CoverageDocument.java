package net.bpelunit.framework.coverage.result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CoverageDocument implements ICoverageDocument {

	private File suite;
	private List<IBPELCoverage> coverageInformation = new ArrayList<IBPELCoverage>();

	public CoverageDocument(File suite, List<IBPELCoverage> coverageInformation) {
		super();
		this.suite = suite;
		this.coverageInformation.addAll(coverageInformation);
	}

	public File getExecutedSuite() {
		return this.suite;
	}

	public List<IBPELCoverage> getCoverageInformationForProcesses() {
		return this.coverageInformation;
	}

}
