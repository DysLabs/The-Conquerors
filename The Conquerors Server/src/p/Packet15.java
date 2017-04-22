package p;

/**
 * Packet 15 Open Window clientbound
 *
 * @author sn
 *
 */
public class Packet15 extends Packet {
	@PacketField
	private final int id = 15;
	@PacketField
	private String spatialID;
	@PacketField
	private String[] slots;
}
