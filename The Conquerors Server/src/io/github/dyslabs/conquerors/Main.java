package io.github.dyslabs.conquerors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import io.github.dyslabs.conquerors.window.Window;
import p.Packet;

public class Main {
	public static final int PROTOCOL = 0;
	public static final int MAX_PLAYERS = 16;
	private static final Group ALL = new Group();
	public static final Random RANDOM = new Random(System.nanoTime());
	public static final Date START = new Date();
	public static final String SERVER_SPATIALID = Main.getID("server");
	public static final Logger out = Logger.getLogger(Main.class.getName());

	public static void broadcast(final Packet p) {
		Main.ALL.broadcast(p);
	}

	public static void broadcastPlayerList() throws NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		final String[] playerNames = Main.playerList();
		Main.ALL.broadcast(PacketBus.craftPacket(13, "playerNames", playerNames));
	}

	public static void chat(final String sender, final boolean ally, final String message)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Main.broadcast(PacketBus.craftPacket(18, "sender", sender, "ally", ally, "message", message));
	}

	public static String chatMsg(final String snd, final String msg) {
		final Date now = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("h:m");
		return "[" + sdf.format(now) + "] " + snd + ": " + msg;
	}

	public static void checkModel(final String modelUri)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Main.ALL.broadcast(PacketBus.craftPacket(6, "modelName", modelUri));
	}

	public static void clientDisconnect(final Client c)
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

	public static Packet encodeWindowAsPacket(final Window w, final Client c)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		return PacketBus.craftPacket(15, "slots", w.slotsText(c));
	}

	public static String getID() {
		return Main.getID("undefinedobject");
	}

	public static String getID(final String object) {
		final long id = System.nanoTime() - Main.RANDOM.nextLong();
		return object + "[" + id + "]";
	}

	public static byte[] getModel(final String modelUri) {
		return new byte[0];// TODO: model support
	}

	public static Client getPlayer(final String username) {
		final Client[] c = new Client[] { null };
		Main.ALL.stream().forEach(pr -> {
			final Client client = (Client) pr;
			if (username.equals(client.PlayerData.username)) {
				c[0] = client;
			}
		});
		return c[0];
	}

	public static void main(final String[] args) throws IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
			ClassNotFoundException, NoSuchFieldException {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				Main.out.info("Shutting down");
			}
		});
		final Date d = new Date();
		final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHms");
		final SimpleDateFormat df2 = new SimpleDateFormat("d M yyyy h:m:s a");
		final FileHandler fileTxt = new FileHandler(df.format(d) + ".txt");
		fileTxt.setFormatter(new Formatter() {
			@Override
			public String format(final LogRecord record) {
				final String timestamp = df2.format(new Date());
				return timestamp + "\r\n" + record.getSourceClassName() + " " + record.getSourceMethodName() + "\r\n"
						+ record.getLevel() + ": " + record.getMessage() + "\r\n\r\n";
			}
		});
		Main.out.addHandler(fileTxt);
		Main.out.info("Logfile: " + df.format(d) + ".txt");
		Main.out.info("Server Spatial ID: " + Main.SERVER_SPATIALID);
		final Packet p = new p.Packet1();
		p.set("spatialID", Main.SERVER_SPATIALID);
		p.getFields();
		final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter server port:");
		final int port = Integer.parseInt(in.readLine());
		final ServerSocket servlet = new ServerSocket(port);
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

		Main.START.setTime(System.currentTimeMillis());
		while (true) {
			final Client c = new Client(servlet.accept());
			new Thread(Main.getID("ClientThread")) {
				@Override
				public void run() {
					try {
						c.run();
						Main.out.info("Client successfully connected " + c);
					} catch (final InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final SocketException e) {// the client has stopped
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
					} catch (final IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	public static String[] playerList() {
		final String[] players = new String[Main.ALL.size()];
		final int[] i = new int[] { 0 };
		Main.ALL.stream().forEach(c -> {
			players[i[0]] = ((Client) c).PlayerData.username;
			i[0]++;
		});
		return players;
	}

	public static int players() {
		return Main.ALL.size();
	}

	public static void registerPlayer(final Packet p, final Client c)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		/*
		 * Assuming login success: Add player to ALL group Set a spatialID for
		 * the player Send login success packet Broadcast PlayerList Packet
		 * Broadcast CheckModel Packet Broadcast SpawnEntity (As Appropriate)
		 * Broadcast TranslateEntity, ScaleEntity, and RotateEntity
		 */
		if (Main.ALL.size() >= Main.MAX_PLAYERS) {
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
	public static String spawnEntity(final String spatialID, final float xscale, final float yscale, final float zscale,
			final float xpos, final float ypos, final float zpos)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		// String spatialID=Main.getID(spatialCode);
		final String spatialCode = spatialID.split("\\[")[0];
		final String material = "defulat material"; // TODO
		String model = "default model";
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
}