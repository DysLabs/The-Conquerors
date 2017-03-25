package p;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Packet {
	public String[] getFields() {
		Field[] fields = this.getClass().getDeclaredFields();
		ArrayList<String> packetFields = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().startsWith("p_")) {
				packetFields.add(fields[i].getName());
			}
		}
		String[] pfs = new String[packetFields.size()];
		pfs = packetFields.toArray(pfs);
		return pfs;
	}

	public String[] getFieldTypes() {
		Field[] fields = this.getClass().getDeclaredFields();
		ArrayList<String> packetFields = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().startsWith("p_")) {
				packetFields.add(c(fields[i].getType().getSimpleName().toLowerCase()).replace("[]", "Array").trim());
			}
		}
		String[] pfs = new String[packetFields.size()];
		pfs = packetFields.toArray(pfs);
		return pfs;
	}

	public Object[] getFieldValues() throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = this.getClass().getDeclaredFields();
		ArrayList<Object> packetFields = new ArrayList<Object>();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().startsWith("p_")) {
				boolean accessible = fields[i].isAccessible();
				fields[i].setAccessible(true);
				packetFields.add(fields[i].get(this));
				fields[i].setAccessible(accessible);
			}
		}
		Object[] pfs = new Object[packetFields.size()];
		pfs = packetFields.toArray(pfs);
		return pfs;
	}

	public <T> T getField(String field) throws IllegalArgumentException, IllegalAccessException {
		String[] fields = getFields();
		Object[] values = getFieldValues();
		for (int i = 0; i < fields.length; i++) {
			String f = fields[i];
			Object v = values[i];
			if (f.equals("p_" + field)) {
				return (T) v;
			}
		}
		throw new IllegalArgumentException("Packet" + getPacketID() + " does not have field" + field);
	}

	public int getPacketID() throws IllegalArgumentException, IllegalAccessException {
		return this.<Integer>getField("id");
	}

	public boolean initialized() throws IllegalArgumentException, IllegalAccessException {
		boolean init = true;
		Object[] fields = getFieldValues();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i] == null) {
				init = false;
			}
		}
		return init;
	}

	private String c(final String line) {
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}

	/**
	 * Do not include the "p_" prefix
	 * 
	 * @param field
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public <T> void set(String field, T value)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = this.getClass().getDeclaredField("p_" + field);
		boolean accessible = f.isAccessible();
		f.setAccessible(true);
		f.set(this, value);
		f.setAccessible(accessible);
	}

	public String toString() {
		String s = "Packet[";
		try {
			String[] fields = getFields();
			String[] fieldTypes = getFieldTypes();
			Object[] values = getFieldValues();
			for (int i = 0; i < fields.length; i++) {
				String f = fields[i];
				String t = fieldTypes[i];
				Object v = values[i];
				s = s + f + "(" + t.toLowerCase() + ")=" + v + "; ";
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return s + "NULL";
		}
		return s + "]";
	}
}
