package p;

/**
 * Packet 22 Update Player Data clientbound
 *
 * @author sn
 *
 */
public class Packet22 extends Packet {
	@PacketField
	private final int id = 22;
	@PacketField
	private int money;
}
