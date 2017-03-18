package org.genius.conquerors.server;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class GeniusOutputStream implements DataOutput {
	private DataOutputStream out;
	public GeniusOutputStream(OutputStream os) {
		this.out=new DataOutputStream(os);
	}
	
	@Override
	public void write(int arg0) throws IOException {
		out.write(arg0);
	}
	
	public void writeChar(int c) throws IOException {
		writeShort((short)c);
	}
	
	public void writeBoolean(boolean b) throws IOException {
		out.writeBoolean(b);
	}
	
	public void writeByte(int b) throws IOException {
		out.writeByte(b);
	}
	
	public void writeShort(int s) throws IOException {
		out.writeShort(s);
	}
	
	public void writeInt(int i) throws IOException {
		out.writeInt(i);
	}
	
	public void writeLong(long l) throws IOException {
		out.writeLong(l);
	}
	
	public void writeFloat(float f) throws IOException {
		out.writeFloat(f);
	}
	
	public void writeDouble(double d) throws IOException {
		out.writeDouble(d);
	}
	
	public void writeUTF(String s) throws IOException {
		char[] c=s.toCharArray();
		writeInt(c.length);
		for (int i=0;i<c.length;i++) {
			writeChar(c[i]);
		}
	}

	@Override
	public void write(byte[] b) throws IOException {
		out.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
	}

	@Override
	public void writeBytes(String s) throws IOException {
		out.writeBytes(s);
	}

	@Override
	public void writeChars(String s) throws IOException {
		out.writeChars(s);
	}
	
	public void writeVector(float x,float y, float z) throws IOException {
		writeFloat(x);
		writeFloat(y);
		writeFloat(z);
	}
}
