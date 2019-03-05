package org.frontend;

import javax.jms.JMSException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/frontend")
public class FrontendResource {
	
	@POST
	@Produces("application/json")
	public Response getDay(@FormParam("date") String date) throws JMSException{
		String response = Producers.getDayProducer().sendMessage(date);
		
		return Response.ok(response).build();
	}
}
