package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 14 Request Window
 * serverbound
 * @author sn
 *
 */
public class Packet14 extends Packet {
	private String spatialId;
	public Packet14(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}

	@Override
	public void write(GeniusOutputStream out) throws IOException {
		out.writeInt(14);
		out.writeUTF(spatialId);
	}

	@Override
	public void read() throws IOException {
		this.spatialId=in.readLine();
	}

	public String getSpatialId() {
		return spatialId;
	}

	public void setSpatialId(String spatialId) {
		this.spatialId = spatialId;
	}

}
