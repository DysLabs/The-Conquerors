package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 0 Login
 * Serverbound
 * @author sn
 *
 */
public class Packet0 extends Packet {
	private String name;
	private int protocolVersion;
	public Packet0(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getProtocolVersion() {
		return protocolVersion;
	}
	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	
	@Override
	public void write(GeniusOutputStream out) throws IOException {
		out.writeInt(0);//packet id
		out.writeUTF(name);
		out.writeInt(protocolVersion);
	}
	
	@Override
	public void read() throws IOException {
		this.name=in.readUTF();
		this.protocolVersion=in.readInt();
	}
	
}
