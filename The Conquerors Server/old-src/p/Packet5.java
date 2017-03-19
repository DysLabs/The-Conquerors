package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 5 Spawn Entity
 * clientbound
 * @author sn
 *
 */
public class Packet5 extends Packet {
	private String model,material,spatialId;
	public Packet5(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}

	@Override
	public void write(GeniusOutputStream out) throws IOException {
		out.writeInt(5);//packet id
		out.writeUTF(model);
		out.writeUTF(material);
		out.writeUTF(spatialId);
	}

	@Override
	public void read() throws IOException {
		this.model=in.readLine();
		this.material=in.readLine();
		this.spatialId=in.readLine();
	}

}
