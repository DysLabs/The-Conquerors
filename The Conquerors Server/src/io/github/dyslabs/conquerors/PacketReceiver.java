package io.github.dyslabs.conquerors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import p.Packet;

public class PacketReceiver {
	private final PacketOutputStream out;
	private final ByteArrayOutputStream bo;
	private final String ipString;
	private boolean valid = false;

	public PacketReceiver(final String ip) throws IOException {
		this.bo = new ByteArrayOutputStream();
		this.out = new PacketOutputStream(this.bo);
		this.ipString = ip;
	}

	public boolean isValid() {
		return this.valid;
	}

	public void sendPacket(final Packet p) throws IllegalArgumentException, IllegalAccessException,
			NoSuchMethodException, SecurityException, InvocationTargetException, IOException {
		this.out.writePacket(p);
		this.write(this.bo.toByteArray());
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

	private void write(final byte[] data) throws IOException {
		Main.server.addPacket(this.ipString, data);
	}
}
