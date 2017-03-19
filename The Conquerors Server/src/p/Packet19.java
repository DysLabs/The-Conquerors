package p;

/**
 * Packet 19 Select Window Slot
 * serverbound
 * @author sn
 *
 */
public class Packet19 extends Packet {
	private final int p_id=19;
	private String p_spatialID;
	private byte p_slot;
}
