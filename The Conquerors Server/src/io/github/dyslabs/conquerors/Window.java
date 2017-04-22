package io.github.dyslabs.conquerors;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import p.Packet;

public class Window {
	private static class AllianceWindow extends Window {
		public AllianceWindow() {
			this.update();
		}

		@Override
		public String[] texts(final Client c) {
			this.update();
			return super.texts(c);
		}

		public void update() {
			final String[] players = Main.playerList();
			final ArrayList<Slot> slots = new ArrayList<>();
			for (int i = 0; i < players.length; i++) {
				final int j = i;
				slots.add(new Slot(players[i], new JavaFunction() {// qualifier
					@Override
					public Boolean run(final Object... params) {
						final Client c = (Client) params[0];
						return !c.PlayerData.alliance.list().contains(players[j]);
					}
				}, new JavaFunction() {// actor
					@Override
					public <T> T run(final Object... params) {
						final Client c = (Client) params[0];
						try {
							c.sendPacket(Packet.c(18, "Server", false, "Alliance does not function currently"));
						} catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException
								| SecurityException | InvocationTargetException | InstantiationException
								| NoSuchFieldException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						c.PlayerData.alliance.group(Main.getPlayerByUsername(players[j]));
						return null;// T
					}
				}));
			}
			this.slots = new Slot[slots.size()];
			this.slots = slots.toArray(this.slots);
			Main.out.info("Updated " + this.toString());
		}
	}

	public class Slot {
		private final JavaFunction qualifier, action;
		private final String text;

		protected Slot(final String text, final JavaFunction qualifier, final JavaFunction action) {
			this.text = text;
			this.qualifier = qualifier;
			this.action = action;
		}

		public void act(final Client c) {
			this.action.run(c);
		}

		public boolean qualified(final Client c) {
			return this.qualifier.<Boolean>run(c);
		}
	}

	private static HashMap<String, Window> assignments = new HashMap<>();

	private static AllianceWindow AllianceWindow_i = new AllianceWindow();

	public static void declare(final String spatialID, final Window w) {
		Window.assignments.put(spatialID, w);
		Main.out.info("Declared " + w.toString() + " spatialID=" + spatialID);
	}

	public static Window getAllianceWindow() {
		Window.AllianceWindow_i.update();
		return Window.AllianceWindow_i;
	}

	public static Window lookup(final String spatialID) {
		return Window.assignments.get(spatialID);
	}

	protected Slot[] slots;

	private Window() {
	} // subclasses using this constructor must set Slot[]slots in other ways

	private Window(final Slot[] slots) {
		this.slots = slots;
	}

	public Packet asPacket(final Client c, final String spatialID)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		return Packet.c(15, spatialID, this.texts(c));
	}

	public int size(final Client c) {
		return this.slots(c).length;
	}

	public Slot[] slots(final Client c) {
		final ArrayList<Slot> slots = new ArrayList<>();
		for (final Slot s : this.slots) {
			if (s.qualified(c)) {
				slots.add(s);
			}
		}
		final Slot[] ss = new Slot[slots.size()];
		return slots.toArray(ss);
	}

	public String[] texts(final Client c) {
		final Slot[] s = this.slots(c);
		final String[] texts = new String[s.length];
		for (int i = 0; i < s.length; i++) {
			texts[i] = s[i].text;
		}
		return texts;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName() + "{");
		for (final Slot slot : this.slots) {
			sb.append(slot.text).append("; ");
		}
		if (sb.lastIndexOf("; ") != -1) {
			return sb.toString().substring(0, sb.lastIndexOf("; ")) + "}";
		} else {
			return sb.toString() + "}";
		}
	}
}
