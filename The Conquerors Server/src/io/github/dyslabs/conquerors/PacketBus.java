package io.github.dyslabs.conquerors;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import p.Packet;

public class PacketBus {
	private static HashMap<Group, ArrayList<Packet>> packets = new HashMap<>();

	/**
	 * This should only ever be called by Main
	 */
	public static synchronized void bus() {
		PacketBus.packets.entrySet().stream().forEach(entry -> {
			final Group g = entry.getKey();
			final ArrayList<Packet> ps = entry.getValue();
			ps.stream().forEach(p -> {
				g.broadcast(p);
			});
		});
	}

	public static synchronized void busPacket(final Packet p, final Group... groups) {
		for (final Group group : groups) {
			final Group g = group;
			if (!PacketBus.packets.containsKey(g)) {
				throw new RuntimeException(
						"Attempted to bus packet before delcaring group -- this should never happen");
			}
			PacketBus.packets.get(g).add(p);
		}
	}

	/**
	 *
	 * @param pid
	 * @param fields
	 *            format of fieldName(String),value(T)
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchFieldException
	 */
	public static Packet craftPacket(final int pid, final Object... fields)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Class<?> clazz;
		try {
			clazz = Class.forName("p.Packet" + pid);
		} catch (final ClassNotFoundException e) {
			System.out.println("Attempted to craft non-existant Packet" + pid);
			return null;
		}
		final Constructor<?> ctor = clazz.getConstructor();
		final Packet p = (Packet) ctor.newInstance();
		for (int i = 0; i < fields.length; i++) {
			final String f = (String) fields[i];
			final Object v = fields[i + 1];
			p.set(f, v);
			i++;// fields[i+1] is the value, we need to skip it
		}
		return p;
	}

	public static synchronized void registerGroup(final Group g) {
		PacketBus.packets.put(g, new ArrayList<Packet>());
	}
}
