package org.terracotta.utils.perftester.launchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.runners.RunnerFactory;
import org.terracotta.utils.perftester.runners.impl.ConcurrentRunner.ConcurrentRunnerFactory;

public class ConcurrentLauncher extends HarnessLauncher {
	private static Logger log = LoggerFactory.getLogger(ConcurrentLauncher.class);

	public ConcurrentLauncher(int numThreads, RunnerFactory innerRunnerFactory) {
		this(numThreads, innerRunnerFactory, null);
	}
	
	public ConcurrentLauncher(int numThreads, RunnerFactory innerRunnerFactory, HarnessDecorator harnessDecorator) {
		super(new ConcurrentRunnerFactory(numThreads,innerRunnerFactory), harnessDecorator);
	}
}