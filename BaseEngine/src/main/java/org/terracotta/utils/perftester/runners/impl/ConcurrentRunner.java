package org.terracotta.utils.perftester.runners.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.monitoring.Stats;
import org.terracotta.utils.perftester.monitoring.StatsOperationObserver;
import org.terracotta.utils.perftester.runners.Runner;
import org.terracotta.utils.perftester.runners.RunnerFactory;

/**
 * @author Fabien Sanglier
 * 
 * A multi-threaded Runner that uses a fixed thread pool to execute other Runner Operations, 1 Runner per thread.
 * 
 */
public class ConcurrentRunner implements Runner {
	private static Logger log = LoggerFactory.getLogger(ConcurrentRunner.class);

	private final Collection<? extends Runner> operations;
	private ExecutorService executorService;
	private StatsOperationObserver statsOperationObserver;
	private Stats stats = new Stats();

	public static ConcurrentRunner create(RunnerFactory runnerFactory){
		ArrayList<Runner> ops = new ArrayList<Runner>(runnerFactory.getNumThreads());
		for(int i=0 ; i < runnerFactory.getNumThreads(); i++){
			ops.add(runnerFactory.create());
		}
		return new ConcurrentRunner(ops);
	}

	public ConcurrentRunner(Collection<? extends Runner> operations) {
		this.operations = operations;
		this.executorService = Executors.newFixedThreadPool(operations.size());
		this.statsOperationObserver = new StatsOperationObserver(operations);
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public StatsOperationObserver getStatsOperationObserver() {
		return statsOperationObserver;
	}

	public Stats getStats() {
		return stats;
	}

	@Override
	public String getName() {
		return "Parallel Operation Runner";
	}

	@Override
	public void run() {
		log.info("Starting parallel runner with executor pool size:" + operations.size());

		Date startDateTime = new Date();
		try {
			doBeforeRun();
			submitOperations();
		} catch(Exception e) {
			log.error("Error in processing Pending Events.", e);
		} finally {
			//make sure to wait until all threads are done before moving forward...
			shutdownAndAwaitTermination(executorService);

			try {
				//make sure we execute the after load
				doAfterRun();
			} catch (Exception e) {
				log.error("Error in during execution of the afterLoad().", e);
			}

			log.info("All tasks have been processed ...");
			Date endDateTime = new Date();
			long totalOperationTime = endDateTime.getTime() - startDateTime.getTime();
			log.info("Start time: " + startDateTime.toString());
			log.info("End time: " + endDateTime.toString());
			log.info("Operation Duration time (ms): " + totalOperationTime);

			stats.add(getStatsOperationObserver().getAggregateStats());
			log.info("Total operation count: " + getStats().getTxnCount());
			log.info("Throughput: " + getStats().getThroughput());
			log.info("Average Operation Latency: " + getStats().getAvgLatency());
			log.info("Min Operation Latency: " + getStats().getMinLatency());
			log.info("Max Operation Latency: " + getStats().getMaxLatency());
			log.info("Parallel runner execution complete!");
		}
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

	@Override
	public void doBeforeRun() {
		//do nothing
	}

	@Override
	public void doAfterRun() {
		//do nothing
	}
}
