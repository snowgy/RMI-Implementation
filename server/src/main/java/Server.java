
import Service.UserUtility;
import Service.UserUtilityImpl;
import dao.UserRepositoryImpl;
import rmi.communication.RemoteServiceInvoker;
import rmi.registry.LocateSimpleRegistry;
import rmi.registry.RemoteObjectRef;
import rmi.registry.SimpleRegistry;

import java.io.IOException;

public class Server {
    static String host = "localhost";
    static int bind_port = 2005;
    static int registry_port = 2004;
    static String serviceName = "UserManagement";
    static String interfaceName = "UserUtility";

    public Server() {}

    public static void main(String[] args) throws IOException {
            RemoteObjectRef ror = new RemoteObjectRef(host, bind_port, 1, interfaceName);
            SimpleRegistry sr  = LocateSimpleRegistry.getRegistry(host, registry_port);
            if (sr != null){
                sr.rebind(serviceName, ror);
                RemoteServiceInvoker remoteServiceInvoker = new RemoteServiceInvoker(ror);
                remoteServiceInvoker.run();
                System.err.println(UserUtilityImpl.count);
            }
    }
}
