package net.bpelunit.framework.model.test.activity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;

import org.junit.Test;

public class ParallelSplitTest {

	static class DummyPartnerTrack extends PartnerTrack {

		private boolean reportedProgress = false;

		public DummyPartnerTrack() {
			super(null, null);
		}

		@Override
		public void reportProgress(ITestArtefact artefac) {
			reportedProgress  = true;
		}
		
		public boolean hasReportedProgress() {
			boolean result = reportedProgress;
			reportedProgress = false;
			
			return result;
		}
	}
	
	static class Evaluatable implements IEvaluatable {
		private int evaluatesAtTry;
		
		public Evaluatable(int evaluatesAtTry, boolean failsAtExecution) {
			super();
			this.evaluatesAtTry = evaluatesAtTry;
			this.failsAtExecution = failsAtExecution;
		}

		private boolean failsAtExecution;
		private ArtefactStatus status = ArtefactStatus.createInitialStatus();
		
		@Override
		public boolean couldFinishSuccessfully(ActivityContext context,
				String incomingMessage) {
			if(evaluatesAtTry > 0) {
				evaluatesAtTry--;
			}
			
			return evaluatesAtTry == 0;
		}
		
		@Override
		public List<? extends ITestArtefact> getChildren() {
			return null;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public ArtefactStatus getStatus() {
			return status;
		}

		@Override
		public ITestArtefact getParent() {
			return null;
		}

		@Override
		public List<StateData> getStateData() {
			return null;
		}

		@Override
		public void reportProgress(ITestArtefact artefact) {
		}

		@Override
		public void handle(ActivityContext context, String incomingMessage) {
			if(evaluatesAtTry != 0) {
				throw new RuntimeException("Must not be called here");
			}
			
			if(failsAtExecution) {
				status = ArtefactStatus.createFailedStatus("FAILED");
			} else {
				status = ArtefactStatus.createPassedStatus();
			}
		}
	}
	
	@Test
	public void testSingleActivitySuccess() {
		Evaluatable e1 = new Evaluatable(1, false);
		
		DummyPartnerTrack partnerTrack = new DummyPartnerTrack();
		ParallelSplit p = new ParallelSplit(partnerTrack);
		p.initialize(Arrays.asList(new IEvaluatable[]{ e1 }));
		
		assertTrue(p.couldFinishSuccessfully(null, null));
		
		p.handle(null, null);
		assertTrue(partnerTrack.hasReportedProgress());
		assertTrue(p.getStatus().isPassed());
		
		assertFalse(p.couldFinishSuccessfully(null, null));
	}
	
	@Test
	public void testSingleActivityFailure() {
		Evaluatable e1 = new Evaluatable(1, true);
		
		DummyPartnerTrack partnerTrack = new DummyPartnerTrack();
		ParallelSplit p = new ParallelSplit(partnerTrack);
		p.initialize(Arrays.asList(new IEvaluatable[]{ e1 }));
		
		assertTrue(p.couldFinishSuccessfully(null, null));
		
		p.handle(null, null);
		assertTrue(partnerTrack.hasReportedProgress());
		assertTrue(p.getStatus().isFailure());
		
		assertFalse(p.couldFinishSuccessfully(null, null));
	}
	
	@Test
	public void testTwoActivitiesSuccess() {
		Evaluatable e1 = new Evaluatable(1, false);
		Evaluatable e2 = new Evaluatable(1, false);
		
		DummyPartnerTrack partnerTrack = new DummyPartnerTrack();
		ParallelSplit p = new ParallelSplit(partnerTrack);
		p.initialize(Arrays.asList(new IEvaluatable[]{ e1, e2 }));
		
		assertTrue(p.couldFinishSuccessfully(null, null));
		p.handle(null, null);
		assertTrue(partnerTrack.hasReportedProgress());
		assertTrue(p.getStatus().isInProgress());
		
		assertTrue(p.couldFinishSuccessfully(null, null));
		p.handle(null, null);
		assertTrue(partnerTrack.hasReportedProgress());
		assertTrue(p.getStatus().isPassed());
		
		assertFalse(p.couldFinishSuccessfully(null, null));
	}
	
	@Test
	public void testTwoActivitiesFirstFails() {
		Evaluatable e1 = new Evaluatable(1, true);
		Evaluatable e2 = new Evaluatable(1, false);
		
		DummyPartnerTrack partnerTrack = new DummyPartnerTrack();
		ParallelSplit p = new ParallelSplit(partnerTrack);
		p.initialize(Arrays.asList(new IEvaluatable[]{ e1, e2 }));
		
		assertTrue(p.couldFinishSuccessfully(null, null));
		p.handle(null, null);
		assertTrue(partnerTrack.hasReportedProgress());
		assertTrue(p.getStatus().isFailure());
		
		assertTrue(p.couldFinishSuccessfully(null, null));
		p.handle(null, null);
		assertTrue(partnerTrack.hasReportedProgress());
		assertTrue(p.getStatus().isFailure());
		
		assertFalse(p.couldFinishSuccessfully(null, null));
	}
	
	@Test
	public void testTwoActivitiesSecondFails() {
		Evaluatable e1 = new Evaluatable(1, false);
		Evaluatable e2 = new Evaluatable(1, true);
		
		DummyPartnerTrack partnerTrack = new DummyPartnerTrack();
		ParallelSplit p = new ParallelSplit(partnerTrack);
		p.initialize(Arrays.asList(new IEvaluatable[]{ e1, e2 }));
		
		assertTrue(p.couldFinishSuccessfully(null, null));
		p.handle(null, null);
		assertTrue(partnerTrack.hasReportedProgress());
		assertTrue(p.getStatus().isInProgress());
		
		assertTrue(p.couldFinishSuccessfully(null, null));
		p.handle(null, null);
		assertTrue(partnerTrack.hasReportedProgress());
		assertTrue(p.getStatus().isFailure());
		
		assertFalse(p.couldFinishSuccessfully(null, null));
	}

}
