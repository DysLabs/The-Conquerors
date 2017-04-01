package io.github.dyslabs.conquerors;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import p.Packet;

public class PacketReceiver {
	private final PacketOutputStream out;
	private Socket s;
	private boolean valid=false;

	public PacketReceiver(final Socket out) throws IOException {
		this.s = out;
		this.out=new PacketOutputStream(out.getOutputStream());
	}

	public void sendPacket(final Packet p) throws IllegalArgumentException, IllegalAccessException,
			NoSuchMethodException, SecurityException, InvocationTargetException {
		this.out.writePacket(p);
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public void valid(boolean valid) {
		this.valid=valid;
		Main.out.warning(this+" changed to "+valid);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+"["+s.getRemoteSocketAddress()+"]";
	}
}
