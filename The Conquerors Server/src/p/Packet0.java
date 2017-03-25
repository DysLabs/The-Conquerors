package p;

/**
 * Packet 0 Login serverbound
 *
 * @author sn
 *
 */
public class Packet0 extends Packet {
	@PacketField
	private final int id = 0;
	@PacketField
	private String username;
	@PacketField
	private int protocolVersion;
}
