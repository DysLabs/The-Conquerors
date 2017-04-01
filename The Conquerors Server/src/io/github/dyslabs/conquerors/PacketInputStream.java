package io.github.dyslabs.conquerors;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import p.Packet;

public class PacketInputStream {
	private final GeniusInputStream gis;

	public PacketInputStream(final InputStream in) {
		this.gis = new GeniusInputStream(in);
	}

	/**
	 *
	 * @return
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 */
	public Packet readPacket() throws IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
			ClassNotFoundException, NoSuchFieldException {
		final int packetID = this.gis.readInt();
		Class<?> clazz;
		try {
			clazz = Class.forName("p.Packet" + packetID);
		} catch (final ClassNotFoundException e) {
			Main.out.warning("Attempted to read non-existant Packet" + packetID);
			return null;
		}
		final Constructor<?> ctor = clazz.getConstructor();
		final Packet p = (Packet) ctor.newInstance();
		final String[] fields = p.getFields();
		final String[] types = p.getFieldTypes();
		for (int i = 0; i < fields.length; i++) {
			final String f = fields[i].replaceAll("p_", "");
			if (f.equals("id")) {
				continue;// we already have the id
			}
			final String t = types[i];
			// System.out.println(f+"("+t+")");
			final Object val = this.gis.getClass().getMethod("read" + t).invoke(this.gis);
			p.set(f, val);
		}
		Main.out.info(p.toString());
		return p;
	}
}
