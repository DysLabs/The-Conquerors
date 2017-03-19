package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 1397966893 Test Packet
 * serverbound
 * @author sn
 *
 */
public class Packet1397966893 extends Packet {
	private String vl;
	public Packet1397966893(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void write(GeniusOutputStream out) throws IOException {
		out.writeInt(1397966893);//packet id
		out.writeUTF(vl);
	}

	@Override
	public void read() throws IOException {
		this.vl=in.readLine();
	}

	public String getVl() {
		return vl;
	}

	public void setVl(String vl) {
		this.vl = vl;
	}

}
