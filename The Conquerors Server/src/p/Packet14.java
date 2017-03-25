package p;

/**
 * Packet 14 Request Window serverbound
 *
 * @author sn
 *
 */
public class Packet14 extends Packet {
	@PacketField
	private final int id = 14;
	@PacketField
	private String spatialID;
}
