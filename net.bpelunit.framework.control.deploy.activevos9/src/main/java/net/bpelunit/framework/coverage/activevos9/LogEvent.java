package net.bpelunit.framework.coverage.activevos9;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEvent {

	private long processId;
	private String eventDate;
	private String activityType;
	private String activityName;
	private String eventType;
	private String activityId;
	private String flags = "";
	private String xpath;

	private static final String LOG_REGEX = "^\\[(.*?)\\]\\[(.*?)\\] ([\\S]*)(.*?):(.*?)\\[(.*)\\]$";
	private static final Pattern LOG_PATTERN = Pattern.compile(LOG_REGEX);

	public LogEvent(long processId, String eventDate, String activityType,
			String activityName, String eventType, String activityId) {
		super();
		this.processId = processId;
		this.eventDate = eventDate;
		this.activityType = activityType;
		this.activityName = activityName;
		this.eventType = eventType;
		this.activityId = activityId;
	}

	public LogEvent(String logLine) {

		synchronized (LOG_PATTERN) {
			Matcher m = LOG_PATTERN.matcher(logLine);
			if (!m.find()) {
				throw new IllegalArgumentException("Log Entry \"" + logLine
						+ "\"did not conform to specification");
			}
			processId = Long.parseLong(m.group(1));
			eventDate = m.group(2);
			activityType = m.group(3);
			activityName = m.group(4).trim();
			eventType = m.group(5).trim();
			activityId = m.group(6);
		}

		if (activityId.endsWith("[d")) {
			flags = "d";
			activityId = activityId.substring(0,
					activityId.length() - "[d".length() - 2).trim();
		}
	}

	public long getProcessId() {
		return processId;
	}

	public String getEventDate() {
		return eventDate;
	}

	public String getActivityType() {
		return activityType;
	}

	public String getActivityName() {
		return activityName;
	}

	public String getEventType() {
		return eventType;
	}

	public boolean isEventTypeExecuting() {
		return "Executing".equals(getEventType());
	}
	
	public String getActivityId() {
		return activityId;
	}

	public String getFlags() {
		return flags;
	}

	@Override
	public String toString() {
		return String.format("[%d][%s] %s %s: %s [%s]", processId, eventDate,
				activityType, activityName, eventType, activityId);
	}

	/**
	 * Returns an XPath expression that can be used to select the first BPEL XML element that has triggered this event.
	 * 
	 * If the XPath expression returns multiple XML elements, the first one is the correct one.
	 * 
	 * The namespace for the BPEL constructs is bound to the prefix bpel, e.g. &lt;bpel:empty /&gt;
	 * 
	 * @return
	 */
	public synchronized String getActivityXPath() {
		if (xpath == null) {
			xpath = getActivityId();
			if ("/process".equals(xpath)) {
				return "/";
			}
			
			// Elements below an if are put under if/if-condition which does not match the BPEL model structure
			xpath = xpath.replaceAll("/if-condition", "");
			
			// in parallel for-eaches, the branch is denoted with [instance()=x]
			// which must be removed to point to a model element
			xpath = xpath.replaceAll("\\[instance\\(\\)=[0-9]+\\]", "");
			
			// Add namespace prefix to all activities, assumption: / is only used as a path separator in the Activity ID
			xpath = xpath.replaceAll("/", "/bpel:");
		}
		return xpath;
	}

	public long getEventDateAsTimestamp() {
		// TODO Parse log event date
		return 0;
	}
}
