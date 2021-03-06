package io.github.dyslabs.conquerors;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import io.github.dyslabs.conquerors.net.ProtocolServer;
import p.Packet;

public class Main {
	public static final int PORT = 22;
	public static final int PROTOCOL = 0;
	public static final int MAX_PLAYERS = 16;
	private static final Group ALL = new Group();
	private static Map<SocketChannel, List> dataMap;
	private static Map<SocketChannel, Client> clientMap;
	public static final Random RANDOM = new Random(System.nanoTime());
	public static final Date START = new Date();
	public static final String SERVER_SPATIALID = Main.getID("server");
	public static final Logger out = Logger.getLogger(Main.class.getName());
	public static Logger pout = Logger.getLogger(Packet.class.getName());
	public static ProtocolServer server = new ProtocolServer();

	public static void broadcast(final Packet p) throws IllegalArgumentException, IllegalAccessException,
			NoSuchMethodException, SecurityException, InvocationTargetException, IOException {
		final Iterator<PacketReceiver> iter = Main.ALL.iterator();
		while (iter.hasNext()) {
			iter.next().sendPacket(p);
		}
	}

	public static void disconnectClient(final Client c)
			throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException,
			InvocationTargetException, InstantiationException, NoSuchFieldException, IOException {
		c.valid(false);
		Main.broadcast(Packet.c(18, "Server", false, c.PlayerData.username + " has left the game"));
		Main.broadcast(Packet.c(12, c.PlayerData.spatialID));
	}

	public static String getID() {
		return Main.getID("undefinedobject");
	}

	public static String getID(final String object) {
		final long id = System.nanoTime() - Main.RANDOM.nextLong();
		return object + "[" + id + "]";
	}

	public static byte[] getModel(final String uri) throws URISyntaxException, IOException {
		final File model = Main.getPath(uri);
		if (!model.exists()) {
			Main.out.warning(uri+" model does not exist");
			return new byte[0];
		}
		final FileInputStream in = new FileInputStream(model);
		final ByteArrayOutputStream bo = new ByteArrayOutputStream();
		int b;
		while ((b = in.read()) != -1) {
			bo.write(b);
		}
		in.close();
		return bo.toByteArray();
	}

	public static File getPath(final String furi) throws URISyntaxException, IOException {
		final URI uri = Main.class.getResource("assets/" + furi).toURI();
		Path p;
		if (uri.getScheme().equals("jar")) {
			final FileSystem fs = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
			p = fs.getPath(uri.toString());
		} else {
			return new File(uri.getPath());
		}
		return p.toFile();
	}

	public static Client getPlayerByUsername(final String username) {
		final Iterator<PacketReceiver> iter = Main.ALL.iterator();
		while (iter.hasNext()) {
			final Client c = (Client) iter.next();
			if (c.PlayerData.username.equals(username)) {
				return c;
			}
		}
		return null;
	}

