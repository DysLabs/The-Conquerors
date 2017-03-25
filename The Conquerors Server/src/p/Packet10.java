package p;

/**
 * Packet 10 Player Position serverbound
 *
 * @author sn
 *
 */
public class Packet10 extends Packet {
	@PacketField
	private final int id = 10;
	@PacketField
	private float x, y, z;
}
