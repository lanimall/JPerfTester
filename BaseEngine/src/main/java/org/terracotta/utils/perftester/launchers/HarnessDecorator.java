package org.terracotta.utils.perftester.launchers;

public interface HarnessDecorator {
	void doBefore() throws Exception;

	void doAfter() throws Exception;
}
