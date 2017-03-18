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
			final int j=i;
			jfs[i]=new JavaFunction(){
				@Override
				public Boolean run(Object... args) {
					ClientHandler h=(ClientHandler)args[0];
					return (h.money>=b[j].cost);
				}};
		}
		return jfs;
	}

	private enum Buildings {
		FORT(200),
		COMMANDER_CENTER(400)
		;
		
		public final int cost;
		
		private Buildings(int cost) {
			this.cost=cost;
		}
	}
}
