package io.github.dyslabs.conquerors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import p.Packet;

public class Client extends PacketReceiver {
	private final PacketOutputStream out;
	private final PacketInputStream in;
	private final Socket s;
	public final PlayerData PlayerData=new PlayerData();
	private boolean running=true;
	public Client(Socket s) throws IOException {
		this(s.getInputStream(),s.getOutputStream(),s);
	}
	
	/**
	 * If this returns false, the client should be ungrouped immediatally
	 * @return
	 */
	public boolean isValid() {
		return running;
	}
	
	public synchronized void valid(boolean b) {
		this.running=b;
	}
	
	private Client(InputStream in,OutputStream out,Socket s) {
		super(out);
		this.out=new PacketOutputStream(out);
		this.in=new PacketInputStream(in);
		this.s=s;
	}
	
	private void poll() throws InterruptedException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException, IOException {
		Packet p=in.readPacket();
		new PacketHandler(p);
	}
	
	public void run() throws InterruptedException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException, IOException {
		//System.out.println("{"+Thread.currentThread().getName()+"} Now polling "+s.getInetAddress()+" for data");
		while (running) {
			poll();
		}
	}
	
	private class PacketHandler {
		private final Packet p;
		public PacketHandler(Packet p) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InstantiationException, InvocationTargetException, NoSuchFieldException {
			this.p=p;
			switch (p.getPacketID()) {
			case 0:
				handle0();
				break;
			}
		}
		
		private void handle0() throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InstantiationException, InvocationTargetException, NoSuchFieldException {
			String username=p.getField("username");
			int protocolVersion=p.getField("protocolVersion");
			Client.this.PlayerData.username=username;
			if (protocolVersion!=Main.PROTOCOL) {//different protocol oops
				Packet loginFailure=PacketBus.craftPacket(2, new Object[]{
						"reason","You are running a different version than us. Upgrade (or downgrade) to protocol version "+Main.PROTOCOL
				});
				Client.this.sendPacket(loginFailure);
				Main.clientDisconnect(Client.this);
			} else if (Main.players()>=16) {
				Packet loginFailure=PacketBus.craftPacket(2, new Object[]{
						"reason","Server is already full"
				});
				Client.this.sendPacket(loginFailure);
				Main.clientDisconnect(Client.this);
			}
			else { //successful login
				Client.this.PlayerData.spatialID=Main.getID("entity[player]");
				Packet loginSuccess=PacketBus.craftPacket(1, new Object[]{
						"playerList",Main.playerList()
				});
				Client.this.sendPacket(loginSuccess);
				Main.registerPlayer(Client.this);
			}
		}
	}
	
	public class PlayerData {
		public final Group alliance=new Group();
		public String username;
		public int money=300;
		public String spatialID;
	}
}
