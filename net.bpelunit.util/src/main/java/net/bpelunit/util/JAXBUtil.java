package net.bpelunit.util;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public final class JAXBUtil {

	private JAXBUtil() {
	}
	
	public static XMLGregorianCalendar convertToXMLDateTime(Date d) {

		GregorianCalendar c = new GregorianCalendar();
		c.setTime(d);
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException("Error while converting " + d + " to its XML representation", e);
		}

	}

}
