package org.genius.conquerors.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import p.Packet;

public class ClientHandler extends Thread {
	private final Socket s;
	private final InputStream rawIn;
	private final OutputStream rawOut;
	public ClientHandler(Socket s) throws IOException {
		this.s=s;
		this.rawIn=s.getInputStream();
		this.rawOut=s.getOutputStream();
	}
	
	private void init() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		GeniusInputStream in=new GeniusInputStream(rawIn);
		GeniusOutputStream out=new GeniusOutputStream(rawOut);
		PushbackInputStream check=new PushbackInputStream(rawIn);
		while (check.available()!=0) {
			int pid=in.readInt();//packed id
			Packet generic=Packet.getPacket(pid, in, out);
		}
	}
	
	public void run() {
		try {
			init();
		} catch (ClassNotFoundException e) {
			System.out.println("received unknown packet / this client is broken");
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
