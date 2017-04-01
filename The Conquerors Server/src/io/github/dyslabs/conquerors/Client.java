package io.github.dyslabs.conquerors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import p.Packet;

public class Client extends PacketReceiver {
	public class PlayerData {
		public final Group alliance = new Group();
		public String username;
		public int money = 300;
		public String spatialID;
	}

	private final PacketOutputStream out;
	private final PacketInputStream in;
	private final Socket s;
	private Packet job;
	public final PlayerData PlayerData = new PlayerData();

	private Client(final InputStream in, final OutputStream out, final Socket s) throws IOException {
		super(s);
		this.out = new PacketOutputStream(out);
		this.in = new PacketInputStream(in);
		this.s = s;
	}

	public Client(final Socket s) throws IOException {
		this(s.getInputStream(), s.getOutputStream(), s);
	}

	
	public void poll() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException, IOException {
		Packet p=in.readPacket();
		this.job=p;
		if (p.getPacketID()==0) {// Login
			PlayerData.username=p.getField("username");
			Main.registerClient(this);
		} else if (p.getPacketID()==4) {// Request Window
			sendPacket(Packet.c(3, Main.getModel(p.getField("modelName"))));
		} else if (p.getPacketID()==10) {// Player Position
			float x=p.getField("x"),y=p.getField("y"),z=p.getField("z");
			Main.broadcast(Packet.c(7, PlayerData.spatialID,x,y,z));
		} else if (p.getPacketID()==11) {// Player Look
			float x=p.getField("x"),y=p.getField("y"),z=p.getField("z");
			Main.broadcast(Packet.c(9, PlayerData.spatialID,x,y,z));
		} else if (p.getPacketID()==14) {//Request Window
			//TODO window
		} else if (p.getPacketID()==16) { // Disconnect
			//TODO disconnect
		} else if (p.getPacketID()==17) { // Chat
			//TODO chat
		} else if (p.getPacketID()==19) { // Select Window Slot
			//TODO window
		}
		
		else {
			Main.out.warning("Non-existant packet#"+p.getPacketID());
		}
	}
	
	public Packet job() {
		return job;
	}
}
