package org.terracotta.utils.perftester.runners.impl;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
	private final ExecutorService executorService;
	private final CompletionService poolCompletionService;
	private final boolean shutdownPoolAfterExecution;

	private ConcurrentRunner(ExecutorService executorService, Runner[] operations) {
		super(null);
		this.operations = operations;

		if(null == executorService){
			this.executorService = Executors.newCachedThreadPool();
			this.shutdownPoolAfterExecution = true;
		} else {
			this.executorService = executorService;
			this.shutdownPoolAfterExecution = false;
		}

		//wrapping the executor in a CompletionService for easy access to the finished tasks
		this.poolCompletionService = new ExecutorCompletionService(this.executorService);
		this.statsOperationObserver = new StatsOperationObserver(this.operations);
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
			waitForOpCompletion();
		} catch(Exception e) {
			log.error("Error in processing Pending Events.", e);
		} finally {
			if(shutdownPoolAfterExecution){
				//make sure to wait until all threads are done before moving forward...
				ExecutorsUtil.shutdownAndAwaitTermination(executorService, true);
			}
		}

		log.info("End " + getName());
	}

	private void submitOperations() throws Exception {
		if(null == executorService)
			throw new NullPointerException("The executor service should be created at this point. If not, startProcess() must have been called instead of run()...");

		for (Runner op : operations) {
			poolCompletionService.submit(op, "done");
		}
	}

	private void waitForOpCompletion() throws Exception {
		if(null == executorService)
			throw new NullPointerException("The executor service should be created at this point. If not, startProcess() must have been called instead of run()...");

		System.out.println("Waiting for all " + operations.length + " operations to complete...");

		int completedTasks = 0;
		while(completedTasks < operations.length){
			Future fut = null;
			while(null == (fut = poolCompletionService.poll(5, TimeUnit.SECONDS))){
				System.out.print(".");
			}

			//checking that the future is successful
			if("done".equals(fut.get()))
				completedTasks++;
			else
				throw new Exception("The future returned by the operation does not have the expected result.");
		}
	}

	public static class ConcurrentRunnerFactory implements RunnerFactory {
		private final int numThread;
		private final RunnerFactory runnerFactory;
		private final Runner[] runners;
		private final ExecutorService executorService;

		public ConcurrentRunnerFactory(ExecutorService executorService, int numThread, RunnerFactory runnerFactory) {
			this.executorService = executorService;
			this.numThread = numThread;
			this.runnerFactory = runnerFactory;
			this.runners = null;
		}

		public ConcurrentRunnerFactory(ExecutorService executorService, Runner[] runners) {
			this.executorService = executorService;
			this.numThread = 0;
			this.runnerFactory = null;
			this.runners = runners;
		}

		@Override
		public ConcurrentRunner create() {
			if(null != runners && runners.length > 0)
				return new ConcurrentRunner(executorService, runners);
			else{
				if(null != runnerFactory){
					Runner[] ops = new Runner[numThread];
					for(int i=0 ; i < numThread; i++){
						ops[i]=runnerFactory.create();
					}
					return new ConcurrentRunner(executorService, ops);
				}
			}
			return null;
		}
	}
}
