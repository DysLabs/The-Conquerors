package p;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

public abstract class Packet {
	private static HashMap<Integer,Class<?>> packetTable=new HashMap();
	protected GeniusOutputStream out;
	protected GeniusInputStream in;
	
	public static void populatePacketTable() {
		packetTable.put(0, Packet0.class);
		packetTable.put(1, Packet1.class);
		packetTable.put(2, Packet2.class);
		packetTable.put(3, Packet3.class);
		packetTable.put(4, Packet4.class);
		packetTable.put(12, Packet12.class);
		packetTable.put(5, Packet5.class);
		packetTable.put(6, Packet6.class);
		packetTable.put(7, Packet7.class);
		packetTable.put(8, Packet8.class);
		packetTable.put(9, Packet9.class);
		packetTable.put(10, Packet10.class);
		packetTable.put(11, Packet11.class);
	}
	
	public Packet(GeniusInputStream in,GeniusOutputStream out) {
		this.in=in;
		this.out=out;
	}
	
	public static Packet getPacket(int pid,GeniusInputStream in,GeniusOutputStream out) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> clazz = Class.forName("Packet"+pid);
		Constructor<?> ctor = clazz.getConstructor(GeniusInputStream.class,GeniusOutputStream.class);
		return (Packet)ctor.newInstance(new Object[] { in,out });
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
