package net.bpelunit.framework.coverage.activevos9;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IProcess;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

public class LogEventTest {

	private final ActiveVOS9MarkerCollector coverage = new ActiveVOS9MarkerCollector(null);

	@Test
	public void constructorCorrectlyParsesLogLine() throws Exception {
		String logLine = "[309][2014-01-08 16:19:32.551] Process : Executing [/process]";
		LogEvent e = new LogEvent(logLine);
		
		assertEquals(309, e.getProcessId());
		assertEquals("2014-01-08 16:19:32.551", e.getEventDate());
		assertEquals("Process", e.getActivityType());
		assertEquals("", e.getActivityName());
		assertEquals("Executing", e.getEventType());
		assertEquals("/process", e.getActivityId());
	}
	
	@Test
	public void constructorCorrectlyParsesLogLineWithFlag() throws Exception {
		String logLine = "[506][2014-01-10 22:09:54.252] Empty Empty3: Will not execute  [/process/sequence/if/else/empty[@name='Empty3']] [d]";
		LogEvent e = new LogEvent(logLine);
		
		assertEquals(506, e.getProcessId());
		assertEquals("2014-01-10 22:09:54.252", e.getEventDate());
		assertEquals("Empty", e.getActivityType());
		assertEquals("Empty3", e.getActivityName());
		assertEquals("Will not execute", e.getEventType());
		assertEquals("/process/sequence/if/else/empty[@name='Empty3']", e.getActivityId());
		assertEquals("d", e.getFlags());
	}
	
	@Test
	public void toStringReturnsOriginalLogEntry() throws Exception {
		String logLine = "[309][2014-01-08 16:19:32.551] Process : Executing [/process]";
		LogEvent e = new LogEvent(logLine);
		
		assertEquals(logLine, e.toString());
	}
	
	@Test
	@Ignore("TODO Update with saxon-dom")
	public void getActivityXPathMatchesBPELElementInIfs() throws Exception {
		List<LogEvent> logLines = readLogAsEvents("coverage-if-branch1.log");
		Set<String> xpaths = new HashSet<String>();
		for(LogEvent e : logLines) {
			xpaths.add(e.getActivityXPath());
		}
		
		IProcess process = BpelFactory.loadProcess(getClass().getResourceAsStream("CoverageTestCases-if.bpel"));
		for(String activityXPath : xpaths) {
			
			process.getElementsByXPath("/");
			
			List<IBpelObject> elementsByXPath = process.getElementsByXPath(activityXPath);
			assertEquals(1, elementsByXPath.size());
		}
	}
	
	@Test
	public void getActivityXPathToProcessWorks() throws Exception {
		String activityId = "/process";
		LogEvent e = createDummyLogEventWithActivityId(activityId);
		
		assertEquals("/", e.getActivityXPath());
	}
	
	@Test
	public void getActivityXPathToElementInSequenceWorks() throws Exception {
		String activityId = "/process/sequence/receive[@name='Receive']";
		LogEvent e = createDummyLogEventWithActivityId(activityId);
		
		assertEquals("/bpel:process/bpel:sequence/bpel:receive[@name='Receive']", e.getActivityXPath());
	}
	
	@Test
	public void getActivityXPathToElementInIfWorks() throws Exception {
		String activityId = "/process/sequence/if/if-condition/empty[@name='Empty1']";
		LogEvent e = createDummyLogEventWithActivityId(activityId);
		
		assertEquals("/bpel:process/bpel:sequence/bpel:if/bpel:empty[@name='Empty1']", e.getActivityXPath());
	}
	
	@Test
	public void getActivityXPathToElementInElseIfWorks() throws Exception {
		String activityId = "/process/sequence/if/elseif/empty[@name='Empty2']";
		LogEvent e = createDummyLogEventWithActivityId(activityId);
		
		assertEquals("/bpel:process/bpel:sequence/bpel:if/bpel:elseif/bpel:empty[@name='Empty2']", e.getActivityXPath());
	}
	
	@Test
	public void getActivityXPathToElementInWhileWorks() throws Exception {
		String activityId = "/process/sequence/while/assign";
		LogEvent e = createDummyLogEventWithActivityId(activityId);
		
		assertEquals("/bpel:process/bpel:sequence/bpel:while/bpel:assign", e.getActivityXPath());
	}
	
