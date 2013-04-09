package org.terracotta.utils.perftester;

public interface LauncherAPI {
	String[] getOptions();
	boolean launch(String input, String[] args) throws Exception;
	boolean isReady();
}
