package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 2 Login Failure
 * clientbound
 * @author sn
 *
 */
public class Packet2 extends Packet {
	private String reason;
	public Packet2(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}

	@Override
	public void write(GeniusOutputStream out) throws IOException {
		out.writeInt(2);//packet id
		out.writeUTF(reason);
	}

	@Override
	public void read() throws IOException {
		this.reason=in.readUTF();
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
