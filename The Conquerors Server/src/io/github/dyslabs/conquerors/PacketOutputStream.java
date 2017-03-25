package io.github.dyslabs.conquerors;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import p.Packet;

public class PacketOutputStream {
	private final GeniusOutputStream gos;

	public PacketOutputStream(final OutputStream out) {
		this.gos = new GeniusOutputStream(out);
	}

	public void writePacket(final Packet p) throws IllegalArgumentException, IllegalAccessException,
			NoSuchMethodException, SecurityException, InvocationTargetException {
		if (!p.initialized()) {
			System.out.println("Cannot write non-finalized packet");
			return;
		}
		final String[] fields = p.getFields();
		final String[] dataTypes = p.getFieldTypes();
		final Object[] data = p.getFieldValues();
		for (int i = 0; i < dataTypes.length; i++) {
			final String type = dataTypes[i];
			final Object field = data[i];
			try {
				this.gos.getClass().getMethod("write" + type, field.getClass()).invoke(this.gos, field);
			} catch (final NullPointerException npe) {
				Main.out.warning("Attempted to send non-finalized packet. Missing field " + fields[i]);
			}
		}
		Main.out.info("Successfully sent: " + p);
	}
}
