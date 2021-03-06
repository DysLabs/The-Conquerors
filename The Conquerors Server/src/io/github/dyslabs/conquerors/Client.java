package io.github.dyslabs.conquerors;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import p.Packet;

public class Client extends PacketReceiver {
	public class PlayerData {
		public final Group alliance = new Group();
		public String username;
		public int money = 300;
		public String spatialID;
	}

	// private final PushbackInputStream pbi;
	// private final InputStreamReader in_reader;
	private final PacketOutputStream out;
	// private final PacketInputStream in;
	// private final Socket s;
	private Packet job;
	public final PlayerData PlayerData = new PlayerData();

	public Client(final String ip, final OutputStream out) throws IOException {
		super(ip);
		this.out = new PacketOutputStream(out);
	}

	@Deprecated
	public boolean canPoll() throws IOException {
		return true;
	}

	public Packet job() {
		return this.job;
	}

	public void poll(final Packet p)
			throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException,
			InvocationTargetException, InstantiationException, NoSuchFieldException, IOException, URISyntaxException {
		this.job = p;
		if (p.getPacketID() == 0) {// Login
			this.PlayerData.username = p.getField("username");
			Main.registerClient(this);
		} else if (p.getPacketID() == 4) {// Request Model
			this.sendPacket(Packet.c(3, p.getField("model"), Main.getModel(p.getField("model"))));
		} else if (p.getPacketID() == 10) {// Player Position
			final float x = p.getField("x"), y = p.getField("y"), z = p.getField("z");
			Main.broadcast(Packet.c(7, this.PlayerData.spatialID, x, y, z));
		} else if (p.getPacketID() == 11) {// Player Look
			final float x = p.getField("x"), y = p.getField("y"), z = p.getField("z");
			Main.broadcast(Packet.c(9, this.PlayerData.spatialID, x, y, z));
		} else if (p.getPacketID() == 14) {// Request Window
			final String spatialID = p.getField("spatialID");
			this.sendPacket(Window.lookup(spatialID).asPacket(this, spatialID));
		} else if (p.getPacketID() == 16) { // Disconnect
			Main.disconnectClient(this);
		} else if (p.getPacketID() == 17) { // Chat
			final boolean ally = p.getField("ally");
			final String message = p.getField("message");
			final Packet chat = Packet.c(18, this.PlayerData.username, ally, message);
			if (ally) {
				this.PlayerData.alliance.broadcast(chat);
				this.sendPacket(chat);
			} else {
				Main.broadcast(chat);
			}
		} else if (p.getPacketID() == 19) { // Select Window Slot
			final String spatialID = p.getField("spatialID");
			Window.lookup(spatialID).slots(this)[p.<Byte>getField("slot")].act(this);
			;
		}

		else {
			Main.out.warning("Non-existant packet#" + p.getPacketID());
		}
	}

	@Override
	public String toString() {
		return "Client[" + this.PlayerData.username + "]";
	}
}
