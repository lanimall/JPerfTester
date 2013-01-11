package org.terracotta.utils.perftester.launchers;

public interface Launcher {
	void doBefore() throws Exception;
	
	void doAfter() throws Exception;
	
	void launch() throws Exception;
}
