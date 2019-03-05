package org.frontend;

import java.io.File;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class Main {	
	public static void main(String[] args) throws LifecycleException, ServletException, JMSException, NamingException {
		new Main().start();
	}
	
	public void start() throws ServletException, LifecycleException, JMSException, NamingException{
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);

//		Context ctx = tomcat.addContext("/", new File(".").getAbsolutePath());
		
		Context ctx = tomcat.addWebapp("/frontend", new File("src/main/webapp").getAbsolutePath());
				
		Wrapper servletWrapper = Tomcat.addServlet(ctx, "jersey-container-servlet",
				resourceConfig());
//		servletWrapper.addInitParameter("javax.ws.rs.Application", "rnd.web.service.rest.AppConfig");
		ctx.addServletMappingDecoded("/rest/*", "jersey-container-servlet");
//		
//		ContextResource resource = new ContextResource();
//		resource.setName("jms/ConnectionFactory");
//		resource.setAuth("Container");
//		resource.setType("org.apache.activemq.ActiveMQConnectionFactory");
//		resource.setDescription("JMS Connection Factory");
//		resource.setProperty("factory", "org.apache.activemq.jndi.JNDIReferenceFactory");
//		resource.setProperty("brokerURL", "vm://localhost");
//		resource.setProperty("brokerName", "LocalActiveMQBroker");
//		
//		ctx.getNamingResources().addResource(resource);
		
		tomcat.start();
		
		Producers producers = new Producers();
		System.out.println(producers.getDayProducer());
		
		tomcat.getServer().await();
	}
	
	private ServletContainer resourceConfig() {
        return new ServletContainer(new ResourceConfig(
                new ResourceLoader().getClasses()));
    }
}