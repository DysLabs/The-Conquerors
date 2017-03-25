package io.github.dyslabs.conquerors;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import p.Packet;

public class PacketInputStream {
	private final GeniusInputStream gis;

	public PacketInputStream(InputStream in) {
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
		int packetID = gis.readInt();
		Class<?> clazz;
		try {
			clazz = Class.forName("p.Packet" + packetID);
		} catch (ClassNotFoundException e) {
			Main.out.warning("Attempted to read non-existant Packet" + packetID);
			return null;
		}
		Constructor<?> ctor = clazz.getConstructor();
		Packet p = (Packet) ctor.newInstance();
		String[] fields = p.getFields();
		String[] types = p.getFieldTypes();
		for (int i = 0; i < fields.length; i++) {
			String f = fields[i].replaceAll("p_", "");
			if (f.equals("id")) {
				continue;// we already have the id
			}
			String t = types[i];
			// System.out.println(f+"("+t+")");
			Object val = gis.getClass().getMethod("read" + t).invoke(gis);
			p.set(f, val);
		}
		Main.out.info("Read " + p);
		return p;
	}
}
