package io.github.dyslabs.conquerors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import io.github.dyslabs.conquerors.window.Window;
import p.Packet;
import p.Packet0;

public class Main {
	public static final int PROTOCOL = 0;
	public static final int MAX_PLAYERS = 16;
	private static final Group ALL = new Group();
	public static final Random RANDOM = new Random(System.nanoTime());
	public static final Date START = new Date();
	public static final String SERVER_SPATIALID = Main.getID("server");
	public static final Logger out = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
			ClassNotFoundException, NoSuchFieldException {
		System.out.println("Server Spatial ID: " + SERVER_SPATIALID);
		// System.out.println("Spawn Server:
		// "+Main.spawnEntity(SERVER_SPATIALID, 0, 0, 0, 0, 0, 0));
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter server port:");
		int port = Integer.parseInt(in.readLine());
		ServerSocket servlet = new ServerSocket(port);
		Main.out.info("Started server on port " + port + " successfully");
		new Thread() {
			@Override
			public void run() {
				Main.out.info("Constant polling of PacketBus running");
				while (true) {
					PacketBus.bus();
				}
			}
		}.start();

		START.setTime(System.currentTimeMillis());
		while (true) {
			Client c = new Client(servlet.accept());
			new Thread(getID("ClientThread")) {
				public void run() {
					try {
						c.run();
						Main.out.info("Client successfully connected " + c);
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
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SocketException e) {// the client has stopped
													// responding
						// or other network issue
						// System.out.println(Thread.currentThread().getName()+"}
						// stopped running");
						try {
							Main.clientDisconnect(c);
						} catch (NoSuchMethodException | SecurityException | InstantiationException
								| IllegalAccessException | IllegalArgumentException | InvocationTargetException
								| NoSuchFieldException e1) {
							Main.out.severe("An error occured while attempting to disconnect client " + c);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	public static String[] playerList() {
		String[] players = new String[Main.ALL.size()];
		int[] i = new int[] { 0 };
		Main.ALL.stream().forEach(c -> {
			players[i[0]] = ((Client) c).PlayerData.username;
			i[0]++;
		});
		return players;
	}

	public static int players() {
		return Main.ALL.size();
	}

	public static String getID(String object) {
		long id = System.nanoTime() - RANDOM.nextLong();
		return object + "[" + id + "]";
	}

	public static String getID() {
		return getID("undefinedobject");
	}

	public static void registerPlayer(Packet p, Client c)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		/*
		 * Assuming login success: Add player to ALL group Set a spatialID for
		 * the player Send login success packet Broadcast PlayerList Packet
		 * Broadcast CheckModel Packet Broadcast SpawnEntity (As Appropriate)
		 * Broadcast TranslateEntity, ScaleEntity, and RotateEntity
		 */
		if (ALL.size() >= Main.MAX_PLAYERS) {
			c.sendPacket(PacketBus.craftPacket(2, new Object[] { "reason", "The server is full, sorry" }));
		} else if (p.<Integer>getField("protocolVersion") != Main.PROTOCOL) {
			c.sendPacket(PacketBus.craftPacket(2, "reason", "You are running a different version than the server"));
		} else {// login success
			c.PlayerData.spatialID = Main.getID("player");
			// login success packet
			c.sendPacket(PacketBus.craftPacket(1, "spatialID", c.PlayerData.spatialID));
			// player list packet
			Main.ALL.group(c);
			Main.broadcastPlayerList();
			// spawn entity
			Main.spawnEntity(c.PlayerData.spatialID, 0f, 0f, 0f, 0f, 0f, 0f);
			// welcome chat message
			Main.chat("Server", false, c.PlayerData.username + " has joined the game");
			Main.out.info(c.PlayerData.username + " is now connected");
		}
	}

	public static void broadcastPlayerList() throws NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		String[] playerNames = Main.playerList();
		Main.ALL.broadcast(PacketBus.craftPacket(13, "playerNames", playerNames));
	}

	public static void checkModel(String modelUri)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Main.ALL.broadcast(PacketBus.craftPacket(6, "modelName", modelUri));
	}

	/**
	 * Spatial code may be anything from the set of [player]
	 * 
	 * @param spatialID
	 * @param xscale
	 * @param yscale
	 * @param zscale
	 * @throws NoSuchFieldException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static String spawnEntity(String spatialID, float xscale, float yscale, float zscale, float xpos, float ypos,
			float zpos) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		// String spatialID=Main.getID(spatialCode);
		String spatialCode = spatialID.split("\\[")[0];
		String material = "defulat material", model = "default model"; // TODO
																		// material
																		// support
		switch (spatialCode) {
		case "player":
			Window.assignWindow(spatialID, Window.ALLY_WINDOW);
			model = "playe rmodel";
			break;
		}
		// Check Model
		Main.ALL.broadcast(PacketBus.craftPacket(6, "model", model));
		// Spawn Entity Packet
		Main.ALL.broadcast(PacketBus.craftPacket(5, "model", model, "material", material, "spatialID", spatialID));
		// Scale Entity Packet
		Main.ALL.broadcast(PacketBus.craftPacket(8, "spatialID", spatialID, "x", xscale, "y", yscale, "z", zscale));
		// Set Entity Position
		Main.ALL.broadcast(PacketBus.craftPacket(7, "spatialID", spatialID, "x", xpos, "y", ypos, "z", zpos));
		return spatialCode;
	}

	public static byte[] getModel(String modelUri) {
		return new byte[0];// TODO: model support
	}

	public static void clientDisconnect(Client c)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		/**
		 * Ungroup player Update player list Remove entity
		 */
		c.valid(false);
		Main.ALL.ungroup(c);
		Main.broadcastPlayerList();
		Main.broadcast(PacketBus.craftPacket(12, "spatialID", c.PlayerData.spatialID));
	}

	public static void broadcast(Packet p) {
		Main.ALL.broadcast(p);
	}

	public static String chatMsg(String snd, String msg) {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("h:m");
		return "[" + sdf.format(now) + "] " + snd + ": " + msg;
	}

	public static void chat(String sender, boolean ally, String message)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Main.broadcast(PacketBus.craftPacket(18, "sender", sender, "ally", ally, "message", message));
	}

	public static Packet encodeWindowAsPacket(Window w, Client c)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		return PacketBus.craftPacket(15, "slots", w.slotsText(c));
	}

	public static Client getPlayer(String username) {
		Client[] c = new Client[] { null };
		Main.ALL.stream().forEach(pr -> {
			Client client = (Client) pr;
			if (username.equals(client.PlayerData.username)) {
				c[0] = client;
			}
		});
		return c[0];
	}
}