package p;

/**
 * Packet 2 Login Failure clientbound
 *
 * @author sn
 *
 */
public class Packet2 extends Packet {
	@PacketField
	private final int id = 2;
	@PacketField
	private String reason;
}
