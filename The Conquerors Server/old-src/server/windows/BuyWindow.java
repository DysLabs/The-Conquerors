package org.genius.conquerors.server.windows;
import org.genius.conquerors.server.ClientHandler;
import org.genius.conquerors.server.GameWindow;
import org.genius.conquerors.server.JavaFunction;

public class BuyWindow extends GameWindow {

	public BuyWindow() {
		super(getBuildings(),getQualifiers());
		
	}
	
	private static String[] getBuildings() {
		String[] names=new String[Buildings.values().length];
		for (int i=0;i<names.length;i++) {
			names[i]=Buildings.values()[i].name();
		}
		return names;
	}
	
	private static JavaFunction[] getQualifiers() {
		Buildings[] b=Buildings.values();
		JavaFunction[] jfs=new JavaFunction[b.length];
		for (int i=0;i<b.length;i++) {
			jfs[i]=b[i].qualifier;
		}
		return jfs;
	}

	public enum Buildings {
		LANDMINE(20),
		RESEARCH_CENTER(100),
		HOSPITAL(180),
		TURRENT(65),
		POWER_PLANT(65),
		NAVAL_SHIPYARD(80),
		NAVAL_HOUSE(50),
		SOLDIER_HOUSE(40),
		NUCLEAR_PLANT(140),
		SPACE_LINK(440),
		HEADQUARTERS(500),
		BARRACKS(150),
		WATERMINE(30),
		PLANE_HOUSE(60),
		FORT(200),
		NUCLEAR_SILO(675),
		AIRPORT(300),
		TANK_HOUSE(50),
		TANK_FACTORY(235),
		BUNKER(100),
		ANTIAIR_TURRENT(85),
		COMMANDER_CENTER(400),;
		
		public final int cost;
		public final JavaFunction qualifier;
		
		private Buildings(int cost,JavaFunction qualifier) {
			this.cost=cost;
			this.qualifier=qualifier;
		}
		
		private Buildings(int cost) {
			this(cost,new JavaFunction(){
				@Override
				public Boolean run(Object... args) {
					ClientHandler h=(ClientHandler)args[0];
					return (h.money>=cost);
				}});
		}
	}
}
