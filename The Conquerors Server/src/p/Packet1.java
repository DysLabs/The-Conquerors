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
	public void write(GeniusOutputStream out) throws IOException {
		out.writeInt(1);//packet id
		out.writeByte(playerListLength);
		for (int i=0;i<playerListLength;i++) {
			out.writeUTF(player[i]);
		}
	}

	@Override
	public void read() throws IOException {
		this.playerListLength=in.readByte();
		this.player=new String[this.playerListLength];
		for (int i=0;i<this.playerListLength;i++) {
			this.player[i]=in.readUTF();
		}
	}
	
	public byte getPlayerListLength() {
		return this.playerListLength;
	}
	public void setPlayerListLength(byte playerListLength) {
		this.playerListLength=playerListLength;
	}

	public String[] getPlayer() {
		return player;
	}

	public void setPlayer(String[] player) {
		this.player = player;
	}
}
