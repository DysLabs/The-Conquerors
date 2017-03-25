package io.github.dyslabs.conquerors;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
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
			if (!((Client) r).isValid()) {
				this.ungroup(r);
				return;
			}
			try {
				r.sendPacket(p);
			} catch (final IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public synchronized void group(final PacketReceiver... pr) {
		this.receivers.addAll(Arrays.<PacketReceiver>asList(pr));
		Main.out.info("Grouped " + pr + " into " + this);
		Main.out.info(this.size() + " members in this group");
	}

	public synchronized boolean include(final PacketReceiver pr) {
		return this.receivers.contains(pr);
	}

	public synchronized int size() {
		return this.receivers.size();
	}

	public synchronized Stream<PacketReceiver> stream() {
		return this.receivers.stream();
	}

	public synchronized void ungroup(final PacketReceiver... pr) {
		for (final PacketReceiver element : pr) {
			// System.out.println("Ungrouped "+pr[i]);
			this.receivers.remove(element);
		}
	}
}
