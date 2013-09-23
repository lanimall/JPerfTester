package org.terracotta.utils.perftester.launchers;

public class HarnessDecoratorNoOp implements HarnessDecorator {

	@Override
	public void doBefore() throws Exception {
		//do nothing
	}

	@Override
	public void doAfter() throws Exception {
		//do nothing
	}
}
