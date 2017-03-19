package org.genius.conquerors.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import p.Packet;

public class Main {
	public static final int VERSION=0;
	public static byte playerCount=0;
	public static String[] playerList=new String[0];
	private static ArrayList<ClientHandler> clientHandlers=new ArrayList();
	private static int entityCount=0;
	private static HashMap<String,GameWindow> windows=new HashMap();
	public static void main(String[]args) throws NumberFormatException, IOException {
		System.out.print("Enter server port:");
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		ServerSocket servlet=new ServerSocket(Integer.parseInt(in.readLine()));
		while (true) {
			ClientHandler h=new ClientHandler(servlet.accept());
			clientHandlers.add(h);
			h.start();
		}
	}
	
	public static GameWindow getWindowForSpatialID(String spatialid) {
		return windows.get(spatialid);
	}
	
	public static GameWindow registerWindowForSpatial(String spatial,GameWindow window) {
		return windows.put(spatial, window);
	}
	
	public static void broadcastPacket(Packet p) {
		clientHandlers.stream().forEach(h -> {
			try {
				h.sendPacket(p);
			} catch (IOException e) {
				System.out.println("broken client");
			}
		});
	}
	
	public static synchronized String getSpatialID(String object) {
		return object+"["+entityCount+"]";
	}
	
	public static synchronized String getSpatialID() {
		byte[] c=new byte[5];
		Random r=new Random(System.nanoTime());
		r.nextBytes(c);
		return getSpatialID(new String(c));
	}
}
