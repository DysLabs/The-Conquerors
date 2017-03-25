package io.github.dyslabs.conquerors;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.BaseStream;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import p.Packet;

/**
 * A group is a list of {@link PacketReceivers}
 * 
 * @author sn
 *
 */
public class Group {
	private final ArrayList<PacketReceiver> receivers = new ArrayList<PacketReceiver>();

	public Group(PacketReceiver... packetReceivers) {
		this.receivers.addAll(Arrays.<PacketReceiver>asList(packetReceivers));
	}

	public synchronized void group(PacketReceiver... pr) {
		this.receivers.addAll(Arrays.<PacketReceiver>asList(pr));
		Main.out.info("Grouped " + pr + " into " + this);
		Main.out.info(size() + " members in this group");
	}

	public synchronized void ungroup(PacketReceiver... pr) {
		for (int i = 0; i < pr.length; i++) {
			// System.out.println("Ungrouped "+pr[i]);
			this.receivers.remove(pr[i]);
		}
	}

	public synchronized void broadcast(Packet p) {
		receivers.stream().forEach(r -> {
			if (!((Client) r).isValid()) {
				ungroup(r);
				return;
			}
			try {
				r.sendPacket(p);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public synchronized Stream<PacketReceiver> stream() {
		return receivers.stream();
	}

	public synchronized int size() {
		return receivers.size();
	}

	public synchronized boolean include(PacketReceiver pr) {
		return receivers.contains(pr);
	}
}
