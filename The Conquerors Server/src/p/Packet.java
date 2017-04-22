package p;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;

import io.github.dyslabs.conquerors.Main;

public class Packet {
	/**
	 * This method differs in that it only takes the field values, not the names
	 *
	 * @param pid
	 * @param fieldValues
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchFieldException
	 */
	public static Packet c(final int pid, final Object... fieldValues)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Class<?> clazz;
		try {
			clazz = Class.forName("p.Packet" + pid);
		} catch (final ClassNotFoundException e) {
			Main.out.log(Level.SEVERE, "Attempted to craft non-existant Packet" + pid, e);
			return null;
		}
		final Constructor<?> ctor = clazz.getConstructor();
		final Packet p = (Packet) ctor.newInstance();
		final String[] fields = p.getFields();
		if (fieldValues.length != (fields.length - 1)) {
			Main.out.severe("Fields and fieldValues mismatched");
			return null;
		}
		for (int i = 0; i < fields.length; i++) {
			final String f = fields[i];
			if (f.equals("id")) {
				continue;
			}
			final Object v = fieldValues[i - 1];
			p.set(f, v);
		}
		return p;
	}

	/**
	 *
	 * @param pid
	 * @param fields
	 *            format of fieldName(String),value(T)
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchFieldException
	 */
	public static Packet craftPacket(final int pid, final Object... fields)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Class<?> clazz;
		try {
			clazz = Class.forName("p.Packet" + pid);
		} catch (final ClassNotFoundException e) {
			Main.out.log(Level.SEVERE, "Attempted to craft non-existant Packet" + pid, e);
			return null;
		}
		final Constructor<?> ctor = clazz.getConstructor();
		final Packet p = (Packet) ctor.newInstance();
		for (int i = 0; i < fields.length; i++) {
			final String f = (String) fields[i];
			final Object v = fields[i + 1];
			p.set(f, v);
			i++;// fields[i+1] is the value, we need to skip it
		}
		return p;
	}

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
		throw new IllegalArgumentException("Packet" + this.getPacketID() + " does not have field " + field);
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
				Object v = values[i];
				final String type = t;
				if (type.contains("Array")&&!type.contains("Byte")) {
					final Object[] arr = (Object[]) v;
					final StringBuilder sb = new StringBuilder();
					sb.append(t.replaceAll("Array", "["));
					for (final Object element : arr) {
						sb.append(element).append("; ");
					}
					if (sb.lastIndexOf("; ") != -1) {
						v = sb.toString().substring(0, sb.lastIndexOf("; ")) + "]";
					} else {
						v = sb.toString() + "]";
					}
				}
				s = s + f + "(" + type.replaceFirst("Array", "[]") + ")=" + v + "; ";
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return s + "NULL";
		}
		s = s.substring(0, s.lastIndexOf("; "));
		return s + "}";
	}
}
