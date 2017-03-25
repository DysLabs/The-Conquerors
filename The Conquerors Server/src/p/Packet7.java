package p;

/**
 * Packet 7 Translate Entity clientbound
 *
 * @author sn
 *
 */
public class Packet7 extends Packet {
	@PacketField
	private final int id = 7;
	@PacketField
	private String spatialID;
	@PacketField
	private float x, y, z;
}
