package p;

/**
 * Packet 13 Player List clientbound
 *
 * @author sn
 *
 */
public class Packet13 extends Packet {
	@PacketField
	private final int id = 13;
	@PacketField
	private String[] playerNames;
}
