package org.frontend;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;

public class Listener implements javax.servlet.ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		Producers producers;
		try {
			producers = new Producers();
			System.out.println(producers.getDayProducer());
			System.out.println("test");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}