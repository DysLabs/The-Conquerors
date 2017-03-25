package p;

/**
 * Packet 20 Move Units clientbound
 *
 * @author sn
 *
 */
public class Packet20 extends Packet {
	@PacketField
	private final int id = 20;
	@PacketField
	private String[] spatialID;
	@PacketField
	private float x, y, z;
}
