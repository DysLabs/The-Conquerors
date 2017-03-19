package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 15 Open Window
 * clientbound
 * @author sn
 *
 */
public class Packet15 extends Packet {
	private String spatialID;
	private byte slots;
	private String[] slot;
	public Packet15(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}

	@Override
	public void write(GeniusOutputStream out) throws IOException {
		out.writeInt(15);//packet id
		out.writeUTF(spatialID);
		out.writeByte(slots);
		for (int i=0;i<slots;i++) {
			out.writeUTF(slot[i]);
		}
	}

	@Override
	public void read() throws IOException {
		this.spatialID=in.readLine();
		this.slots=in.readByte();
		slot=new String[slots];
		for (int i=0;i<slots;i++) {
			slot[i]=in.readLine();
		}
	}

	public byte getSlots() {
		return slots;
	}

	public void setSlots(byte slots) {
		this.slots = slots;
	}

	public String getSpatailID() {
		return spatialID;
	}

	public void setSpatialID(String spatialID) {
		this.spatialID = spatialID;
	}

	public String[] getSlot() {
		return slot;
	}

	public void setSlot(String[] slot) {
		this.slot = slot;
	}

}
