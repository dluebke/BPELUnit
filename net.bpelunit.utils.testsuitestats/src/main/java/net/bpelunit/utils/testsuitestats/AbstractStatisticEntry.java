package net.bpelunit.utils.testsuitestats;


abstract class AbstractStatisticEntry implements IStatisticEntry {

	public static final String TYPE_TESTSUITE = "TEST_SUITE";
	public static final String TYPE_TESTCASE = "TEST_CASE";
	public static final String TYPE_TESTTRACK = "TEST_TRACK";
	public static final String TYPE_TESTACTIVITY = "TEST_ACTIVITY";
	public static final String TYPE_TESTSUITES = "TEST_SUITES";
	
	@Override
	public String toString() {
		return String.format("%s\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%s",
				getTitle(), 
				getType(),
				getCountAllReceives(),
				getCountAllSends(),
				getCountCompleteHumanTask(),
				getCountReceiveOnly(),
				getCountReceiveSend(),
				getCountSendOnly(),
				getCountSendReceive(),
				getCountSendReceiveAsync(),
				getCountReceiveSendAsync(),
				getCountWait(),
				isClientTrackUsed() + ""
		);
	}

	protected abstract String getType();

}
