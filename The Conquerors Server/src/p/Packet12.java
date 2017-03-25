package p;

/**
 * Packet 12 Remove Entity clientbound
 *
 * @author sn
 *
 */
public class Packet12 extends Packet {
	@PacketField
	private final int id = 12;
	@PacketField
	private String spatialID;
}
