package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 19 Select Window Slot
 * serverbound
 * @author sn
 *
 */
public class Packet19 extends Packet {
	private String spatialID;
	private byte slot;
	public Packet19(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}
	
	@Override
	public void write(GeniusOutputStream out) throws IOException {
		out.writeInt(19);
		out.writeUTF(spatialID);
		out.writeByte(slot);
	}
	
	@Override
	public void read() throws IOException {
		this.spatialID=in.readLine();
		this.slot=in.readByte();
	}

	public String getSpatialID() {
		return spatialID;
	}

	public void setSpatialID(String spatialID) {
		this.spatialID = spatialID;
	}

	public byte getSlot() {
		return slot;
	}

	public void setSlot(byte slot) {
		this.slot = slot;
	}

}
