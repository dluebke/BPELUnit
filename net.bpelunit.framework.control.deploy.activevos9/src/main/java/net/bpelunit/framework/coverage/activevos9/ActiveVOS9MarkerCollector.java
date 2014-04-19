package net.bpelunit.framework.coverage.activevos9;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessInstanceDetail;

import net.bpelunit.activevos9.ActiveVOSAdministrativeFunctions;
import net.bpelunit.framework.coverage.marker.Marker;
import net.bpelunit.framework.coverage.marker.MarkerFactory;
import net.bpelunit.framework.coverage.marker.MarkerInstance;
import net.bpelunit.framework.coverage.marker.MarkerInstanceList;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IProcess;

public class ActiveVOS9MarkerCollector {

	private ActiveVOSAdministrativeFunctions administrativeFunctions;

	public ActiveVOS9MarkerCollector(ActiveVOSAdministrativeFunctions administrativeFunctions) {
		this.administrativeFunctions = administrativeFunctions;
	}
	
	public void fetchLogInfoForTestCase(Calendar startedFrom, Calendar startedTill, String testCase,
			List<IProcess> processes) {
		List<ProcessInstance> processInstances = fetchProcessesInstancesStartedBetween(startedFrom, startedTill);
		processInstances = filterProcessInstancesContainedInDeployment(processInstances, processes);
		fetchLogsForAllProcessInstances(processInstances);
	}

	private void fetchLogsForAllProcessInstances(
			List<ProcessInstance> processInstances) {
		for(ProcessInstance pi : processInstances) {
			String log = administrativeFunctions.getLogForProcessInstance(pi.getProcessId());
			pi.setLog(log);
		}
	}

	private List<ProcessInstance> filterProcessInstancesContainedInDeployment(
			List<ProcessInstance> processInstances, List<IProcess> processes) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private List<ProcessInstance> fetchProcessesInstancesStartedBetween(
			Calendar startedFrom, Calendar startedTill) {
		List<AesProcessInstanceDetail> processInstances = administrativeFunctions.getProcessInstancesStartedBetween(startedFrom.getTime(), startedTill.getTime());
		
		List<ProcessInstance> result = new ArrayList<ProcessInstance>();
		for(AesProcessInstanceDetail p : processInstances) {
			ProcessInstance pi = new ProcessInstance(null, null, p.getProcessId(), p.getName());
			result.add(pi);
		}
		
		return result;
	}

	MarkerInstanceList convertLogEventsToMarkerInstanceList(
			List<LogEvent> logEvents, String testCase,
			MarkerFactory markerFactory, IProcess process) {
		MarkerInstanceList result = new MarkerInstanceList();

		for (LogEvent e : logEvents) {
			IBpelObject referencedElement = process
					.getElementsByXPath(e.getActivityXPath()).get(0);

			if(referencedElement != null && referencedElement instanceof IActivity) {
				IActivity referencedActivity = (IActivity)referencedElement;
			
				List<Marker> markers = markerFactory
						.getMarkersForInstrumentedActivity(referencedActivity);
				for (Marker m : markers) {
					MarkerInstance mi = new MarkerInstance(m, testCase,
							e.getEventDateAsTimestamp());
					result.add(mi);
				}
			}
		}

		return result;
	}

	List<LogEvent> readLog(String processLog) throws IOException {
		List<LogEvent> result = new ArrayList<LogEvent>();

		String[] lines = processLog.split("\n");
		for (String line : lines) {
			try {
				result.add(new LogEvent(line));
			} catch (IllegalArgumentException e) {
				// Ignore wrongly formatted lines
			}
		}

		return result;
	}
}
