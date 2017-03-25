package io.github.dyslabs.conquerors.window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import io.github.dyslabs.conquerors.Client;
import io.github.dyslabs.conquerors.JavaFunction;
import io.github.dyslabs.conquerors.Main;

public class Window {
	public static Window BUY_WINDOW=new BuyWindow();
	public static Window ALLY_WINDOW=new AllyWindow();
	public static void refreshAllyWindow() {
		Window.ALLY_WINDOW=new AllyWindow();
	}
	
	private static HashMap<String,Window> spatialIDLookup=new HashMap<String, Window>();
	public static void assignWindow(String spatialID,Window w) {
		spatialIDLookup.put(spatialID, w);
	}
	
	public static Window findWindow(String spatialID) {
		return spatialIDLookup.get(spatialID);
	}
	
	protected ArrayList<Slot> slots=new ArrayList<Slot>();
	
	private Window(Slot[]slots) {
		this.slots.addAll(Arrays.asList(slots));
	}
	
	private Window() {
		// Must declare slots in this constructor
	}
	
	private Slot[] slots(Client c) {
		int[] i=new int[]{0};
		slots.stream().forEach(slot -> {
			if (slot.qualified(c)) {
				i[0]++;
			}
		});
		Slot[] slotList=new Slot[i[0]];
		i[0]=0;
		slots.stream().forEach(slot -> {
			if (slot.qualified(c)) {
				slotList[i[0]]=slot;
				i[0]++;
			}
		});
		return slotList;
	}
	
	public int slotsSize(Client c) {
		return slots(c).length;
	}
	
	public String[] slotsText(Client c) {
		Slot[] s=this.slots(c);
		String[] t=new String[s.length];
		for (int i=0;i<s.length;i++) {
			t[i]=s[i].text;
		}
		return t;
	}
	
	public void select(byte b,Client c) {
		slots.get(b).action(c);
	}
	
	private class Slot {
		byte id;
		String text;
		JavaFunction qualifier,action;
		
		public Slot(byte id,String text,JavaFunction qualifier,JavaFunction action) {
			this.id=id;
			this.text=text;
			setQualifier(qualifier);
			setAction(action);
		}
		
		/**
		 * A Java function that returns a boolean if the player is qualified to have this slot
		 * @param qualifier
		 */
		public void setQualifier(JavaFunction qualifier) {
			this.qualifier=qualifier;
		}
		
		/**
		 * The action is a Java function that returns nothing when the player selects this slot
		 * @param action
		 */
		public void setAction(JavaFunction action) {
			this.action=action;
		}
		
		public boolean qualified(Client c) {
			return this.qualifier.<Boolean>run(c);
		}
		
		public void action(Client c) {
			this.action.run(c);
		}
	}
	
	private static class BuyWindow extends Window {
		public BuyWindow() {
			Buildings[] bs=Buildings.values();
			Slot[] slots=new Slot[bs.length];
			for (int i=0;i<bs.length;i++) {
				slots[i]=new Slot((byte)i,bs[i].name(),bs[i].qualifier,new JavaFunction(){
					@Override
					public <T> T run(Object... params) {
						// TODO Add code to handle building being built
						return null;
					}
					
				});
			}
			this.slots.addAll(Arrays.asList(slots));
		}
		
		private enum Buildings {
			// Commented builings will be included in a later release
			//LANDMINE("Landmine",20),
			//RESEARCH_CENTER("Research Center",100),
			//HOSPITAL("Hospital",180),
			TURRENT("Turrent",65),
			//POWER_PLANT("Power Plant",65),
			//NAVAL_SHIPYARD("Naval Shipyard",80),
			//NAVAL_HOUSE("Naval House",50),
			//SOLDIER_HOUSE("Soldier House",40),
			NUCLEAR_PLANT("Nuclear Plant",140),
			//SPACE_LINK("Space Link",440),
			//HEADQUARTERS("Headquarters",500),
			BARRACKS("Barracks",150),
			//WATERMINE("Watermine",30),
			//PLANE_HOUSE("Plane House",60),
			//FORT("Fort",200),
			//NUCLEAR_SILO("Nuke Silo",675),
			//AIRPORT("Airport",300),
			//TANK_HOUSE("Tank House",50),
			//TANK_FACTORY("Tank Factory",235),
			//BUNKER("Bunker",100),
			//ANTIAIR_TURRENT("Anti-Air Turrent",85),
			COMMANDER_CENTER("Command Center",400);
			
			public final int cost;
			public final String name;
			public final JavaFunction qualifier;
			
			private Buildings(String name,int cost,JavaFunction qualifier) {
				this.name=name;
				this.cost=cost;
				this.qualifier=qualifier;
			}
			
			private Buildings(String name,int cost) {
				this(name,cost,new JavaFunction(){
					@Override
					public Boolean run(Object... args) {
						Client h=(Client)args[0];
						return (h.PlayerData.money>=cost);
					}});
			}
		}
	}
	
	private static class AllyWindow extends Window {
		public AllyWindow() {
			String[] playerList=Main.playerList();
			Slot[] clients=new Slot[playerList.length];
			for (int i=0;i<clients.length;i++) {
				Client c=Main.getPlayer(playerList[i]);
				clients[i]=new Slot((byte)i,playerList[i],new JavaFunction(){
					//qualifier
					public Boolean run(Object... params) {
						Client cl=(Client)params[0];
						return cl.PlayerData.alliance.include(c);
					}}, new JavaFunction(){
						//action
						@Override
						public <T> T run(Object... params) {
							// TODO Auto-generated method stub
							return null;
						}});
			}
			this.slots.addAll(Arrays.asList(clients));
		}
	}
}
