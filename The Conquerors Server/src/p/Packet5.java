package p;

/**
 * Packet 5 Spawn Entity clientbound
 *
 * @author sn
 *
 */
public class Packet5 extends Packet {
	@PacketField
	private final int id = 5;
	@PacketField
	private String model, material, spatialID;
}
