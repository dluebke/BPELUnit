package net.bpelunit.framework.model.test.activity;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.wire.IncomingMessage;

public class ParallelSplit extends Activity implements IEvaluatable {

	private ITestArtefact parent;

	public ParallelSplit(PartnerTrack partnerTrack) {
		this(partnerTrack, null);
	}
	
	public ParallelSplit(PartnerTrack partnerTrack, ITestArtefact parentIfNotPartnerTrack) {
		super(partnerTrack);
		this.parent = parentIfNotPartnerTrack;
	}

	private List<IEvaluatable> allActivities = new ArrayList<IEvaluatable>();
	private List<IEvaluatable> activitiesToExecute = new ArrayList<IEvaluatable>(); 
	
	public void initialize(List<?extends IEvaluatable> activities) {
		setStatus(ArtefactStatus.createInitialStatus());
		this.allActivities.clear();
		this.allActivities.addAll(activities);
		this.activitiesToExecute.clear();
		this.activitiesToExecute.addAll(activities);
	}
	
	@Override
	public void run(ActivityContext context) {
		setStatus(ArtefactStatus.createInProgressStatus());

		while(getStatus().isInProgress()) {
			IncomingMessage message;
			try {
				message = context.receiveMessage(getPartnerTrack());
			} catch (Exception e) {
				setStatus(ArtefactStatus.createErrorStatus("Error while waiting for message: " + e.getLocalizedMessage(), e));
				return;
			}
			String messageContents = message.getBody();
			
			handle(context, messageContents);
		}
	}

	public void handle(ActivityContext context, String messageContents) {
		if(getStatus().isInitial()) {
			setStatus(ArtefactStatus.createInProgressStatus());
		}
		
		IEvaluatable activityToExecute = getActivityToExecute(context, messageContents);
		if(activityToExecute != null) {
			activityToExecute.handle(context, messageContents);
			activitiesToExecute.remove(activityToExecute);
			reportProgress(activityToExecute);
			if(!activityToExecute.getStatus().isPassed()) {
				setStatus(activityToExecute.getStatus());
			}
		} else {
			setStatus(ArtefactStatus.createFailedStatus("No sub-activity found that could handle the incoming message: \n" + messageContents));
			return;
		}
		
		if(activitiesToExecute.size() == 0 && getStatus().isInProgress()) {
			setStatus(ArtefactStatus.createPassedStatus());
		}
	}

	private IEvaluatable getActivityToExecute(ActivityContext context,
			String message) {
		
		for(IEvaluatable e : activitiesToExecute) {
			if(e.couldFinishSuccessfully(context, message)) {
				return e;
			}
		}
		
		return null;
	}

	@Override
	public int getActivityCount() {
		return allActivities.size();
	}

	@Override
	public String getActivityCode() {
		return "ParallelSplit";
	}

	@Override
	public String getName() {
		return "Parallel Split";
	}

	@Override
	public ITestArtefact getParent() {
		if(this.parent != null) {
			return this.parent;
		} else {
			return this.getPartnerTrack();
		}
	}

	@Override
	public List<?extends ITestArtefact> getChildren() {
		List<?extends ITestArtefact> result = new ArrayList<IEvaluatable>(allActivities);
		return result;
	}

	@Override
	public boolean couldFinishSuccessfully(ActivityContext context,
			String incomingMessage) {
		return getActivityToExecute(context, incomingMessage) != null;
	}
}
