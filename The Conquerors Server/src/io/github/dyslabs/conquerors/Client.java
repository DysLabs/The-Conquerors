package io.github.dyslabs.conquerors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import io.github.dyslabs.conquerors.window.Window;
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
	public final PlayerData PlayerData = new PlayerData();

	private boolean running = true;

	private Client(final InputStream in, final OutputStream out, final Socket s) {
		super(out);
		this.out = new PacketOutputStream(out);
		this.in = new PacketInputStream(in);
		this.s = s;
	}

	public Client(final Socket s) throws IOException {
		this(s.getInputStream(), s.getOutputStream(), s);
	}

	/**
	 * If this returns false, the client should be ungrouped immediatally
	 *
	 * @return
	 */
	public boolean isValid() {
		return this.running;
	}

	private void poll() throws InterruptedException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
			ClassNotFoundException, NoSuchFieldException, IOException {
		final Packet p = this.in.readPacket();
		switch (p.getPacketID()) {
		case 0:// Login
			this.PlayerData.username = p.getField("username");
			Main.registerPlayer(p, this);
			break;
		case 4:// Request Model
			final String modelUri = p.getField("modelName");
			final byte[] model = Main.getModel(modelUri);
			this.sendPacket(PacketBus.craftPacket(3, "modelName", modelUri, "model", model));
			break;
		case 10:// Player Position
			final float x = p.getField("x"), y = p.getField("y"), z = p.getField("z");
			Main.broadcast(PacketBus.craftPacket(7, "spatialID", this.PlayerData.spatialID, "x", x, "y", y, "z", z));
			break;
		case 11:// Player Look
			final float x1 = p.getField("x"), y1 = p.getField("y"), z1 = p.getField("z");
			Main.broadcast(PacketBus.craftPacket(9, "spatialID", this.PlayerData.spatialID, "x", x1, "y", y1, "z", z1));
			break;
		case 14:// Request Window
			this.sendPacket(Main.encodeWindowAsPacket(Window.BUY_WINDOW, this));
			break;// TODO: window system
		case 16:// Disconnect
			Main.clientDisconnect(this);
			break;
		case 17:// Chat
			final String sender = p.getField("sender");
			final boolean ally = p.getField("ally");
			final String message = p.getField("message");
			String msg;
			if (ally) {
				msg = Main.chatMsg("(ALLY) " + sender, message);
			} else {
				msg = Main.chatMsg(sender, message);
			}
			Main.chat(sender, ally, msg);
			break;
		case 19:// Select Window Slot
			break;
		case 20:// Move Units
			break;
		}
	}

	public void run() throws InterruptedException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
			ClassNotFoundException, NoSuchFieldException, IOException {
		// System.out.println("{"+Thread.currentThread().getName()+"} Now
		// polling "+s.getInetAddress()+" for data");
		while (this.running) {
			this.poll();
		}
	}

	public synchronized void valid(final boolean b) {
		this.running = b;
	}
}
