package org.genius.conquerors.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;

import p.Packet;
import p.Packet18;

public class ClientHandler extends Thread {
	private final Socket s;
	private final InputStream rawIn;
	private final OutputStream rawOut;
	private GeniusOutputStream out;
	private GeniusInputStream in;
	protected String username;
	protected String spatialId;
	public int money=300;
	protected final ArrayList<String> ally=new ArrayList();//list of allies (spatial ID)
	public ClientHandler(Socket s) throws IOException {
		this.s=s;
		this.rawIn=s.getInputStream();
		this.rawOut=s.getOutputStream();
	}
	//Packet1397966893
	//Packet1397966893
	private void init() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.in=new GeniusInputStream(rawIn);
		this.out=new GeniusOutputStream(rawOut);
		PushbackInputStream check=new PushbackInputStream(rawIn);
		while (check.available()!=0) {
			int pid=in.readInt();//packed id
			Packet generic=Packet.getPacket(pid, in, out);
			generic.read();
			PacketHandler ph=new PacketHandler(generic,this);
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
		} finally {
			try {
				for (int i=0;i<Main.playerList.length;i++) {
					if (Main.playerList[i].equals(this.username)) {
						Main.playerList[i]=null;
					}
				}
				String[] players=new String[Main.playerList.length-1];
				for (int i=0;i<Main.playerList.length;i++) {
					if (Main.playerList[i]!=null) {
						players[i]=Main.playerList[i];
					}
				}
				Main.playerCount--;
				Main.playerList=players;
				Packet18 chat=new Packet18(getIn(),getOut());
				chat.setSender("Server");
				chat.setAlly(false);
				chat.setMessage(username+" unexpectdly disconnected");
				Main.broadcastPacket(chat);
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendPacket(Packet p) throws IOException {
		p.write(this.out);
	}
	
	public GeniusOutputStream getOut() {
		return this.out;
	}
	
	public GeniusInputStream getIn() {
		return this.in;
	}
}
