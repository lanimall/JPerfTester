package org.terracotta.utils.perftester.runners;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Executors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.runners.impl.ConcurrentRunner;
import org.terracotta.utils.perftester.runners.impl.ConcurrentRunner.ConcurrentRunnerFactory;
import org.terracotta.utils.perftester.runners.impl.SimpleRunner.SimpleIterativeRunnerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class SimpleRunnerTest {
	private static Logger log = LoggerFactory.getLogger(SimpleRunnerTest.class);

	private OpsCountRunnerFactory createRunnerFactory(long numOperations){
		return new SimpleIterativeRunnerFactory(numOperations);
	}
	
	@Test
	public void testStartProcess() throws Exception {
		//get the number of threads and number of customers to create
		int numThreads = 1;
		
		//number of iteration per thread
		long numTotalOperations = 100000000;
		
		ConcurrentRunner runner = new ConcurrentRunnerFactory(Executors.newFixedThreadPool(numThreads), numThreads,createRunnerFactory(numTotalOperations / numThreads)).create();
		runner.run();
		
		assertEquals(numTotalOperations, runner.getStatsOperationObserver().getAggregateStats().getTxnCount());
	}
	
	@Test
	public void testStartProcessMultiThreads() throws Exception {
		//get the number of threads and number of customers to create
		int numThreads = 4;
		
		//number of iteration per thread
		long numTotalOperations = 100000000;
		
		ConcurrentRunner runner = new ConcurrentRunnerFactory(Executors.newFixedThreadPool(numThreads), numThreads,createRunnerFactory(numTotalOperations / numThreads)).create();
		runner.run();
		
		assertEquals(numTotalOperations, runner.getStatsOperationObserver().getAggregateStats().getTxnCount());
	}
}
