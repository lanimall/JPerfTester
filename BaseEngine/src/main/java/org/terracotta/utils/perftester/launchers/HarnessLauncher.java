package org.terracotta.utils.perftester.launchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.runners.Runner;
import org.terracotta.utils.perftester.runners.RunnerFactory;

public class HarnessLauncher {
	private static Logger log = LoggerFactory.getLogger(HarnessLauncher.class);
	private final RunnerFactory runnerFactory;
	private HarnessDecorator harnessLauncherDecorator;

	public HarnessLauncher(RunnerFactory runnerFactory) {
		this(runnerFactory, null);
	}
	
	public HarnessLauncher(RunnerFactory runnerFactory,	HarnessDecorator harnessDecorator) {
		super();
		this.runnerFactory = runnerFactory;

		if(null == harnessDecorator)
			this.harnessLauncherDecorator = new HarnessDecoratorNoOp();
		else
			this.harnessLauncherDecorator = harnessDecorator;
	}

	public void setHarnessLauncherDecorator(HarnessDecorator harnessLauncherDecorator) {
		this.harnessLauncherDecorator = harnessLauncherDecorator;
	}

	public void launch() {
		log.info("Starting launch...");

		//create the runner object
		Runner runner = runnerFactory.create();
		if(null == runner)
			throw new IllegalArgumentException("The runner object may not be null...");

		long start = System.currentTimeMillis();
		try {
			harnessLauncherDecorator.doBefore();
			runner.run();
		} catch(Exception e) {
			log.error("Error in processing runner", e);
		} finally {
			try {
				//make sure we execute the after load
				harnessLauncherDecorator.doAfter();
			} catch (Exception e) {
				log.error("Error in during execution of the doAfter().", e);
			}
			long stop = System.currentTimeMillis();
			log.debug("Launch done in {}ms", stop - start);
		}
	}
}
