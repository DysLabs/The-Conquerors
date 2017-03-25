package p;

/**
 * Packet 17 Chat serverbound
 *
 * @author sn
 *
 */
public class Packet17 extends Packet {
	@PacketField
	private final int id = 17;
	@PacketField
	private boolean ally;
	@PacketField
	private String message;
}
