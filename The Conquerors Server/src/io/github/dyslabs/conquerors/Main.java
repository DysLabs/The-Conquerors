package io.github.dyslabs.conquerors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;
import java.util.stream.IntStream;

import p.Packet;
import p.Packet0;

public class Main {
	public static final int PROTOCOL=0;
	public static final int MAX_PLAYERS=16;
	private static final Group ALL=new Group(); 
	public static final Random RANDOM=new Random(System.nanoTime());
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
							System.err.println("literally cant even disconnect client");
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
	
	/**
	 * When a client has disconnected
	 * @param c
	 * @throws NoSuchFieldException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static void clientDisconnect(Client c) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		System.out.println(c.PlayerData.username+" has disconnected");
		c.valid(false);
		Packet chatMessage=PacketBus.craftPacket(18, new Object[]{
				"sender","Server",
				"ally",false,
				"message",c.PlayerData.username+" has left the game"
		});
		Main.ALL.ungroup(c);
		Main.ALL.broadcast(chatMessage);
		Packet playerList=PacketBus.craftPacket(13, new Object[]{
				"playerNames",Main.playerList()
		});
		Main.ALL.broadcast(playerList);
	}
	
	public static String[] playerList() {
		String[] players=new String[Main.ALL.size()];
		int[] i=new int[]{0};
		Main.ALL.stream().forEach(c -> {
			players[i[0]]=((Client)c).PlayerData.username;
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
	
	public static void registerPlayer(Client c) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Main.ALL.group(c);
		Packet chat=PacketBus.craftPacket(18, new Object[]{
				"sender","Server",
				"ally",false,
				"message","Welcome "+c.PlayerData.username+" to the game!"
		});
		Main.ALL.broadcast(chat);
	}
	
	public static void broadcast(Packet p) {
		Main.ALL.broadcast(p);
	}
}