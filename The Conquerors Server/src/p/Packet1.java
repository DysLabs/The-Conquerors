package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 1 Login Success
 * clientbound
 * @author sn
 *
 */
public class Packet1 extends Packet {
	private byte playerListLength;
	private String[] player;
	public Packet1(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}

	@Override
	public void write() throws IOException {
		out.writeInt(1);//packet id
		out.writeByte(playerListLength);
		for (int i=0;i<playerListLength;i++) {
			out.writeUTF(player[i]);
		}
	}

	@Override
	public void read() throws IOException {
		
	}

}
