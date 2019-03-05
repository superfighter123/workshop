package org.frontend;

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

public class Producer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

	private String clientId;
	private Connection connection;
	private Session session;
	private MessageProducer messageProducer;

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

		// create a MessageProducer for sending messages
		messageProducer = session.createProducer(queue);
	}

	public void closeConnection() throws JMSException {
		connection.close();
	}

	public String sendMessage(String message) throws JMSException {

		//create a temporary queue for the response
		Destination tempDst = session.createTemporaryQueue();
		MessageConsumer responseConsumer = session.createConsumer(tempDst);

		// create a JMS TextMessage
		TextMessage textMessage = session.createTextMessage(message);
		textMessage.setJMSReplyTo(tempDst);

		// send the message to the queue destination
		messageProducer.send(textMessage);

		LOGGER.debug(clientId + ": sent message with text='{}'", message);

		String response = "No response";

		Message responseMessage = responseConsumer.receive(1000);

		if (responseMessage != null) {
			// cast the message to the correct type
			TextMessage responseTextMessage = (TextMessage) responseMessage;

			// retrieve the message content
			String text = responseTextMessage.getText();
			LOGGER.debug(clientId + ": received message with text='{}'", text);

			// create greeting
			response = text;
		} else {
			LOGGER.debug(clientId + ": no message received");
		}

		LOGGER.info("response={}", response);
		return response;
	}

}