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
import java.util.stream.IntStream;

import io.github.dyslabs.conquerors.window.Window;
import p.Packet;
import p.Packet0;

public class Main {
	public static final int PROTOCOL=0;
	public static final int MAX_PLAYERS=16;
	private static final Group ALL=new Group(); 
	public static final Random RANDOM=new Random(System.nanoTime());
	public static final Date START=new Date();
	public static void main(String[]args) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException {
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter server port:");
		int port=Integer.parseInt(in.readLine());
		ServerSocket servlet=new ServerSocket(port);
		new Thread(){
			@Override
			public void run() {
				while (true) {
					PacketBus.bus();
				}
			}
		}.start();
		
		START.setTime(System.currentTimeMillis());
		while (true) {
			Client c=new Client(servlet.accept());
			ALL.group(c);
			new Thread(getID("ClientThread")){
				public void run() {
					try {
						c.run();
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
					} catch (SocketException e) {//the client has stopped responding
						//or other network issue
						//System.out.println(Thread.currentThread().getName()+"} stopped running");
						try {
							Main.clientDisconnect(c);
						} catch (NoSuchMethodException | SecurityException | InstantiationException
								| IllegalAccessException | IllegalArgumentException | InvocationTargetException
								| NoSuchFieldException e1) {
							System.out.println("cant disconnect client");
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
		String[] players=new String[Main.ALL.size()];
		int[] i=new int[]{0};
		Main.ALL.stream().forEach(c -> {
			players[i[0]]=((Client)c).PlayerData.username;
			i[0]++;
		});
		return players;
	}
	
	public static int players() {
		return Main.ALL.size();
	}
	
	public static String getID(String object) {
		long id=System.nanoTime()-RANDOM.nextLong();
		return object+id;
	}
	
	public static String getID() {
		return getID("undefinedobject");
	}
	
	public static void registerPlayer(Packet p,Client c) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		/*
		 * Assuming login success:
		 * Add player to ALL group
		 * Set a spatialID for the player
		 * Send login success packet
		 * Broadcast PlayerList Packet
		 * Broadcast CheckModel Packet
		 * Broadcast SpawnEntity
		 * (As Appropriate) Broadcast TranslateEntity, ScaleEntity, and RotateEntity
		 */
		if (ALL.size()>=Main.MAX_PLAYERS) {
			c.sendPacket(PacketBus.craftPacket(2, new Object[]{
					"reason","The server is full, sorry"
			}));
		} else if (p.<Integer>getField("protocolVersion")!=Main.PROTOCOL) {
			c.sendPacket(PacketBus.craftPacket(2, 
					"reason","You are running a different version than the server"
			));
		}
		else {//login success
			c.PlayerData.spatialID=Main.getID("player");
			//login success packet
			c.sendPacket(PacketBus.craftPacket(1, 
					"spatialID",c.PlayerData.spatialID
			));
			//player list packet
			Main.ALL.group(c);
			Main.broadcastPlayerList();
			//check model
			//TODO: Add models
			//spawn entity
			Main.ALL.broadcast(PacketBus.craftPacket(5, 
					"model","player model", //TODO: model support
					"material","player material", //TODO: model support
					"spatialID",c.PlayerData.spatialID
			));
			//set entity (translate, scale, and rotate
			Main.ALL.broadcast(PacketBus.craftPacket(7, 
					"spatialID",c.PlayerData.spatialID,
					"x",0f, // * TODO:
					"y",0f, // * Add map dimensions
					"z",0f  // * Entity support
			));
		}
	}
	
	public static void broadcastPlayerList() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		String[] playerNames=Main.playerList();
		Main.ALL.broadcast(PacketBus.craftPacket(13, 
				"playerNames",playerNames
		));
	}
	
	public static void checkModel(String modelUri) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Main.ALL.broadcast(PacketBus.craftPacket(6, 
				"modelName",modelUri
		));
	}
	
	public static byte[] getModel(String modelUri) {
		return new byte[0];//TODO: model support
	}
	
	public static void clientDisconnect(Client c) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		/**
		 * Ungroup player
		 * Update player list
		 * Remove entity
		 */
		c.valid(false);
		Main.ALL.ungroup(c);
		Main.broadcastPlayerList();
		Main.broadcast(PacketBus.craftPacket(12, 
				"spatialID",c.PlayerData.spatialID
		));
	}
	
	public static void broadcast(Packet p) {
		Main.ALL.broadcast(p);
	}
	
	public static String chatMsg(String snd,String msg) {
		Date now=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("h:m");
		return "["+sdf.format(now)+"] "+snd+": "+msg;
	}
	
	public static void chat(String sender,boolean ally,String message) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Main.broadcast(PacketBus.craftPacket(18, 
				"sender",sender,
				"ally",ally,
				"message",message
		));
	}
	
	public static Packet encodeWindowAsPacket(Window w,Client c) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		return PacketBus.craftPacket(15, "slots",w.slotsText(c));
	}
	
	public static Client getPlayer(String username) {
		Client[] c=new Client[]{null};
		Main.ALL.stream().forEach(pr -> {
			Client client=(Client)pr;
			if (username.equals(client.PlayerData.username)) {
				c[0]=client;
			}
		});
		return c[0];
	}
}