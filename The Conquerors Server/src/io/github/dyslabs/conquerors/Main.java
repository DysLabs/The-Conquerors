package io.github.dyslabs.conquerors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import p.Packet;

public class Main {
	public static final int PORT=22;
	public static final int PROTOCOL = 0;
	public static final int MAX_PLAYERS = 16;
	private static final Group ALL = new Group();
	public static final Random RANDOM = new Random(System.nanoTime());
	public static final Date START = new Date();
	public static final String SERVER_SPATIALID = Main.getID("server");
	public static final Logger out = Logger.getLogger(Main.class.getName());
	public static String getID() {
		return Main.getID("undefinedobject");
	}

	public static String getID(final String object) {
		final long id = System.nanoTime() - Main.RANDOM.nextLong();
		return object + "[" + id + "]";
	}
	
    public static void main(String[]args) throws SecurityException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, NoSuchFieldException {
    	/**
    	 * Setup logger
    	 */
    	final Date d = new Date();
		final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHms");
		final SimpleDateFormat df2 = new SimpleDateFormat("d M yyyy h:m:s a");
		final FileHandler fileTxt = new FileHandler(df.format(d) + ".txt");
		fileTxt.setFormatter(new Formatter() {
			@Override
			public String format(final LogRecord record) {
				final String timestamp = df2.format(new Date());
				if (record.getThrown()==null) {
				return timestamp + "\r\n" + record.getSourceClassName() + " " + record.getSourceMethodName() + "\r\n"
						+ record.getLevel() + ": " + record.getMessage() + "\r\n\r\n";
				} else {
					Throwable t=record.getThrown();
					StringBuilder sb=new StringBuilder();
					StackTraceElement[] stackTrace=t.getStackTrace();
					for (int i=0;i<stackTrace.length;i++) {
						sb.append("\t"+stackTrace[i]).append("\r\n");
					}
					if (t.getCause()!=null) {
						sb.append("Caused by: "+t.getCause().toString()).append("\r\n");
						StackTraceElement[] stackTrace1=t.getCause().getStackTrace();
						for (int i=0;i<stackTrace1.length;i++) {
							sb.append("\t"+stackTrace1[i]).append("\r\n");
						}
					}
					return timestamp+"\r\n"+record.getSourceClassName()+" "+record.getSourceMethodName()+" \r\n"
							+ t.toString()+"\r\n"+sb.toString() 
							+ "\r\n\r\n";
				}
			}
		});
		Main.out.addHandler(fileTxt);
		Main.out.info("Logfile: " + df.format(d) + ".txt");
		Main.out.info("Server Spatial ID: " + Main.SERVER_SPATIALID);
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				Main.out.log(Level.SEVERE, "An uncaught exception was thrown", e);
			}
		});
		
		
		BufferedReader stdin=new BufferedReader(new InputStreamReader(System.in));
		ServerSocket servlet=new ServerSocket(Integer.parseInt(stdin.readLine()));
		new Thread("Socket Acceptor"){
			public void run() {
				while (true) {
					try {
						Socket s=servlet.accept();
						Client client=new Client(s);
						client.valid(false);
						ALL.group(client);
						client.poll();
					} catch (IOException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | NoSuchFieldException e) {
						Main.out.log(Level.WARNING, "An error occured while accepting a client", e);
					}
				}
			}
		}.start();
		
		while (true) {
			Iterator<PacketReceiver> iter=ALL.iterator();
			while (iter.hasNext()) {
				Client c=(Client)iter.next();
				try {
				c.poll();
				} catch (IOException e) {
					Main.disconnectClient(c);
					Main.out.log(Level.SEVERE, c+" is broken", e);
				}
			}
		}
    }
    
    public static int players() {
    	return ALL.size();
    }
    
    public static String[] playerList() {
    	String[] players=new String[players()];
    	int i=0;
    	Iterator<PacketReceiver> iter=ALL.iterator();
    	while (iter.hasNext()) {
    		players[i]=((Client)iter.next()).PlayerData.username;
    		i++;
    	}
    	return players;
    }
    
    public static void registerClient(Client c) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException, InstantiationException, NoSuchFieldException {
    	Packet p=c.job();
    	if (p.<Integer>getField("protocolVersion")!=Main.PROTOCOL) {
    		c.sendPacket(Packet.craftPacket(2, "reason","You are running a different version than us."));
    	} else if (players()+1>Main.MAX_PLAYERS) {
    		c.sendPacket(Packet.craftPacket(2, "reason","This server is full."));
    	} else {
    		c.valid(true);
    		String spatialID=getID("player");
    		c.sendPacket(Packet.c(1, spatialID));
    		broadcast(Packet.c(13, new Object[]{playerList()}));
    		broadcast(Packet.c(6, "player model"));
    		broadcast(Packet.c(5, "player model","material",spatialID));
    		/*
    		 * Packet 8 Scale Entity
    		 * Packet 7 Translate Entity
    		 */
    		out.info(c.PlayerData.username+" is now connected");
    		broadcast(Packet.c(18, "Server",false,c.PlayerData.username+" has joined the game"));
    	}
    }
    
    public static void disconnectClient(Client c) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException, InstantiationException, NoSuchFieldException {
    	c.valid(false);
    	broadcast(Packet.c(18, "Server",false,c.PlayerData.username+" has left the game"));
    	broadcast(Packet.c(12, c.PlayerData.spatialID));
    }
    
    public static void broadcast(Packet p) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
    	Iterator<PacketReceiver> iter=ALL.iterator();
    	while (iter.hasNext()) {
    		iter.next().sendPacket(p);
    	}
    }
    
    public static byte[] getModel(String uri) {
    	return new byte[0];//TODO models
    }
}
