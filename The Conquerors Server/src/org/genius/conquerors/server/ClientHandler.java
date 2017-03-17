package org.genius.conquerors.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
	private final Socket s;
	private final InputStream rawIn;
	private final OutputStream rawOut;
	public ClientHandler(Socket s) throws IOException {
		this.s=s;
		this.rawIn=s.getInputStream();
		this.rawOut=s.getOutputStream();
	}
	
	private void init() throws IOException {
		GeniusInputStream in=new GeniusInputStream(rawIn);
		GeniusOutputStream out=new GeniusOutputStream(rawOut);
		PushbackInputStream check=new PushbackInputStream(rawIn);
		while (check.available()!=0) {
			int pid=in.readInt();//packed id
		}
	}
	
	public void run() {
		init();
	}
}
