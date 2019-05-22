package Service;

import Service.messageService.DurableSubscriber;
import Service.messageService.Publisher;
import Service.messageService.SubscriberMap;
import dao.UserRepository;
import dao.UserRepositoryImpl;
import model.User;

import javax.jms.JMSException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserUtilityImpl implements UserUtility {
    UserRepository userRepository = new UserRepositoryImpl();
    PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
    public static int count = 0;


    @Override
    synchronized public String register(String username, String password) throws RemoteException {
        try{
            userRepository.openConncetion();
            User _user = userRepository.getUserByUsername(username);
            if (_user == null){
                String hashed_password = passwordAuthentication.hash(password.toCharArray());
                User user = new User(username, hashed_password);
                userRepository.saveUser(user);
                return "register success";
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            userRepository.closeConnection();
        }
        return "register success";
    }

    @Override
    public String login(String username, String password) throws RemoteException {
        userRepository.openConncetion();
        User user = userRepository.getUserByUsername(username);
        if(user == null) {
            userRepository.closeConnection();
            return "login failure";
        }
        else{
            String token = user.getPassword();
            if (passwordAuthentication.authenticate(password.toCharArray(), token)){
                userRepository.closeConnection();
                return "login success";
            }
        }
        userRepository.closeConnection();
        return "login failure";
    }

    @Override
    public String createSubscription(String userName, String subId, String topic, String subName) throws RemoteException {
        DurableSubscriber subscriber = new DurableSubscriber();
        try {
            subscriber.create(subId, topic, subName);
            SubscriberMap.add(userName, subscriber);
            return "subscribe success";
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return "subscribe failure";
    }

    @Override
    public HashMap<String, String> getMessagesFromSubscribers(String userName) throws RemoteException {
        ArrayList<DurableSubscriber> arr = SubscriberMap.get(userName);
        HashMap<String, String> messages = new HashMap<>();
        for(DurableSubscriber subscriber : arr) {
            String topicName = subscriber.getTopic();
            try {
                String message = subscriber.getMessage(1000);
                messages.put(topicName, message);
            } catch (JMSException e) {
                e.printStackTrace();
            }

        }
        return messages;
    }

    @Override
    public String publishMessage(String pubId, String topic, String message) throws RemoteException {
        Publisher publisher = new Publisher();
        try {
            publisher.create(pubId, topic);
            publisher.sendComment(message);
            return "send message success";
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return "send message failure";
    }
}
