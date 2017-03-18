package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 3 Model
 * clientbound
 * @author sn
 *
 */
public class Packet3 extends Packet {
	private String modelName;
	private int modelLength;
	private byte[] model;
	public Packet3(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}

	@Override
	public void write(GeniusOutputStream out) throws IOException {
		out.writeInt(3);//packet id
		out.writeUTF(modelName);
		out.writeInt(modelLength);
		for (int i=0;i<model.length;i++) {
			out.writeByte(model[i]);
		}
	}

	@Override
	public void read() throws IOException {
		this.modelName=in.readLine();
		this.modelLength=in.readInt();
		model=new byte[modelLength];
		for (int i=0;i<modelLength;i++) {
			model[i]=in.readByte();
		}
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public int getModelLength() {
		return modelLength;
	}

	public void setModelLength(int modelLength) {
		this.modelLength = modelLength;
	}

	public byte[] getModel() {
		return model;
	}

	public void setModel(byte[] model) {
		this.model = model;
	}

}
