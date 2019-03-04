package org.frontend;

import javax.jms.JMSException;

public class Producers {
	private static Producer dayProducer;
	
	public Producers() throws JMSException {
		dayProducer = new Producer();
		dayProducer.create("day-producer", "day.q");
	}
	
	public static Producer getDayProducer() {
		return dayProducer;
	}
}
