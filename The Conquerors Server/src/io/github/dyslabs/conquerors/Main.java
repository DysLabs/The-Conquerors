package io.github.dyslabs.conquerors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import p.Packet;
import p.Packet0;

public class Main {
	public static final int PROTOCOL=0;
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
					} catch (InterruptedException e) {
						System.out.println("["+c+"] broken client");
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
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			}.start();
		}
	}
	
	public static String getID(String object) {
		long id=System.nanoTime()-RANDOM.nextLong();
		return object+id;
	}
	
	public static String getID() {
		return getID("undefinedobject");
	}
}