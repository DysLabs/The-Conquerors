package p;

/**
 * Packet 11 Player Look
 *
 * @author sn
 *
 */
public class Packet11 extends Packet {
	@PacketField
	private final int id = 11;
	@PacketField
	private float x, y, z;
}
