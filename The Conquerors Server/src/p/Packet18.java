package p;

/**
 * Packet 18 Chat
 * clientbound
 * @author sn
 *
 */
public class Packet18 extends Packet {
	private final int p_id=18;
	private String p_sender;
	private boolean p_ally;
	private String p_message;
}
