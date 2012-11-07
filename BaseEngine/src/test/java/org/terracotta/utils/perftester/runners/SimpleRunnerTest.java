package org.terracotta.utils.perftester.runners;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.runners.impl.ConcurrentRunner;
import org.terracotta.utils.perftester.runners.impl.SimpleRunner.SimpleIterativeRunnerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class SimpleRunnerTest {
	private static Logger log = LoggerFactory.getLogger(SimpleRunnerTest.class);

	private RunnerFactory createRunnerFactory(int numThreads, long numOperations){
		return new SimpleIterativeRunnerFactory(numThreads, numOperations);
	}
	
	@Test
	public void testStartProcess() throws Exception {
		//get the number of threads and number of customers to create
		int numThreads = 1;
		
		//number of iteration per thread
		long numOperations = 100000000;
		
		ConcurrentRunner runner = ConcurrentRunner.create(createRunnerFactory(numThreads, numOperations));
		runner.run();
		
		assertEquals(numOperations, runner.getStatsOperationObserver().getAggregateStats().getTxnCount());
	}
	
	@Test
	public void testStartProcessMultiThreads() throws Exception {
		//get the number of threads and number of customers to create
		int numThreads = 4;
		
		//number of iteration per thread
		long numOperations = 100000000;
		
		ConcurrentRunner runner = ConcurrentRunner.create(createRunnerFactory(numThreads, numOperations));
		runner.run();
		
		assertEquals(numOperations, runner.getStatsOperationObserver().getAggregateStats().getTxnCount());
	}
}
