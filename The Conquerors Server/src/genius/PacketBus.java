package genius;

import java.util.ArrayList;
import java.util.HashMap;

import p.Packet;

public class PacketBus {
	private static HashMap<Group,ArrayList<Packet>> packets=new HashMap<Group, ArrayList<Packet>>();
	public static synchronized void busPacket(Packet p, Group...groups) {
		for (int i=0;i<groups.length;i++) {
			Group g=groups[i];
			if (!packets.containsKey(g)) {
				throw new RuntimeException("Attempted to bus packet before delcaring group -- this should never happen");
			}
			packets.get(g).add(p);
		}
	}
	
	/**
	 * This should only ever be called by Main
	 */
	public static synchronized void bus() {
		packets.entrySet().stream().forEach(entry -> {
			Group g=entry.getKey();
			ArrayList<Packet> ps=entry.getValue();
			ps.stream().forEach(p -> {
				g.broadcast(p);
			});
		});
	}
}
