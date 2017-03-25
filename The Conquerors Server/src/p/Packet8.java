package p;

/**
 * Packet 8 Scale Entity clientbound
 *
 * @author sn
 *
 */
public class Packet8 extends Packet {
	@PacketField
	private final int id = 8;
	@PacketField
	private String spatialID;
	@PacketField
	private float x, y, z;
}
