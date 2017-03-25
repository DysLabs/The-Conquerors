package io.github.dyslabs.conquerors;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import p.Packet;

public class PacketReceiver {
	private final PacketOutputStream out;

	public PacketReceiver(OutputStream out) {
		this.out = new PacketOutputStream(out);
	}

	public void sendPacket(Packet p) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException,
			SecurityException, InvocationTargetException {
		out.writePacket(p);
	}
}
