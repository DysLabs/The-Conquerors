package io.github.dyslabs.conquerors;

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
	
	public void writeString(String s) throws IOException {
		writeUTF(s);
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
	
	public void writeBoolean(Boolean b) throws IOException {
		writeBoolean(b.booleanValue());
	}
	
	public void writeByte(Byte b) throws IOException {
		writeByte(b.byteValue());
	}
	
	public void writeShort(Short s) throws IOException {
		writeShort(s.shortValue());
	}
	
	public void writeChar(Character c) throws IOException {
		writeChar(c.charValue());
	}
	
	public void writeInt(Integer i) throws IOException {
		writeInt(i.intValue());
	}
	
	public void writeLong(Long l) throws IOException {
		writeLong(l.longValue());
	}
	
	public void writeFloat(Float f) throws IOException {
		writeFloat(f.floatValue());
	}
	
	public void writeDouble(Double d) throws IOException {
		writeDouble(d.doubleValue());
	}
	
	public void writeBooleanArray(Boolean[] b) throws IOException {
		writeInt(b.length);
		for (int i=0;i<b.length;i++) {
			out.writeBoolean(b[i]);
		}
	}
	
	public void writeByteArray(Byte[] b) throws IOException {
		writeInt(b.length);
		for (int i=0;i<b.length;i++) {
			writeByte(b[i]);
		}
	}
	
	public void writeShortArray(Short[] b) throws IOException {
		writeInt(b.length);
		for (int i=0;i<b.length;i++) {
			writeShort(b[i]);
		}
	}
	
	public void writeIntArray(int[] b) throws IOException {
		writeInt(b.length);
		for (int i=0;i<b.length;i++) {
			writeInt(b[i]);
		}
	}
	
	public void writeLongArray(long[] b) throws IOException {
		writeInt(b.length);
		for (int i=0;i<b.length;i++) {
			writeLong(b[i]);
		}
	}
	
	public void writeFloatArray(float[] b) throws IOException {
		writeInt(b.length);
		for (int i=0;i<b.length;i++) {
			writeFloat(b[i]);
		}
	}
	
	public void writeDoubleArray(double[]b) throws IOException {
		writeInt(b.length);
		for (int i=0;i<b.length;i++) {
			writeDouble(b[i]);
		}
	}
	
	public void writeStringArray(String[]b) throws IOException {
		writeInt(b.length);
		for (int i=0;i<b.length;i++) {
			writeString(b[i]);
		}
	}
}
