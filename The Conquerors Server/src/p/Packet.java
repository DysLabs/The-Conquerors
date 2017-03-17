package p;

import java.io.IOException;
import java.util.HashMap;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

public abstract class Packet {
	private static HashMap<Integer,Class<?>> packetTable=new HashMap();
	protected GeniusOutputStream out;
	protected GeniusInputStream in;
	
	public static void populatePacketTable() {
		packetTable.put(0, Packet0.class);
	}
	
	public Packet(GeniusInputStream in,GeniusOutputStream out) {
		this.in=in;
		this.out=out;
	}
	
	/**
	 * Will write packet id
	 * @throws IOException
	 */
	public abstract void write() throws IOException;
	/**
	 * will not read packet id
	 * @throws IOException
	 */
	public abstract void read() throws IOException;
}
