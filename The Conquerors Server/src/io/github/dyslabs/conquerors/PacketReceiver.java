package io.github.dyslabs.conquerors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import p.Packet;

public class PacketReceiver {
	private final PacketOutputStream out;
	private OutputStream s;
	private ByteArrayOutputStream bo;
	private boolean valid=false;
	private SocketChannel client;

	public PacketReceiver(final OutputStream out,SocketChannel client) throws IOException {
		this.s = out;
		this.bo=new ByteArrayOutputStream();
		this.out=new PacketOutputStream(bo);
		this.client=client;
	}

	public void sendPacket(final Packet p) throws IllegalArgumentException, IllegalAccessException,
			NoSuchMethodException, SecurityException, InvocationTargetException, IOException {
		this.out.writePacket(p);
		write(bo.toByteArray(),client);
	}
	
	private void write(byte[]data,SocketChannel client) throws IOException {
		ByteBuffer buf=ByteBuffer.wrap(data);
		client.write(buf);
		buf.clear();
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public void valid(boolean valid) {
		this.valid=valid;
		Main.out.warning(this+" changed to "+valid);
	}
	
	@Override
	public String toString() {
		//return getClass().getSimpleName()+"["+s.getRemoteSocketAddress()+"]";
		return super.toString();
	}
}
