package net.bpelunit.framework.model.test.activity;

import net.bpelunit.framework.model.test.report.ITestArtefact;


public interface IEvaluatable extends ITestArtefact {

	boolean couldFinishSuccessfully(ActivityContext context,
			String incomingMessage);
	
	void handle(ActivityContext context, String incomingMessage);
}
