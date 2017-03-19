package org.genius.conquerors.server;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GeniusInputStream implements DataInput {
	private final DataInputStream in;
	
	public GeniusInputStream(InputStream in) {
		this.in=new DataInputStream(in);
	}
	
	public boolean readBoolean() throws IOException {
		return in.readBoolean();
	}
	
	public byte readByte() throws IOException {
		return in.readByte();
	}
	
	public short readShort() throws IOException {
		return in.readShort();
	}
	
	public int readInt() throws IOException {
		return in.readInt();
	}
	
	public long readLong() throws IOException {
		return in.readLong();
	}
	
	public float readFloat() throws IOException {
		return in.readFloat();
	}
	
	public double readDouble() throws IOException {
		return in.readDouble();
	}
	
	public String readUTF() throws IOException {
		int len=readInt();
		char[] c=new char[len];
		for (int i=0;i<len;i++) {
			c[i]=(char)readShort();
		}
		return new String(c);
	}
	
	public char readChar() throws IOException {
		return (char)readShort();
	}

	public int read() throws IOException {
		return in.read();
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		in.readFully(b);
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		in.readFully(b, off, len);
	}

	@Override
	public String readLine() throws IOException {
		return readUTF();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return in.readUnsignedByte();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return in.readUnsignedShort();
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return in.skipBytes(n);
	}
}
