package org.terracotta.utils.perftester.runners;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.impl.IterationCondition;
import org.terracotta.utils.perftester.generators.impl.SequentialGenerator;
import org.terracotta.utils.perftester.runners.impl.ConcurrentRunner;
import org.terracotta.utils.perftester.runners.impl.ConcurrentRunner.ConcurrentRunnerFactory;
import org.terracotta.utils.perftester.runners.impl.RamdomMixRunner.RamdomMixRunnerFactory;
import org.terracotta.utils.perftester.runners.impl.SimpleRunner;

/**
 * @author Fabien Sanglier
 * 
 */
public class RandomMixRunnerTest {
	private static Logger log = LoggerFactory.getLogger(RandomMixRunnerTest.class);

	private OpsCountRunnerFactory createRunnerFactory(long numOperations){
		RamdomMixRunnerFactory runnerFactory = new RamdomMixRunnerFactory(numOperations);
		runnerFactory.addOperationMix(new SimpleRunner(new IterationCondition(1),new SequentialGenerator()){
			@Override
			public String getName() {
				return super.getName() + "1";
			}

			@Override
			public void doUnitOfWork(Long key) {
				//log.info("Running " + getName() + " and do something with this key:" + key);
			}
		}, 45);
		runnerFactory.addOperationMix(new SimpleRunner(new IterationCondition(1),new SequentialGenerator()){
			@Override
			public String getName() {
				return super.getName() + "2";
			}

			@Override
			public void doUnitOfWork(Long key) {
				//log.info("Running " + getName() + " and do something with this key:" + key);
			}
		}, 35);
		runnerFactory.addOperationMix(new SimpleRunner(new IterationCondition(1),new SequentialGenerator()){
			@Override
			public String getName() {
				return super.getName() + "3";
			}

			@Override
			public void doUnitOfWork(Long key) {
				//log.info("Running " + getName() + " and do something with this key:" + key);
			}
		}, 20);
		
		return runnerFactory;
	}
	
	@Test
	public void testStartProcess() throws Exception {
		//get the number of threads and number of customers to create
		int numThreads = 1;
		
		//number of iteration per thread
		long numTotalOperations = 100000;
		
		ConcurrentRunner runner = new ConcurrentRunnerFactory(numThreads,createRunnerFactory(numTotalOperations / numThreads)).create();
		runner.run();
		
		assertEquals(numTotalOperations, runner.getStatsOperationObserver().getAggregateStats().getTxnCount());
	}
	
	@Test
	public void testStartProcessMultiThreads() throws Exception {
		//get the number of threads and number of customers to create
		int numThreads = 4;
		
		//number of iteration per thread
		long numTotalOperations = 100000;
		
		ConcurrentRunner runner = new ConcurrentRunnerFactory(numThreads,createRunnerFactory(numTotalOperations / numThreads)).create();
		runner.run();
		
		assertEquals(numTotalOperations, runner.getStatsOperationObserver().getAggregateStats().getTxnCount());
	}
}
