package p;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

public abstract class Packet {
	protected GeniusOutputStream out;
	protected GeniusInputStream in;
	
	public Packet(GeniusInputStream in,GeniusOutputStream out) {
		this.in=in;
		this.out=out;
	}
	
	public static Packet getPacket(int pid,GeniusInputStream in,GeniusOutputStream out) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> clazz = Class.forName("p.Packet"+pid);
		Constructor<?> ctor = clazz.getConstructor(GeniusInputStream.class,GeniusOutputStream.class);
		return (Packet)ctor.newInstance(new Object[] { in,out });
	}
	
	public abstract void write(GeniusOutputStream out) throws IOException;
	
	public void write() throws IOException {
		write(out);
	}
	/**
	 * will not read packet id
	 * @throws IOException
	 */
	public abstract void read() throws IOException;
}
