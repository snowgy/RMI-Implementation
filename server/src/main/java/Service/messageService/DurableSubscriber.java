package Service.messageService;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class DurableSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(DurableSubscriber.class);
    private String clientId;
    private Connection connection;
    private Session session;
    private MessageConsumer messageConsumer;
    private String topic;

    private String subscriptionName;

    public void create(String clientId, String topicName, String subscriptionName) throws JMSException {
        this.clientId = clientId;
        this.subscriptionName = subscriptionName;
        this.topic = topicName;
        // create a Connection Factory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);

        // create a Connection
        connection = connectionFactory.createConnection();
        connection.setClientID(clientId);

        // create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // create the topic from which messages will be received
        Topic topic = session.createTopic(topicName);

        // create a MessageConsumer for receiving messages
        messageConsumer = session.createDurableSubscriber(topic, subscriptionName);

        // start the connection in order to receive messages
        connection.start();
    }

    public void removeDurableSubscriber() throws JMSException {
        messageConsumer.close();
        session.unsubscribe(subscriptionName);
    }

    public void closeConnection() throws JMSException {
        connection.close();
    }

    public String getMessage(int timeout) throws JMSException {
        Message message = messageConsumer.receive(timeout);
        String msg = "no message";
        // check if a message was received
        if (message != null) {
            // cast the message to the correct type
            TextMessage textMessage = (TextMessage) message;

            // receive the message content
            String text = textMessage.getText();
            msg = text;

        } else {
            LOGGER.error(clientId + ": no message received");
        }
        LOGGER.error(clientId + " receive message = {}", msg);
        return msg;
    }

    public String getTopic() {
        return topic;
    }
}
