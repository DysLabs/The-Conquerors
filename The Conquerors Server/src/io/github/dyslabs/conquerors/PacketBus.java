package io.github.dyslabs.conquerors;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import p.Packet;

public class PacketBus {
	private static HashMap<Group, ArrayList<Packet>> packets = new HashMap<Group, ArrayList<Packet>>();

	public static synchronized void busPacket(Packet p, Group... groups) {
		for (int i = 0; i < groups.length; i++) {
			Group g = groups[i];
			if (!packets.containsKey(g)) {
				throw new RuntimeException(
						"Attempted to bus packet before delcaring group -- this should never happen");
			}
			packets.get(g).add(p);
		}
	}

	public static synchronized void registerGroup(Group g) {
		packets.put(g, new ArrayList<Packet>());
	}

	/**
	 * This should only ever be called by Main
	 */
	public static synchronized void bus() {
		packets.entrySet().stream().forEach(entry -> {
			Group g = entry.getKey();
			ArrayList<Packet> ps = entry.getValue();
			ps.stream().forEach(p -> {
				g.broadcast(p);
			});
		});
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
	public static Packet craftPacket(int pid, Object... fields)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Class<?> clazz;
		try {
			clazz = Class.forName("p.Packet" + pid);
		} catch (ClassNotFoundException e) {
			System.out.println("Attempted to craft non-existant Packet" + pid);
			return null;
		}
		Constructor<?> ctor = clazz.getConstructor();
		Packet p = (Packet) ctor.newInstance();
		for (int i = 0; i < fields.length; i++) {
			String f = (String) fields[i];
			Object v = fields[i + 1];
			p.set(f, v);
			i++;// fields[i+1] is the value, we need to skip it
		}
		return p;
	}
}
