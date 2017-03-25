package p;

/**
 * Packet 1 Login Success clientbound
 *
 * @author sn
 *
 */
public class Packet1 extends Packet {
	@PacketField
	public final int id = 1;
	@PacketField
	private String spatialID;
}
