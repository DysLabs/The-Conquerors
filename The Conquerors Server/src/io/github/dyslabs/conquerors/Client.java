package io.github.dyslabs.conquerors;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import p.Packet;

public class Client extends PacketReceiver {
	public class PlayerData {
		public final Group alliance = new Group();
		public String username;
		public int money = 300;
		public String spatialID;
	}

	private PushbackInputStream pbi;
	private InputStreamReader in_reader;
	private final PacketOutputStream out;
	private final PacketInputStream in;
	private final Socket s;
	private Packet job;
	public final PlayerData PlayerData = new PlayerData();

	private Client(final InputStream in, final OutputStream out, final Socket s,SocketChannel client) throws IOException {
		super(out,client);
		this.pbi=new PushbackInputStream(in);
		this.in_reader=new InputStreamReader(pbi);
		this.out = new PacketOutputStream(out);
		this.in = new PacketInputStream(pbi);
		this.s = s;
	}

	public Client(final Socket s,SocketChannel client) throws IOException {
		this(s.getInputStream(), s.getOutputStream(), s, client);
	}

	public boolean canPoll() throws IOException {
		return true;
	}
	
	public void poll() {
		
	}
	
	public String toString() {
		return "Client["+s.getRemoteSocketAddress()+" \""+PlayerData.username+"\"]";
	}
	
	public void poll(long s) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException, IOException, InterruptedException {
		Main.out.info("poll="+s);
		Main.out.info("pbi.available "+pbi.available());
		Main.out.info("in.available "+in.available());
		if (!in_reader.ready()) {
			Main.out.info("There appears to be no data to read");
			return;
		}
		int b=pbi.read();
		Main.out.info(b+"");
		if (b==-1) {
			Main.out.info("No data can be read");
			return;
		}
		pbi.unread(b);
		Main.out.info("Waing for packet");
		Packet p=in.readPacket();
		Main.out.info("Received "+p);
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
			String spatialID=p.getField("spatialID");
			sendPacket(Window.lookup(spatialID).asPacket(this, spatialID));
		} else if (p.getPacketID()==16) { // Disconnect
			Main.disconnectClient(this);
		} else if (p.getPacketID()==17) { // Chat
			boolean ally=p.getField("ally");
			String message=p.getField("message");
			Packet chat=Packet.c(18, this.PlayerData.username, ally, message);
			if (ally) {
				this.PlayerData.alliance.broadcast(chat);
			} else {
				Main.broadcast(chat);
			}
		} else if (p.getPacketID()==19) { // Select Window Slot
			//TODO window
		}
		
		else {
			Main.out.warning("Non-existant packet#"+p.getPacketID());
		}
	}
	
	public void poll(Packet p) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException, InstantiationException, NoSuchFieldException, IOException {
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
			String spatialID=p.getField("spatialID");
			sendPacket(Window.lookup(spatialID).asPacket(this, spatialID));
		} else if (p.getPacketID()==16) { // Disconnect
			Main.disconnectClient(this);
		} else if (p.getPacketID()==17) { // Chat
			boolean ally=p.getField("ally");
			String message=p.getField("message");
			Packet chat=Packet.c(18, this.PlayerData.username, ally, message);
			if (ally) {
				this.PlayerData.alliance.broadcast(chat);
			} else {
				Main.broadcast(chat);
			}
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
