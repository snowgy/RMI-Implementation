package rmi.registry;

import rmi.communication.RemoteServiceInvoker;

import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// This is a simple registry server.
// The system does not do any error checking or bound checking.
// It uses the ROR as specified in the coursework sheet.

// protocol:
//   (1) lookup  --> returns ROR.
//   (2) rebind --> binds ROR.
//   (3) whoareyou --> I am simple registry etc.
// it is used through registry.SimpleRegistry and registry.LocateSimpleRegistry.

public class SimpleRegistryServer {
	public static final int port = 2004;
	private static Hashtable<String, RemoteObjectRef> table = new Hashtable<String, RemoteObjectRef>();

	public static void startServer(int port) {
		try(ServerSocket serverSoc = new ServerSocket(port)){
			Executor executor = Executors.newFixedThreadPool(100);
			System.out.println("server socket created at " + port);

			while(true){
				Socket newsoc = serverSoc.accept();

				executor.execute(new Runnable() {
					@Override
					public void run() {
						Socket conn = newsoc;
						try {
							handler(conn);
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try{
								if (conn != null)
									conn.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void handler(Socket newsoc) throws IOException{
		// input/output streams (remember, TCP is bidirectional).
		BufferedReader in = new BufferedReader(new InputStreamReader(newsoc.getInputStream()));
		PrintWriter out = new PrintWriter(newsoc.getOutputStream(), true);

		// get a line. this should be a command:
		// (1) lookup servicename --> ["found", ROR data] or ["not found"]
		// (2) rebound servicename ROR --> ["bound"]
		// (3) who are you? --> I am a simple registry.

		String command = in.readLine();
		// branch: commands are either lookup or rebind.
		if (command.equals("lookup")) {
			System.out.println("it is lookup request.");

			String serviceName = in.readLine();

			System.out.println("The service name is " + serviceName + ".");

			// tests if it is in the table.
			// if it is gets it.
			if (table.containsKey(serviceName)) {
				System.out.println("the service found.");

				RemoteObjectRef ror = (RemoteObjectRef) table.get(serviceName);

				System.out.println("ROR is " + ror.getIP_adr() + "," + ror.getPort() + "," + ror.getObj_Key() + ","
						+ ror.getRemote_Interface_Name() + ".");

				out.println("found");
				out.println(ror.getIP_adr());
				out.println(Integer.toString(ror.getPort()));
				out.println(Integer.toString(ror.getObj_Key()));
				out.println(ror.getRemote_Interface_Name());

				System.out.println("ROR was sent.\n");
			} else {
				System.out.println("the service not found.\n");

				out.println("not found");
			}
		} else if (command.equals("rebind")) {
			System.out.println("it is rebind request.");

			// again no error check.
			String serviceName = in.readLine();

			System.out.println("the service name is " + serviceName + ".");

			// get ROR data.
			// I do not serialise.
			// Go elementary, that is my slogan.

			System.out.println("I got the following ror:");

			String IP_adr = in.readLine();
			int Port = Integer.parseInt(in.readLine());
			int Obj_Key = Integer.parseInt(in.readLine());
			String Remote_Interface_Name = in.readLine();

			System.out.println("IP address: " + IP_adr);
			System.out.println("port num:" + Port);
			System.out.println("object key:" + Obj_Key);
			System.out.println("Interface Name:" + Remote_Interface_Name);

			// make ROR.
			RemoteObjectRef ror = new RemoteObjectRef(IP_adr, Port, Obj_Key, Remote_Interface_Name);
			synchronized (table) {
				// put it in the table.
				table.remove(serviceName);
				Object res = table.put(serviceName, ror);
			}
			System.out.println("ROR is put in the table.\n");

			// ack.
			out.println("bound");
		} else if (command.equals("who are you?")) {
			out.println("I am a simple registry.");
			System.out.println("I was asked who I am, so I answered.\n");
		} else {
			System.out.println("I got an imcomprehensive message.\n");
		}
	}

	public static void main(String args[]) throws IOException {

		SimpleRegistryServer.startServer(port);
	}
}

