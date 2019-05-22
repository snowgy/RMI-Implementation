package Service.messageService;

import java.util.ArrayList;
import java.util.HashMap;

public class SubscriberMap {
    public static HashMap<String, ArrayList<DurableSubscriber>> subscriberMap = new HashMap<>();

    public static void add(String userName, DurableSubscriber subscriber) {
        if (subscriberMap.containsKey(userName)) {
            ArrayList<DurableSubscriber> arr = subscriberMap.get(userName);
            arr.add(subscriber);
            subscriberMap.put(userName, arr);
        } else {
            ArrayList<DurableSubscriber> arr = new ArrayList<>();
            arr.add(subscriber);
            subscriberMap.put(userName, arr);
        }
    }

    public static ArrayList<DurableSubscriber> get(String userName) {
        return subscriberMap.get(userName);
    }
}