	@Test
	public void getActivityXPathToElementInOnMessageEventHandlerWorks() throws Exception {
		String activityId = "/process/sequence/scope/eventHandlers/onEvent[2]/scope[instance()=1]/empty";
		LogEvent e = createDummyLogEventWithActivityId(activityId);
		
		assertEquals("/bpel:process/bpel:sequence/bpel:scope/bpel:eventHandlers/bpel:onEvent[2]/bpel:scope/bpel:empty", e.getActivityXPath());
	}
	
	@Test
	public void getActivityXPathToElementInOnAlarmEventHandlerWorks() throws Exception {
		String activityId = "/process/sequence/scope/eventHandlers/onAlarm/scope/empty";
		LogEvent e = createDummyLogEventWithActivityId(activityId);
		
		assertEquals("/bpel:process/bpel:sequence/bpel:scope/bpel:eventHandlers/bpel:onAlarm/bpel:scope/bpel:empty", e.getActivityXPath());
	}
	
	@Test
	public void getActivityXPathToElementInParallelForEachWorks() throws Exception {
		String activityId = "/process/sequence/forEach[@name='ForEach']/scope[@name='ForEach'][instance()=1]/empty[@name='Empty1']";
		LogEvent e = createDummyLogEventWithActivityId(activityId);
		assertEquals("/bpel:process/bpel:sequence/bpel:forEach[@name='ForEach']/bpel:scope[@name='ForEach']/bpel:empty[@name='Empty1']", e.getActivityXPath());
		
		activityId = "/process/sequence/forEach[@name='ForEach']/scope[@name='ForEach'][instance()=4]/empty[@name='Empty1']";
		e = createDummyLogEventWithActivityId(activityId);
		assertEquals("/bpel:process/bpel:sequence/bpel:forEach[@name='ForEach']/bpel:scope[@name='ForEach']/bpel:empty[@name='Empty1']", e.getActivityXPath());
	}
	
	@Test
	public void getActivityXPathToElementInSequentialForEachWorks() throws Exception {
		String activityId = "/process/sequence/forEach[@name='ForEach']/scope[@name='ForEach']/empty[@name='Empty1']";
		LogEvent e = createDummyLogEventWithActivityId(activityId);
		assertEquals("/bpel:process/bpel:sequence/bpel:forEach[@name='ForEach']/bpel:scope[@name='ForEach']/bpel:empty[@name='Empty1']", e.getActivityXPath());
	}
	
	@Test
	public void getActivityXPathToElementWhenTwoElementsInSameContainerHaveTheSameNameWorks() throws Exception {
		String activityId = "/process/sequence/empty[@name='Empty1'][2]";
		LogEvent e = createDummyLogEventWithActivityId(activityId);
		assertEquals("/bpel:process/bpel:sequence/bpel:empty[@name='Empty1'][2]", e.getActivityXPath());
	}
	
	@Test
	@Ignore("Does not work 100% for now but is not crucial")
	public void toStringReturnsOriginalLogEntryFromExampleLog() throws Exception {
		List<String> exampleLog = readLogAsLines("process.log");
		for(String logLine : exampleLog) {
			LogEvent e = new LogEvent(logLine);
			assertEquals(logLine, e.toString());
		}
	}

	private List<LogEvent> readLogAsEvents(String logName) throws IOException {
		InputStream processLogStream = getClass().getResourceAsStream(logName);
		assertNotNull(processLogStream);
		return coverage.readLog(IOUtils.toString(processLogStream));
	}

	List<String> readLogAsLines(String logName) throws IOException {
		List<String> result = new ArrayList<String>();
		InputStream processLogStream = getClass().getResourceAsStream(logName);
		assertNotNull(processLogStream);
		
		BufferedReader logReader = null;
		try {
			logReader = new BufferedReader(new InputStreamReader(processLogStream));
			String line;
			while((line = logReader.readLine()) != null) {
				try {
					result.add(line);
				} catch(IllegalArgumentException e) {
					// Ignore wrongly formatted lines
				}
			}
			
			return result;
		} finally {
			IOUtils.closeQuietly(logReader);
			IOUtils.closeQuietly(processLogStream);
		}
	}
	
	private LogEvent createDummyLogEventWithActivityId(String activityId) {
		return new LogEvent(0, "", "", "", "", activityId);
	}
	
}
