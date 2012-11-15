package org.terracotta.utils.perftester.launchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.runners.RunnerFactory;
import org.terracotta.utils.perftester.runners.impl.ConcurrentRunner;
import org.terracotta.utils.perftester.runners.impl.ConcurrentRunner.ConcurrentRunnerFactory;

public class ConcurrentLauncher extends BaseLauncher {
	private static Logger log = LoggerFactory.getLogger(ConcurrentLauncher.class);

	private final int numThreads;
	private final RunnerFactory runnerFactory;

	public ConcurrentLauncher(int numThreads, RunnerFactory runnerFactory) {
		super();
		
		if(log.isInfoEnabled())
			log.info("Threads to use:" + numThreads);
		
		this.numThreads = numThreads;
		this.runnerFactory = runnerFactory;
	}

	@Override
	protected void run() {
		ConcurrentRunner runner = new ConcurrentRunnerFactory(numThreads,runnerFactory).create();
		runner.run();
	}
	
	public RunnerFactory getRunnerFactory() {
		return runnerFactory;
	}

	public int getNumThreads() {
		return numThreads;
	}
}