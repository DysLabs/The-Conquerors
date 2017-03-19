package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 16 Disconnect
 * serverbound
 * @author sn
 *
 */
public class Packet16 extends Packet {

	public Packet16(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}

	@Override
	public void write(GeniusOutputStream out) throws IOException {
		out.writeInt(16);//packet id
	}

	@Override
	public void read() throws IOException {
		//empty payload
	}

}
