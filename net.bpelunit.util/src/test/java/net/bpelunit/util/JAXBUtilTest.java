package net.bpelunit.util;

import static org.junit.Assert.*;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Test;

public class JAXBUtilTest {

	@Test
	public void testConvertToXMLGregorianCalendar() throws Exception {
		Date d = new Date(1393927848166l); // 2014-03-04T11:10:48.166
		XMLGregorianCalendar xmlDate = JAXBUtil.convertToXMLDateTime(d);
		assertTrue(xmlDate.toXMLFormat().startsWith("2014-03-04T11:10:48.166")); // time zone is dependent on system
	}
	
}
