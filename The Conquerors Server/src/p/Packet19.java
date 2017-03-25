package p;

/**
 * Packet 19 Select Window Slot serverbound
 *
 * @author sn
 *
 */
public class Packet19 extends Packet {
	@PacketField
	private final int id = 19;
	@PacketField
	private String spatialID;
	@PacketField
	private byte slot;
}
