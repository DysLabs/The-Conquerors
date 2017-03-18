package org.genius.conquerors.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;

import p.Packet;

public class Main {
	private static ArrayList<ClientHandler> clientHandlers=new ArrayList();
	public static void main(String[]args) throws NumberFormatException, IOException {
		Packet.populatePacketTable();
		System.out.print("Enter server port:");
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		ServerSocket servlet=new ServerSocket(Integer.parseInt(in.readLine()));
		while (true) {
			ClientHandler h=new ClientHandler(servlet.accept());
			clientHandlers.add(h);
			h.start();
		}
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
}
