package p;

import java.io.IOException;

import org.genius.conquerors.server.GeniusInputStream;
import org.genius.conquerors.server.GeniusOutputStream;

public class Packet18 extends Packet {
	private String sender;
	private boolean ally;
	private String message;
	public Packet18(GeniusInputStream in, GeniusOutputStream out) {
		super(in, out);
	}

	@Override
	public void write(GeniusOutputStream out) throws IOException {
		out.writeInt(18);//packet id
		out.writeUTF(sender);
		out.writeBoolean(ally);
		out.writeUTF(message);
	}

	@Override
	public void read() throws IOException {
		sender=in.readLine();
		ally=in.readBoolean();
		message=in.readLine();
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public boolean isAlly() {
		return ally;
	}

	public void setAlly(boolean ally) {
		this.ally = ally;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
