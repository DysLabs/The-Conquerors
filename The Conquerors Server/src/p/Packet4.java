package p;

/**
 * Packet 4 Request Model serverbound
 *
 * @author sn
 *
 */
public class Packet4 extends Packet {
	@PacketField
	private final int id = 4;
	@PacketField
	private String model;
}