	public static void main(final String[] args) throws SecurityException, IOException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, NoSuchFieldException, URISyntaxException, InterruptedException {
		/**
		 * Setup logger
		 */
		final Date d = new Date();
		final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHms");
		final SimpleDateFormat df2 = new SimpleDateFormat("dd MMMMM yyyy h:m:s a");
		final Formatter txtFormat = new Formatter() {
			@Override
			public String format(final LogRecord record) {
				final String timestamp = df2.format(new Date());
				if (record.getThrown() == null) {
					return timestamp + "\r\n" + record.getSourceClassName() + " " + record.getSourceMethodName()
							+ "\r\n" + record.getLevel() + ": " + record.getMessage() + "\r\n\r\n";
				} else {
					final Throwable t = record.getThrown();
					final StringBuilder sb = new StringBuilder(record.getMessage() + "\n");
					final StackTraceElement[] stackTrace = t.getStackTrace();
					for (final StackTraceElement element : stackTrace) {
						sb.append("\t" + element).append("\r\n");
					}
					if (t.getCause() != null) {
						sb.append("Caused by: " + t.getCause().toString()).append("\r\n");
						final StackTraceElement[] stackTrace1 = t.getCause().getStackTrace();
						for (final StackTraceElement element : stackTrace1) {
							sb.append("\t" + element).append("\r\n");
						}
					}
					return timestamp + "\r\n" + record.getSourceClassName() + " " + record.getSourceMethodName()
							+ " \r\n" + t.toString() + "\r\n" + sb.toString() + "\r\n\r\n";
				}
			}
		};
		final FileHandler fileTxt = new FileHandler(df.format(d) + ".txt");
		fileTxt.setFormatter(txtFormat);
		final FileHandler packetTxt = new FileHandler(df.format(d) + "-packets.txt");
		packetTxt.setFormatter(new Formatter() {
			@Override
			public String format(final LogRecord log) {
				final String timestamp = df2.format(new Date());
				final StringBuilder sb = new StringBuilder(timestamp).append("\r\n");
				if (log.getSourceMethodName().toLowerCase().contains("read")) {// C->S
					sb.append("Client to Server: " + log.getMessage());
				} else {// S->C
					sb.append("Server to Client: " + log.getMessage());
				}
				return sb.append("\r\n\r\n").toString();
			}
		});
		Main.pout.addHandler(packetTxt);
		Main.out.addHandler(fileTxt);
		Main.out.info("Logfile: " + df.format(d) + ".txt");
		Main.out.info("Packet Logfile: " + df.format(d) + "-packets.txt");
		Main.out.info("Server Spatial ID: " + Main.SERVER_SPATIALID);
		Thread.setDefaultUncaughtExceptionHandler(
				(t, e) -> Main.out.log(Level.SEVERE, "An uncaught exception was thrown", e));
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (final Handler h : Main.out.getHandlers()) {
					h.close();
				}
				for (final Handler h : Main.pout.getHandlers()) {
					h.close();
				}
			}
		});
		Main.getModel("player.blend");
		Window.declare("alliance", Window.getAllianceWindow());
		final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		/*
		 * new Thread("Socket Acceptor"){ public void run() { while (true) {
		 * Client client=null; try { Socket s=servlet.accept(); client=new
		 * Client(s); client.valid(false); ALL.group(client); client.poll(5000);
		 * } catch (IOException | InstantiationException |
		 * IllegalAccessException | IllegalArgumentException |
		 * InvocationTargetException | NoSuchMethodException | SecurityException
		 * | ClassNotFoundException | NoSuchFieldException |
		 * InterruptedException e) { Main.out.log(Level.WARNING,
		 * "An error occured while accepting "+client, e); } } } }.start();
		 *
		 * while (true) { Iterator<PacketReceiver> iter=ALL.iterator(); // if
		 * (!iter.hasNext()) Main.out.warning("No items in iterator");; while
		 * (iter.hasNext()) { Client c=(Client)iter.next(); try { if
		 * (c.canPoll()) { c.poll(5000); } } catch (IOException |
		 * InterruptedException e) { Main.disconnectClient(c);
		 * Main.out.log(Level.SEVERE, c+" is broken", e); } } }
		 * //Main.out.severe("Exited main loop");
		 */
		Main.dataMap = new HashMap<>();
		Main.clientMap = new HashMap<>();
		final Selector selector = Selector.open();
		final ServerSocketChannel server = ServerSocketChannel.open();
		server.configureBlocking(false);
		// server.socket().bind(new InetSocketAddress("localhost",
		// Integer.parseInt(stdin.readLine())));
		final int port = Integer.parseInt(stdin.readLine());
		Main.server.run(port);
		Main.out.info("Started server on port " + port);
		// server.register(selector, SelectionKey.OP_ACCEPT);

		while (true) {
			Main.update();
		}

	}

	public static String[] playerList() {
		final String[] players = new String[Main.players()];
		int i = 0;
		final Iterator<PacketReceiver> iter = Main.ALL.iterator();
		Main.out.info(Main.ALL.size() + " players reported connected");
		while (iter.hasNext()) {
			players[i] = ((Client) iter.next()).PlayerData.username;
			i++;
		}
		// Main.out.info(players.length + " players");
		return players;
	}

	public static int players() {
		return Main.ALL.size();
	}

	public static void registerClient(final Client c)
			throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException,
			InvocationTargetException, InstantiationException, NoSuchFieldException, IOException {
		Main.ALL.group(c);
		final Packet p = c.job();
		if (p.<Integer>getField("protocolVersion") != Main.PROTOCOL) {
			c.sendPacket(Packet.craftPacket(2, "reason", "You are running a different version than us."));
		} else if ((Main.players() + 1) > Main.MAX_PLAYERS) {
			c.sendPacket(Packet.craftPacket(2, "reason", "This server is full."));
		} else if (Arrays.asList(Main.playerList()).contains(c.PlayerData.username)) {
			c.sendPacket(Packet.craftPacket(2, "reason", "A player with that name is already connected"));
		} else {
			c.valid(true);
			final String spatialID = Main.getID("player");
			c.PlayerData.spatialID = spatialID;
			c.sendPacket(Packet.c(1, spatialID));
			Main.broadcast(Packet.c(13, new Object[] { Main.playerList() }));
			Main.broadcast(Packet.c(6, "player.blend"));
			Main.broadcast(Packet.c(5, "player.blend" + "", "material", spatialID));
			/*
			 * Packet 8 Scale Entity Packet 7 Translate Entity
			 */
			Main.out.info(c.PlayerData.username + " is now connected");
			Main.broadcast(Packet.c(18, "Server", false, c.PlayerData.username + " has joined the game"));
		}
	}

	public static void update() {

	}
}
