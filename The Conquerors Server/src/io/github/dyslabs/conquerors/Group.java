package io.github.dyslabs.conquerors;

import java.io.IOException;
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
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public synchronized void group(final PacketReceiver... pr) {
		for (final PacketReceiver p : pr) {
			this.group(p);
		}
	}

	public synchronized void group(final PacketReceiver pr) {
		this.receivers.add(pr);
		Main.out.info("Grouped " + pr + " into " + this);
		Main.out.info(this.size() + " members in this group");
	}

	public synchronized boolean include(final PacketReceiver pr) {
		return this.receivers.contains(pr);
	}

	public synchronized Iterator<PacketReceiver> iterator() {
		return this.list().iterator();
	}

	public synchronized ArrayList<PacketReceiver> list() {
		final ArrayList<PacketReceiver> list = new ArrayList<>();
		this.receivers.forEach(pr -> {
			if (pr.isValid()) {
				list.add(pr);
			}
		});
		return list;
	}

	public synchronized int size() {
		return this.list().size();
	}

	public synchronized Stream<PacketReceiver> stream() {
		return this.list().stream();
	}

	public synchronized void ungroup(final PacketReceiver... pr) {
		for (final PacketReceiver element : pr) {
			this.receivers.remove(element);
		}
	}
}
