package Service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface UserUtility extends Remote{
    String register(String username, String password)
            throws RemoteException;
    String login(String username, String password)
            throws RemoteException;
    String createSubscription(String userName, String subId, String topic, String subName)
            throws RemoteException;
    HashMap<String, String> getMessagesFromSubscribers(String userName)
            throws RemoteException;
    String publishMessage(String pubId, String topic, String message)
            throws RemoteException;
}
