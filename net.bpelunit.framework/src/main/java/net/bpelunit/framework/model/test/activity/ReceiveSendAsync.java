/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.data.DataCopyOperation;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.wire.IncomingMessage;

/**
 * A receive/send asynchronous activity is a combination of an asynchronous receive and an
 * asynchronous send chained together.
 * 
 * The combined activities have the optional ability of header processing and copying values from
 * input to output.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ReceiveSendAsync extends TwoWayAsyncActivity implements IEvaluatable {


	// ************************** Initialization ************************

	public ReceiveSendAsync(PartnerTrack partnerTrack) {
		super(partnerTrack);
		setStatus(ArtefactStatus.createInitialStatus());
	}


	// ***************************** Activity **************************

	@Override
	public void run(ActivityContext context) {

		IncomingMessage incoming;
		try {
			incoming= context.receiveMessage(this.getPartnerTrack());
		} catch (TimeoutException e) {
			setStatus(ArtefactStatus.createErrorStatus("Timeout while waiting for incoming asynchronous message", e));
			return;
		} catch (InterruptedException e) {
			setStatus(ArtefactStatus.createAbortedStatus("Aborted while waiting for incoming asynchronous messsage", e));
			return;
		}

		String message = incoming.getBody();
		
		context.setHeaderProcessor(getHeaderProcessor());
		context.setMapping(getMapping());
		
		this.handle(context, message);
	}

	@Override
	public String getActivityCode() {
		return "ReceiveSendAsync";
	}

	// ************************** ITestArtefact ************************

	@Override
	public String getName() {
		return "Receive/Send Asynchronous";
	}

	@Override
	public List<ITestArtefact> getChildren() {
		List<ITestArtefact> children= new ArrayList<ITestArtefact>();
		// Add copy information
		if (getMapping() != null) {
			for (DataCopyOperation copy : getMapping()) {
				children.add(copy);
			}
		}
		// Add activities
		children.add(getReceiveAsync());
		children.add(getSendAsync());
		return children;
	}

	@Override
	public boolean couldFinishSuccessfully(ActivityContext context,
			String incoming) {
		return getReceiveAsync().couldFinishSuccessfully(context, incoming);
	}


	@Override
	public void handle(ActivityContext context, String message) {
		getReceiveAsync().handle(context, message);
		reportProgress(getReceiveAsync());

		/*
		 * Note that there is no way of indicating an error during processing of an asynchronous
		 * receive. Sending back fault data is not acceptable.
		 * 
		 * See ReceiveAsync class for more information.
		 * 
		 */

		if (getReceiveAsync().hasProblems()) {
			// The receive failed (either never received anything, or wrong
			// message)
			// Abort
			setStatus(getReceiveAsync().getStatus());
			return;
		}

		getSendAsync().run(context);
		reportProgress(getSendAsync());

		if (getSendAsync().hasProblems()) {
			setStatus(getSendAsync().getStatus());
			return;
		}

		setStatus(ArtefactStatus.createPassedStatus());
	}
}
