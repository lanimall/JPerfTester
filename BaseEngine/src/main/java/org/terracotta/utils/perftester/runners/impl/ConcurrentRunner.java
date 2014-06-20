package org.terracotta.utils.perftester.runners.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.ExecutorsUtil;
import org.terracotta.utils.perftester.monitoring.StatsOperationObserver;
import org.terracotta.utils.perftester.runners.Runner;
import org.terracotta.utils.perftester.runners.RunnerFactory;

/**
 * @author Fabien Sanglier
 * 
 * A multi-threaded Runner that uses a fixed thread pool to execute other Runner Operations, 1 Runner per thread.
 * 
 */
public class ConcurrentRunner extends BaseRunner implements Runner {
	private static Logger log = LoggerFactory.getLogger(ConcurrentRunner.class);

	private final Runner[] operations;
	private ExecutorService executorService;

	private ConcurrentRunner(Runner[] operations) {
		super(null);
		this.operations = operations;
		this.executorService = Executors.newFixedThreadPool(operations.length);
		this.statsOperationObserver = new StatsOperationObserver(operations);
	}

	@Override
	public String getName() {
		return "Parallel Operation Runner";
	}

	@Override
	protected void execute() {
		log.info("Starting " + getName() + " with executor pool size:" + operations.length);

		try {
			submitOperations();
		} catch(Exception e) {
			log.error("Error in processing Pending Events.", e);
		} finally {
			//make sure to wait until all threads are done before moving forward...
			ExecutorsUtil.shutdownAndAwaitTermination(executorService, true);
		}

		log.info("End " + getName());
	}

	private void submitOperations() throws Exception {
		if(null == executorService)
			throw new NullPointerException("The executor service should be created at this point. If not, startProcess() must have been called instead of run()...");

		for (Runner op : operations) {
			executorService.submit(op);
		}
	}

	public static class ConcurrentRunnerFactory implements RunnerFactory {
		private final int numThread;
		private final RunnerFactory runnerFactory;
		private final Runner[] runners;

		public ConcurrentRunnerFactory(int numThread, RunnerFactory runnerFactory) {
			this.numThread = numThread;
			this.runnerFactory = runnerFactory;
			this.runners = null;
		}
		
		public ConcurrentRunnerFactory(Runner[] runners) {
			this.numThread = 0;
			this.runnerFactory = null;
			this.runners = runners;
		}

		@Override
		public ConcurrentRunner create() {
			if(null != runners && runners.length > 0)
				return new ConcurrentRunner(runners);
			else{
				if(null != runnerFactory){
					Runner[] ops = new Runner[numThread];
					for(int i=0 ; i < numThread; i++){
						ops[i]=runnerFactory.create();
					}
					return new ConcurrentRunner(ops);
				}
			}
			return null;
		}
	}
}
