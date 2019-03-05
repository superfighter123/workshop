package org.frontend;

import javax.jms.JMSException;
import javax.naming.NamingException;

public class Producers {
	private static Producer dayProducer;
	
	public Producers() throws JMSException, NamingException {
		dayProducer = new Producer();
		dayProducer.create("day-producer", "day.q");
	}
	
	public static Producer getDayProducer() {
		return dayProducer;
	}
}
