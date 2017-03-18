package org.genius.conquerors.server;

public interface JavaFunction {
	public <T extends Object> T run(Object... args);
}
