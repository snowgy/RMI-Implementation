package rmi.registry;


import rmi.communication.RemoteServiceInvoker;
import rmi.communication.ServiceInvocationHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class RemoteObjectRef implements Serializable {
	private String IP_adr;
	private int Port;
	private int Obj_Key;
	private String Remote_Interface_Name;

	public String getIP_adr() {
		return IP_adr;
	}

	public void setIP_adr(String IP_adr) {
		this.IP_adr = IP_adr;
	}

	public int getPort() {
		return Port;
	}

	public void setPort(int port) {
		Port = port;
	}

	public int getObj_Key() {
		return Obj_Key;
	}

	public void setObj_Key(int obj_Key) {
		Obj_Key = obj_Key;
	}

	public String getRemote_Interface_Name() {
		return Remote_Interface_Name;
	}

	public void setRemote_Interface_Name(String remote_Interface_Name) {
		Remote_Interface_Name = remote_Interface_Name;
	}



	public RemoteObjectRef(String ip, int port, int obj_key, String riname) {
		IP_adr = ip;
		Port = port;
		Obj_Key = obj_key;
		Remote_Interface_Name = riname;
	}

	// this method is important, since it is a stub creator.
	//
	public Object localise() {
		// Implement this as you like: essentially you should
		// create a new stub object and returns it.
		// Assume the stub class has the name e.g.
		//
		// Remote_Interface_Name + "_stub".
		//
		// Then you can create a new stub as follows:
		//
		// Class c = Class.forName(Remote_Interface_Name + "_stub");
		// Object o = c.newinstance()
		//
		// For this to work, your stub should have a constructor without
		// arguments.
		// You know what it does when it is called: it gives communication
		// module
		// all what it got (use CM's static methods), including its method name,
		// arguments etc., in a marshalled form, and CM (yourRMI) sends it out
		// to
		// another place.
		// Here let it return null.
		try {
			Class inter = Class.forName("Service." + Remote_Interface_Name);
			InvocationHandler serviceHandler = new ServiceInvocationHandler(this);

			Object stub = Proxy.newProxyInstance(inter.getClassLoader(), new Class[]{inter}, serviceHandler);
			return stub;
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return null;
	}
}