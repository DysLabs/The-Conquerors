package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 8 Scale Entity
 * clientbound
 * @author sn
 *
 */
public class Packet8 extends Packet {
	private String spatialId;
	private float x,y,z;
	public Packet8(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}

	@Override
	public void write(GeniusOutputStream out) throws IOException {
		out.writeInt(8);//packet id
		out.writeUTF(spatialId);
		out.writeFloat(x);
		out.writeFloat(y);
		out.writeFloat(z);
	}

	@Override
	public void read() throws IOException {
		this.spatialId=in.readLine();
		this.x=in.readFloat();
		this.y=in.readFloat();
		this.z=in.readFloat();
	}

	public String getSpatialId() {
		return spatialId;
	}

	public void setSpatialId(String spatialId) {
		this.spatialId = spatialId;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

}
