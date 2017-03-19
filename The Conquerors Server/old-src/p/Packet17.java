package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 17 Chat
 * serverbound
 * @author sn
 *
 */
public class Packet17 extends Packet {
	private boolean ally;
	private String message;
	public Packet17(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}

	@Override
	public void write(GeniusOutputStream out) throws IOException {
		out.writeInt(17);//packet id
		out.writeBoolean(ally);
		out.writeUTF(message);
	}

	@Override
	public void read() throws IOException {
		this.ally=in.readBoolean();
		message=in.readLine();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isAlly() {
		return ally;
	}

	public void setAlly(boolean ally) {
		this.ally = ally;
	}

}
