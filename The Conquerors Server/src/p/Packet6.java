package p;

/**
 * Packet 6 Check Model clientbound
 *
 * @author sn
 *
 */
public class Packet6 extends Packet {
	@PacketField
	private final int id = 6;
	@PacketField
	private String model;
}
