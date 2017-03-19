package genius;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

import p.Packet;
import p.Packet0;

public class Main {
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
	}
}
