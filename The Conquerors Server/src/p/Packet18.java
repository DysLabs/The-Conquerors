package p;

/**
 * Packet 18 Chat clientbound
 *
 * @author sn
 *
 */
public class Packet18 extends Packet {
	@PacketField
	private final int id = 18;
	@PacketField
	private String sender;
	@PacketField
	private boolean ally;
	@PacketField
	private String message;
}
