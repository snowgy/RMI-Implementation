//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import Service.UserUtility;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import rmi.registry.LocateSimpleRegistry;
import rmi.registry.RemoteObjectRef;
import rmi.registry.SimpleRegistry;

public class ConcurrentClient {
    private static int concurrent_num = 100;
    private static CountDownLatch cdl = new CountDownLatch(1);
    static String host = "localhost";
    static int port = 2004;
    static String serviceName = "UserManagement";
    private static AtomicInteger count = new AtomicInteger(0);
    private static ReentrantLock lock = new ReentrantLock();

    public ConcurrentClient() {
    }

    public static void main(String[] var0) throws InterruptedException {
        if (var0[0] != null) {
            concurrent_num = Integer.valueOf(var0[0]);
        }

        for (int i = 0; i < concurrent_num; ++i) {
            new Thread(() -> {
                try {
                    /* wait until the countDownLatch is equal to 0 */
                    cdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /* After count down, execute register */
                register();
            }).start();
            /* wake up all the threads at the same time */
            cdl.countDown();
        }

        System.out.println(concurrent_num + " prepared");
        cdl.countDown();
    }

    public static void register() {
        SimpleRegistry simpleRegistry = LocateSimpleRegistry.getRegistry(host, port);
        if (simpleRegistry != null) {
            try {
                RemoteObjectRef ror = simpleRegistry.lookup(serviceName);
                // UserUtility var1 = null;
                //RemoteObjectRef var2 = new RemoteObjectRef("localhost", 2005, 1, "UserUtility");
                UserUtility userUtility = (UserUtility)ror.localise();
                String username = getRandomString(10);
                String password = getRandomString(12);
                userUtility.register(username, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        count.incrementAndGet();
        System.out.println("count: " + count);
    }

    public static String getRandomString(int var0) {
        String var1 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random var2 = new Random();
        StringBuffer var3 = new StringBuffer();

        for(int var4 = 0; var4 < var0; ++var4) {
            int var5 = var2.nextInt(62);
            var3.append(var1.charAt(var5));
        }

        return var3.toString();
    }

}
