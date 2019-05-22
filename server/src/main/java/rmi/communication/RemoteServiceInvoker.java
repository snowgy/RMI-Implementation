package rmi.communication;

import rmi.registry.RORtbl;
import rmi.registry.RemoteObjectRef;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Spliterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/*
    The server class creates a RemoteServiceInvoker to listen
    to the specified port, which could receive the request from
    client. Then, the invoker will call the local object to get
    the return result and send the reply to the client.
 */
public class RemoteServiceInvoker {
    String serverIP = "localhost";
    int port = 2005;
    private RemoteObjectRef ror;
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public RemoteServiceInvoker(RemoteObjectRef ror) {
        this.ror = ror;
    }
    public RemoteObjectRef getRor() {
        return ror;
    }

    public void setRor(RemoteObjectRef ror) {
        this.ror = ror;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void startServer(){
        try(ServerSocket serverSocket = new ServerSocket(ror.getPort())){
            Executor executor = Executors.newFixedThreadPool(100);
            while(true){
                try {
                    Socket socket = serverSocket.accept();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            Socket conn = socket;
                            handler(conn);
                        }
                    });
                } catch (IOException e){
                    e.printStackTrace();
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void handler(Socket socket){
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        try {
            System.out.println("====== 1. receive request ======");
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("====== 2. created streams ======");
            String interfaceName = (String) in.readObject();
            Class c = Class.forName("Service." + interfaceName + "Impl");
            Object stub = c.newInstance();
            Message request = (Message) in.readObject();
            String method = request.getMethod();
            System.out.println("====== 3. invoke " + method + " ======");
            request.invoke(stub);
            System.out.println("====== 4. got " + request.getReturnValue() + " ======");
            request.setReturnValue(request.getReturnValue());
            out.writeObject(request);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (in != null)
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (out != null)
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void run(){
        startServer();
    }
}
