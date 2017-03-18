package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 6 Check Model
 * clientbound
 * @author sn
 *
 */
public class Packet6 extends Packet {
	private String model;
	public Packet6(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}

	@Override
	public void write() throws IOException {
		out.writeInt(6);//packet id
		out.writeUTF(model);
	}

	@Override
	public void read() throws IOException {
		this.model=in.readLine();
	}

}
