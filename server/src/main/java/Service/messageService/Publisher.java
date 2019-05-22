package Service.messageService;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class Publisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(Publisher.class);

    private String clientId;
    private Connection connection;
    private Session session;
    private MessageProducer messageProducer;

    public void create(String clientId, String topicName) throws JMSException {
        this.clientId = clientId;
        // create a connection factory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);

        // create a connection
        this.connection = connectionFactory.createConnection();
        this.connection.setClientID(clientId);

        // create a session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // create a topic to which messages will be sent
        Topic topic = session.createTopic(topicName);

        // create a MessageProducer for sending messages
        messageProducer = session.createProducer(topic);
    }

    public void closeConnection() throws JMSException {
        connection.close();
    }

    public void sendComment(String comment) throws JMSException {
        // create a JMS TextMessage
        TextMessage textMessage = session.createTextMessage(comment);

        // send the message to the topic destination
        messageProducer.send(textMessage);

        LOGGER.error(clientId + ": send message with text='{}'", comment);
    }
}
