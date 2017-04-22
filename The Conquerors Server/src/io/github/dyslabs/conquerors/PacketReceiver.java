package io.github.dyslabs.conquerors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import p.Packet;

public class PacketReceiver {
	private final PacketOutputStream out;
	private final OutputStream s;
	private final ByteArrayOutputStream bo;
	private boolean valid = false;
	private final SocketChannel client;

	public PacketReceiver(final OutputStream out, final SocketChannel client) throws IOException {
		this.s = out;
		this.bo = new ByteArrayOutputStream();
		this.out = new PacketOutputStream(this.bo);
		this.client = client;
	}

	public boolean isValid() {
		return this.valid;
	}

	public void sendPacket(final Packet p) throws IllegalArgumentException, IllegalAccessException,
			NoSuchMethodException, SecurityException, InvocationTargetException, IOException {
		this.out.writePacket(p);
		this.write(this.bo.toByteArray(), this.client);
		this.bo.reset();
	}

	@Override
	public String toString() {
		// return getClass().getSimpleName()+"["+s.getRemoteSocketAddress()+"]";
		return super.toString();
	}

	public void valid(final boolean valid) {
		this.valid = valid;
		Main.out.warning(this + " changed to " + valid);
	}

	private void write(final byte[] data, final SocketChannel client) throws IOException {
		final ByteBuffer buf = ByteBuffer.wrap(data);
		client.write(buf);
		buf.clear();
	}
}
