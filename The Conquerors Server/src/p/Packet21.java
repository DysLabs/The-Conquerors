package p;

/**
 * Packet 21 Set Owner clientbound
 *
 * @author sn
 *
 */
public class Packet21 extends Packet {
	@PacketField
	private final int id = 21;
	@PacketField
	private String ownerSpatialID;
	@PacketField
	private String spatialID;
}
