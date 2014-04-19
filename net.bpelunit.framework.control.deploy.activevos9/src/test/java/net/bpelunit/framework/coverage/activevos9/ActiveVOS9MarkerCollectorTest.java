package net.bpelunit.framework.coverage.activevos9;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

public class ActiveVOS9MarkerCollectorTest {

	private final ActiveVOS9MarkerCollector coverage = new ActiveVOS9MarkerCollector(null);

	
	
	private List<LogEvent> readExampleLog(String logResourceName) throws Exception {
		InputStream in = getClass().getResourceAsStream(logResourceName);
		assertNotNull(in);
		return coverage.readLog(IOUtils.toString(in));
	}
}
