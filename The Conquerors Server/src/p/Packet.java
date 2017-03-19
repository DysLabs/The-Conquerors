package p;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Packet {
	public String[] getFields() {
		Field[] fields=this.getClass().getDeclaredFields();
		ArrayList<String> packetFields=new ArrayList<String>();
		for (int i=0;i<fields.length;i++) {
			if (fields[i].getName().startsWith("p_")) {
				packetFields.add(fields[i].getName());
			}
		}
		String[] pfs=new String[packetFields.size()];
		pfs=packetFields.toArray(pfs);
		return pfs;
	}
	
	public String[] getFieldTypes() {
		Field[] fields=this.getClass().getDeclaredFields();
		ArrayList<String> packetFields=new ArrayList<String>();
		for (int i=0;i<fields.length;i++) {
			if (fields[i].getName().startsWith("p_")) {
				packetFields.add(c(fields[i].getType().getSimpleName().toLowerCase()).replace("[]", "Array").trim());
			}
		}
		String[] pfs=new String[packetFields.size()];
		pfs=packetFields.toArray(pfs);
		return pfs;
	}
	
	public Object[] getFieldValues() throws IllegalArgumentException, IllegalAccessException {
		Field[] fields=this.getClass().getDeclaredFields();
		ArrayList<Object> packetFields=new ArrayList<Object>();
		for (int i=0;i<fields.length;i++) {
			if (fields[i].getName().startsWith("p_")) {
				boolean accessible=fields[i].isAccessible();
				fields[i].setAccessible(true);
				packetFields.add(fields[i].get(this));
				fields[i].setAccessible(accessible);
			}
		}
		Object[] pfs=new Object[packetFields.size()];
		pfs=packetFields.toArray(pfs);
		return pfs;
	}
	
	public boolean initialized() throws IllegalArgumentException, IllegalAccessException {
		boolean init=true;
		Object[] fields=getFieldValues();
		for (int i=0;i<fields.length;i++) {
			if (fields[i]==null) {
				init=false;
			}
		}
		return init;
	}
	
	private String c(final String line) {
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
	
	/**
	 * Do not include the "p_" prefix
	 * @param field
	 * @param value
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public <T> void set(String field,T value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f=this.getClass().getDeclaredField("p_"+field);
		boolean accessible=f.isAccessible();
		f.setAccessible(true);
		f.set(this, value);
		f.setAccessible(accessible);
	}
}
