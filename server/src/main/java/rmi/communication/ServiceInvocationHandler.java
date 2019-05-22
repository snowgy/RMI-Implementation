package rmi.communication;

import rmi.registry.RemoteObjectRef;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/*
    The class implements InvocationHandler. It decides how
    the proxy handles calls. The proxy was generated dynamically
    in the localise() function of RemoteObjectRef
 */

public class ServiceInvocationHandler implements InvocationHandler {

    private RemoteObjectRef ror;

    public ServiceInvocationHandler(RemoteObjectRef ror){
        this.ror = ror;
    }

    public void startServer(){
        try(ServerSocket serverSocket = new ServerSocket(ror.getPort())) {
            Executor executor = Executors.newFixedThreadPool(100);
            Socket sock = serverSocket.accept();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("====== begin execute ======");
                    Socket conn = sock;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handler(){

    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Message request = new Message(method.getName(), args);
        try {
            System.out.println("====== begin execute ======");
            // create a socket to the remote server
            Socket sock = new Socket(ror.getIP_adr(), ror.getPort());
            // create input and output stream
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            // send ror to the server to find the corresponding skeleton Object
            out.writeObject(ror.getRemote_Interface_Name());
            System.out.println("====== send " + request.getMethod() + " request to the remote ======");
            // send request
            out.writeObject(request);
            // accept reply
            Message reply = (Message) in.readObject();

            return reply.getReturnValue();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    public RemoteObjectRef getRor() {
        return ror;
    }

    public void setRor(RemoteObjectRef ror) {
        this.ror = ror;
    }

}
