//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import Service.UserUtility;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import rmi.registry.LocateSimpleRegistry;
import rmi.registry.RemoteObjectRef;
import rmi.registry.SimpleRegistry;

public class Client {
    private static UserUtility stub = null;
    static String host = "localhost";
    static int port = 2004;
    static String serviceName = "UserManagement";
    static HashMap<Integer, String> topics;
    static String myUserName;
    static int cnt = 0;

    public Client() {
        topics = new HashMap<>();
        topics.put(1, "Movie");
        topics.put(2, "Sport");
        topics.put(3, "Music");
        topics.put(4, "Makeup");
    }

    public static void main(String[] args) throws IOException {
        SimpleRegistry simpleRegistry = LocateSimpleRegistry.getRegistry(host, port);
        System.out.println("located. " + simpleRegistry + "\n");
        topics = new HashMap<>();
        topics.put(1, "Movie");
        topics.put(2, "Sport");
        topics.put(3, "Music");
        topics.put(4, "Makeup");
        if (simpleRegistry != null) {
            RemoteObjectRef ror = simpleRegistry.lookup(serviceName);
            stub = (UserUtility)ror.localise();
            System.out.println("Usage: Type 1 for Register, Type 2 for Login, Type 3 for Exit\n 1.Register 2.Login 3.Exit");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch(choice) {
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("bye");
                    return;
            }
            if (choice == 1) {
                System.out.println("Please subscribe your interested topics (enter -1 to finish)");
                System.out.println("1. Movie  2. Sport  3. Music 4. Makeup");
                int num;
                while ((num = scanner.nextInt()) != -1) {
                    createSubscriber(num);
                }
            }
            while (true) {
                System.out.println("1. Get messages from your subscribed topics");
                System.out.println("2. Publish messages");
                int num = scanner.nextInt();
                switch (num) {
                    case 1:
                        getMessagesFromSubscribers();
                        break;
                    case 2:
                        System.out.println("Please choose one topic");
                        System.out.println("1. Movie  2. Sport  3. Music 4. Makeup");
                        int topicNum = scanner.nextInt();
                        System.out.println("Please write your comment");
                        String content = scanner.nextLine();
                        publishMessage(topics.get(topicNum), content);
                        break;

                }
            }

        } else {
            System.out.println("no registry found.");
        }

    }

    public static void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("please enter your username");
        String userName = scanner.next();
        myUserName = userName;
        System.out.println("please enter your password");
        String password = scanner.next();

        try {
            String msg = stub.register(userName, password);
            System.out.println(msg);
        } catch (IOException e) {
            System.out.println("register exception");
            e.printStackTrace();
        }
    }

    public static void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("username: ");
        String userName = scanner.next();
        myUserName = userName;
        System.out.println("password: ");
        String password = scanner.next();

        try {
            String msg = stub.login(userName, password);
            System.out.println(msg);
        } catch (RemoteException e) {
            System.out.println("login exception");
            e.printStackTrace();
        }
    }

    public static void createSubscriber(int num) {
        String subId = myUserName + String.valueOf(cnt);
        String subName = myUserName + "Sub" + String.valueOf(cnt);
        String topic = topics.get(num);
        try {
            String msg = stub.createSubscription(myUserName, subId, topic, subName);
            System.out.println(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        cnt++;
    }

    public static void getMessagesFromSubscribers() {
        try {
            HashMap<String, String> messages = stub.getMessagesFromSubscribers(myUserName);
            outputMessages(messages);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Get messages failure");
        }
    }

    public static void publishMessage(String topic, String content) {
        String pubId = myUserName + String.valueOf(cnt);
        try {
            String returnMsg = stub.publishMessage(pubId, topic, content);
            System.out.println(returnMsg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        cnt++;
    }

    public static void outputMessages(HashMap<String, String> messages) {
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
