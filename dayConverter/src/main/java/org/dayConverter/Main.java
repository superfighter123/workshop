package org.dayConverter;

import java.io.File;

import javax.jms.JMSException;
import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class Main {
	public static void main(String[] args) throws LifecycleException, ServletException, JMSException {
		new Main().start();
	}

	public void start() throws ServletException, LifecycleException, JMSException {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8081);

		// Context ctx = tomcat.addContext("/", new File(".").getAbsolutePath());

		Context ctx = tomcat.addWebapp("/frontend", new File("src/main/webapp").getAbsolutePath());

		Wrapper servletWrapper = Tomcat.addServlet(ctx, "jersey-container-servlet", resourceConfig());
		// servletWrapper.addInitParameter("javax.ws.rs.Application",
		// "rnd.web.service.rest.AppConfig");
		ctx.addServletMappingDecoded("/rest/*", "jersey-container-servlet");

		tomcat.start();
		tomcat.getServer().await();

		Consumer consumer = new Consumer();
		
		while(true) {
			consumer.getMessage();
		}
	}

	private ServletContainer resourceConfig() {
		return new ServletContainer(new ResourceConfig(new ResourceLoader().getClasses()));
	}
}