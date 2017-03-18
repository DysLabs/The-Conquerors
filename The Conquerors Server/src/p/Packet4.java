package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 4 Request Model
 * serverbound
 * @author sn
 *
 */
public class Packet4 extends Packet {
	private String model;
	public Packet4(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}

	@Override
	public void write() throws IOException {
		out.writeInt(4);//packet id
		out.writeUTF(model);
	}

	@Override
	public void read() throws IOException {
		this.model=in.readLine();
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

}
