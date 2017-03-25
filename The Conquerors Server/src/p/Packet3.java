package p;

/**
 * Packet 3 Model clientbound
 *
 * @author sn
 *
 */
public class Packet3 extends Packet {
	@PacketField
	private final int id = 3;
	@PacketField
	private String modelName;
	@PacketField
	private byte[] model;
}
