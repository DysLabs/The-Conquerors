package io.github.dyslabs.conquerors;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import p.Packet;

public class PacketReceiver {
	private final PacketOutputStream out;

	public PacketReceiver(final OutputStream out) {
		this.out = new PacketOutputStream(out);
	}

	public void sendPacket(final Packet p) throws IllegalArgumentException, IllegalAccessException,
			NoSuchMethodException, SecurityException, InvocationTargetException {
		this.out.writePacket(p);
	}
}
