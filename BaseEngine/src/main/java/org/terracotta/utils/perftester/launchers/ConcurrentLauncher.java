package org.terracotta.utils.perftester.launchers;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.runners.RunnerFactory;
import org.terracotta.utils.perftester.runners.impl.ConcurrentRunner.ConcurrentRunnerFactory;

public class ConcurrentLauncher extends HarnessLauncher {
	private static Logger log = LoggerFactory.getLogger(ConcurrentLauncher.class);

	public ConcurrentLauncher(ExecutorService executor, int numThreads, RunnerFactory innerRunnerFactory) {
		this(executor, numThreads, innerRunnerFactory, null);
	}
	
	public ConcurrentLauncher(ExecutorService executor, int numThreads, RunnerFactory innerRunnerFactory, HarnessDecorator harnessDecorator) {
		super(new ConcurrentRunnerFactory(executor, numThreads,innerRunnerFactory), harnessDecorator);
	}
}