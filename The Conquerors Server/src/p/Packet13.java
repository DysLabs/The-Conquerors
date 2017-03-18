package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

/**
 * Packet 13 Player List
 * clientbound
 * @author sn
 *
 */
public class Packet13 extends Packet {
	private byte listCount;
	private String[] list;
	public Packet13(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}

	@Override
	public void write(GeniusOutputStream out) throws IOException {
		out.writeInt(13);//packet id
		out.writeByte(listCount);
		for (int i=0;i<list.length;i++) {
			out.writeUTF(list[i]);
		}
	}

	@Override
	public void read() throws IOException {
		this.listCount=in.readByte();
		list=new String[listCount];
		for (int i=0;i<listCount;i++) {
			list[i]=in.readLine();
		}
	}

	public byte getListCount() {
		return listCount;
	}

	public void setListCount(byte listCount) {
		this.listCount = listCount;
	}

	public String[] getList() {
		return list;
	}

	public void setList(String[] list) {
		this.list = list;
	}

}
