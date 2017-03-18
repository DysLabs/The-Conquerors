package org.genius.conquerors.server;

public class GameWindow {
	private String[] opts;
	private JavaFunction[] qualifiers;
	private final String windowID;
	
	public GameWindow(String[]prompts,JavaFunction[]qualifiers) {
		opts=prompts;
		this.qualifiers=qualifiers;
		this.windowID=Main.getSpatialID("window");
	}
	
	public int slots(ClientHandler h) {
		int slots=0;
		for (int i=0;i<opts.length;i++) {
			if (qualifiers[i]!=null) {
				if (qualifiers[i].<Boolean>run(h)) {
					slots++;
				}
			} else {
				slots++;
			}
		}
		return slots;
	}
	
	public String[] options(ClientHandler h) {
		String[] options=new String[slots(h)];
		int slot=0;
		for (int i=0;i<opts.length;i++) {
			if (qualifiers[i]!=null) {
				if (qualifiers[i].<Boolean>run(h)) {
					options[slot]=opts[i];
					slot++;
				}
			} else {
				options[slot]=opts[i];
				slot++;
			}
		}
		return options;
	}
}
