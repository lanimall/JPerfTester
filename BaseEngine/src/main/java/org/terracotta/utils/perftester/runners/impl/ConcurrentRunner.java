package org.terracotta.utils.perftester.runners.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
			shutdownAndAwaitTermination(executorService);
		}

		log.info("End " + getName());
	}

	private void shutdownAndAwaitTermination(ExecutorService pool) {
		pool.shutdown(); // Disable new tasks from being submitted

		try {
			// Wait until existing tasks to terminate
			while(!pool.awaitTermination(5, TimeUnit.SECONDS)){
				System.out.print(".");
			}

			pool.shutdownNow(); // Cancel currently executing tasks

			// Wait a while for tasks to respond to being canceled
			if (!pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS))
				log.error("Pool did not terminate");
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();

			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
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

		public ConcurrentRunnerFactory(int numThread, RunnerFactory runnerFactory) {
			this.numThread = numThread;
			this.runnerFactory = runnerFactory;
		}

		@Override
		public ConcurrentRunner create() {
			Runner[] ops = new Runner[numThread];
			for(int i=0 ; i < numThread; i++){
				ops[i]=runnerFactory.create();
			}
			return new ConcurrentRunner(ops);
		}
	}
}
