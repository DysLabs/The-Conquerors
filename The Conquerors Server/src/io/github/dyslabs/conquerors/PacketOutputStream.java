package io.github.dyslabs.conquerors;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import p.Packet;

public class PacketOutputStream {
	private final GeniusOutputStream gos;

	public PacketOutputStream(OutputStream out) {
		this.gos = new GeniusOutputStream(out);
	}

	public void writePacket(Packet p) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException,
			SecurityException, InvocationTargetException {
		if (!p.initialized()) {
			System.out.println("Cannot write non-finalized packet");
			return;
		}
		String[] fields = p.getFields();
		String[] dataTypes = p.getFieldTypes();
		Object[] data = p.getFieldValues();
		for (int i = 0; i < dataTypes.length; i++) {
			String type = dataTypes[i];
			Object field = data[i];
			try {
				gos.getClass().getMethod("write" + type, field.getClass()).invoke(gos, field);
			} catch (NullPointerException npe) {
				Main.out.warning("Attempted to send non-finalized packet. Missing field " + fields[i]);
			}
		}
		Main.out.info("Successfully sent: " + p);
	}
}