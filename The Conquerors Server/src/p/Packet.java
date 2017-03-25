package p;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Packet {
	private String c(final String line) {
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}

	private boolean fieldHasAnnotation(final Field f) {
		return f.getAnnotation(PacketField.class) != null;
	}

	public <T> T getField(final String field) throws IllegalArgumentException, IllegalAccessException {
		final String[] fields = this.getFields();
		final Object[] values = this.getFieldValues();
		for (int i = 0; i < fields.length; i++) {
			final String f = fields[i];
			final Object v = values[i];
			if (f.equals(field)) {
				return (T) v;
			}
		}
		throw new IllegalArgumentException("Packet" + this.getPacketID() + " does not have field" + field);
	}

	public String[] getFields() {
		final Field[] fields = this.getClass().getDeclaredFields();
		final ArrayList<String> packetFields = new ArrayList<>();
		for (final Field field : fields) {
			if (this.fieldHasAnnotation(field)) {
				packetFields.add(field.getName());
			}
		}
		String[] pfs = new String[packetFields.size()];
		pfs = packetFields.toArray(pfs);
		return pfs;
	}

	public String[] getFieldTypes() {
		final Field[] fields = this.getClass().getDeclaredFields();
		final ArrayList<String> packetFields = new ArrayList<>();
		for (final Field field : fields) {
			if (this.fieldHasAnnotation(field)) {
				packetFields.add(this.c(field.getType().getSimpleName().toLowerCase()).replace("[]", "Array").trim());
			}
		}
		String[] pfs = new String[packetFields.size()];
		pfs = packetFields.toArray(pfs);
		return pfs;
	}

	public Object[] getFieldValues() throws IllegalArgumentException, IllegalAccessException {
		final Field[] fields = this.getClass().getDeclaredFields();
		final ArrayList<Object> packetFields = new ArrayList<>();
		for (final Field field : fields) {
			if (this.fieldHasAnnotation(field)) {
				final boolean accessible = field.isAccessible();
				field.setAccessible(true);
				packetFields.add(field.get(this));
				field.setAccessible(accessible);
			}
		}
		Object[] pfs = new Object[packetFields.size()];
		pfs = packetFields.toArray(pfs);
		return pfs;
	}

	public int getPacketID() throws IllegalArgumentException, IllegalAccessException {
		return this.<Integer>getField("id");
	}

	public boolean initialized() throws IllegalArgumentException, IllegalAccessException {
		boolean init = true;
		final Object[] fields = this.getFieldValues();
		for (final Object field : fields) {
			if (field == null) {
				init = false;
			}
		}
		return init;
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
	public <T> void set(final String field, final T value)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		final Field f = this.getClass().getDeclaredField(field);
		final boolean accessible = f.isAccessible();
		f.setAccessible(true);
		f.set(this, value);
		f.setAccessible(accessible);
	}

	@Override
	public String toString() {
		String s = "Packet{";
		try {
			final String[] fields = this.getFields();
			final String[] fieldTypes = this.getFieldTypes();
			final Object[] values = this.getFieldValues();
			for (int i = 0; i < fields.length; i++) {
				final String f = fields[i];
				final String t = fieldTypes[i];
				final Object v = values[i];
				s = s + f + "(" + t.toLowerCase().replaceFirst("array", "[]") + ")=" + v + "; ";
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return s + "NULL";
		}
		s = s/* .substring(0, s.lastIndexOf("; ")); */;
		return s + "}";
	}
}
