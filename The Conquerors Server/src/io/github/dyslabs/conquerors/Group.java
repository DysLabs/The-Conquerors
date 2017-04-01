package io.github.dyslabs.conquerors;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.stream.Stream;

import p.Packet;

/**
 * A group is a list of {@link PacketReceivers}
 *
 * @author sn
 *
 */
public class Group {
	private final ArrayList<PacketReceiver> receivers = new ArrayList<>();

	public Group(final PacketReceiver... packetReceivers) {
		this.receivers.addAll(Arrays.<PacketReceiver>asList(packetReceivers));
	}

	public synchronized void broadcast(final Packet p) {
		this.receivers.stream().forEach(r -> {
			if (!r.isValid()) {
				return;
			}
			try {
				r.sendPacket(p);
			} catch (final IllegalArgumentException e) {
				Main.out.log(Level.SEVERE, "An error occured", e);
			} catch (final IllegalAccessException e) {
				Main.out.log(Level.SEVERE, "An error occured", e);
			} catch (final NoSuchMethodException e) {
				Main.out.log(Level.SEVERE, "An error occured", e);
			} catch (final SecurityException e) {
				Main.out.log(Level.SEVERE, "An error occured", e);
			} catch (final InvocationTargetException e) {
				Main.out.log(Level.SEVERE, "An error occured", e);
			}
		});
	}

	public synchronized void group(final PacketReceiver... pr) {
		for (PacketReceiver p : pr) {
			group(p);
		}
	}
	
	public synchronized void group(PacketReceiver pr) {
		this.receivers.add(pr);
		Main.out.info("Grouped " + pr + " into " + this);
		Main.out.info(this.size() + " members in this group");
	}

	public synchronized boolean include(final PacketReceiver pr) {
		return this.receivers.contains(pr);
	}

	public synchronized int size() {
		return list().size();
	}

	public synchronized Stream<PacketReceiver> stream() {
		return list().stream();
	}
	
	public synchronized Iterator<PacketReceiver> iterator() {
		return list().iterator();
	}

	public synchronized void ungroup(final PacketReceiver... pr) {
		for (final PacketReceiver element : pr) {
			// System.out.println("Ungrouped "+pr[i]);
			this.receivers.remove(element);
		}
	}
	
	public synchronized ArrayList<PacketReceiver> list() {
		ArrayList<PacketReceiver> list=new ArrayList<>();
		this.receivers.forEach(pr -> {
			if (pr.isValid()) {
				list.add(pr);
			}
		});
		return list;
	}
}
