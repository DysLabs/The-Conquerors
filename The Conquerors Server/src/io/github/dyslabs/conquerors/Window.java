package io.github.dyslabs.conquerors;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import p.Packet;

public class Window {
	private static HashMap<String,Window> assignments=new HashMap<>();
	private static AllianceWindow AllianceWindow_i=new AllianceWindow();
	public static Window getAllianceWindow() {
		AllianceWindow_i.update();
		return AllianceWindow_i;
	}
	public static void declare(String spatialID, Window w) {
		assignments.put(spatialID, w);
		Main.out.info("Declared "+w.toString()+" spatialID="+spatialID);
	}
	
	public static Window lookup(String spatialID) {
		return assignments.get(spatialID);
	}
	
	protected Slot[] slots;
	private Window(Slot[] slots) {
		this.slots=slots;
	}
	
	private Window() {} // subclasses using this constructor must set Slot[]slots in other ways
	
	public Packet asPacket(Client c,String spatialID) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		return Packet.c(15, spatialID, texts(c));
	}
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(this.getClass().getSimpleName()+"{");
		for (int i=0;i<slots.length;i++) {
			sb.append(slots[i].text).append("; ");
		}
		if (sb.lastIndexOf("; ")!=-1) {
			return sb.toString().substring(0, sb.lastIndexOf("; "))+"}";
		} else {
			return sb.toString()+"}";
		}
	}
	
	public Slot[] slots(Client c) {
		ArrayList<Slot> slots=new ArrayList<>();
		for (Slot s : this.slots) {
			if (s.qualified(c)) {
				slots.add(s);
			}
		}
		Slot[] ss=new Slot[slots.size()];
		return slots.toArray(ss);
	}
	
	public int size(Client c) {
		return slots(c).length;
	}
	
	public String[] texts(Client c) {
		Slot[] s=slots(c);
		String[] texts=new String[s.length];
		for (int i=0;i<s.length;i++) {
			texts[i]=s[i].text;
		}
		return texts;
	}
	
	public class Slot {
		private JavaFunction qualifier,action;
		private String text;
		protected Slot(String text,JavaFunction qualifier,JavaFunction action) {
			this.text=text;
			this.qualifier=qualifier;
			this.action=action;
		}
		
		public boolean qualified(Client c) {
			return qualifier.<Boolean>run(c);
		}
		
		public void act(Client c) {
			action.run(c);
		}
	}
	
	private static class AllianceWindow extends Window {
		public AllianceWindow() {
			update();
		}
		
		public String[] texts(Client c) {
			update();
			return super.texts(c);
		}
		
		public void update() {
			String[] players=Main.playerList();
			ArrayList<Slot> slots=new ArrayList<>();
			for (int i=0;i<players.length;i++) {
				int j=i;
				slots.add(new Slot(players[i], 
						new JavaFunction(){//qualifier
							@Override
							public Boolean run(Object... params) {
								Client c=(Client)params[0];
								return !c.PlayerData.alliance.list().contains(players[j]);
							}}, 
						new JavaFunction(){//actor
							@Override
							public <T> T run(Object... params) {
								Client c=(Client)params[0];
								try {
									c.sendPacket(Packet.c(18, "Server",false,"Alliance does not function currently"));
								} catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException
										| SecurityException | InvocationTargetException | InstantiationException
										| NoSuchFieldException | IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								c.PlayerData.alliance.group(Main.getPlayerByUsername(players[j]));
								return null;//T
							}}));
			}
			this.slots=new Slot[slots.size()];
			this.slots=slots.toArray(this.slots);
			Main.out.info("Updated "+this.toString());
		}
	}
}
