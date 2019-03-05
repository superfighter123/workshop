package org.dayConverter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

	private String clientId;
	private Connection connection;
	private Session session;
	private MessageConsumer messageConsumer;
	private MessageProducer replyProducer;

	public void create(String clientId, String queueName) throws JMSException {
		this.clientId = clientId;

		// create a Connection Factory
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);

		// create a Connection
		connection = connectionFactory.createConnection();
		connection.setClientID(clientId);

		// create a Session
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// create the Queue to which messages will be sent
		Queue queue = session.createQueue(queueName);

		// create a MessageConsumer for receiving messages
		messageConsumer = session.createConsumer(queue);
		
		//create a MessageProducer for sending replies
		replyProducer = session.createProducer(null);
		
		// start the connection in order to receive messages
	    connection.start();
	}

	public void closeConnection() throws JMSException {
		connection.close();
	}

	public void getMessage() throws JMSException {

		String response = "No response";

		Message message = messageConsumer.receive();

		if (message != null) {
			// cast the message to the correct type
			TextMessage responseTextMessage = (TextMessage) message;

			// retrieve the message content
			String text = responseTextMessage.getText();
			LOGGER.debug(clientId + ": received message with text='{}'", text);

			// create greeting
			response = text;
		} else {
			LOGGER.debug(clientId + ": no message received");
		}

		TextMessage responseMessage = session.createTextMessage(response);
		
		replyProducer.send(message.getJMSReplyTo(), responseMessage);
		
		LOGGER.info("response={}", response);
	}

}