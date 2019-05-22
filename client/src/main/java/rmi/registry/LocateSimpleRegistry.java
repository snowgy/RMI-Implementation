package rmi.registry;

import java.net.*;
import java.io.*;

public class LocateSimpleRegistry {
	// this is the SOLE static method.
	// you use it as: registry.LocateSimpleRegistry.getRegistry(123.123.123.123, 2048)
	// and it returns null if there is none, else it returns the registry.
	// actually the registry is just a pair of host IP and port.
	// inefficient? well you can change it as you like.
	// for the rest, you can see registry.SimpleRegistry.java.
	public static SimpleRegistry getRegistry(String host, int port) {
		// open socket.
		Socket soc = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			soc = new Socket(host, port);
			// get TCP streams and wrap them.
			in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			out = new PrintWriter(soc.getOutputStream(), true);

			// ask.
			out.println("who are you?");

			// gets answer.
			if ((in.readLine()).equals("I am a simple registry.")) {
				return new SimpleRegistry(host, port);
			} else {

				System.out.println("somebody is there but not a  registry!");
				return null;
			}
		} catch (Exception e) {
			System.out.println("nobody is there!" + e.toString());
			e.printStackTrace();
			return null;
		} finally {
			if(soc!=null) {
				try {
					soc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out!=null) {
				out.close();
			}
		}
	}
}
