package p;

/**
 * Packet 9 Rotate Entity clientbound
 *
 * @author sn
 *
 */
public class Packet9 extends Packet {
	@PacketField
	private final int id = 9;
	@PacketField
	private String spatialID;
	@PacketField
	private float x, y, z;
}
