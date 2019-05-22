package Service.messageService;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.jms.JMSException;

public class MessageTest {
    public static void main(String[] args) throws JMSException {
        Publisher publisher1 = new Publisher();
        Publisher publisher2 = new Publisher();
        DurableSubscriber subscriber1 = new DurableSubscriber();
        subscriber1.create("subscriber1", "topic1", "sub1");
        publisher1.create("publisher1", "topic1");
        publisher2.create("publisher2", "topic2");
        // subscriber1.closeConnection();
        publisher1.sendComment("message1");
        // subscriber1.create("subscriber1", "topic1", "sub1");
        String msg = subscriber1.getMessage(1000);
        System.out.println(msg);
        publisher1.closeConnection();
        publisher2.closeConnection();
        subscriber1.removeDurableSubscriber();
        subscriber1.closeConnection();
    }
}
