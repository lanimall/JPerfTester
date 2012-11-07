package org.terracotta.utils.perftester.runners;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.impl.IterationCondition;
import org.terracotta.utils.perftester.generators.impl.SequentialGenerator;
import org.terracotta.utils.perftester.runners.impl.ConcurrentRunner;
import org.terracotta.utils.perftester.runners.impl.RamdomMixRunner.RamdomMixRunnerFactory;
import org.terracotta.utils.perftester.runners.impl.SimpleRunner;

/**
 * @author Fabien Sanglier
 * 
 */
public class RandomMixRunnerTest {
	private static Logger log = LoggerFactory.getLogger(RandomMixRunnerTest.class);

	private RunnerFactory createRunnerFactory(int numThreads, long numOperations){
		RamdomMixRunnerFactory runnerFactory = new RamdomMixRunnerFactory(numThreads, numOperations);
		runnerFactory.addOperationMix(new SimpleRunner(new IterationCondition(1),new SequentialGenerator()){
			@Override
			public String getName() {
				return super.getName() + "1 - 45%";
			}

			@Override
			public void doUnitOfWork(Long key) {
				//log.info("Running " + getName() + " and do something with this key:" + key);
			}

			@Override
			protected boolean resetStatsBeforeEachExecute() {
				return false;
			}

			@Override
			protected boolean printStatsAfterEachExecute() {
				return false;
			}
		}, 45);
		runnerFactory.addOperationMix(new SimpleRunner(new IterationCondition(1),new SequentialGenerator()){
			@Override
			public String getName() {
				return super.getName() + "2 - 35%";
			}

			@Override
			public void doUnitOfWork(Long key) {
				//log.info("Running " + getName() + " and do something with this key:" + key);
			}
			
			@Override
			protected boolean resetStatsBeforeEachExecute() {
				return false;
			}

			@Override
			protected boolean printStatsAfterEachExecute() {
				return false;
			}
		}, 35);
		runnerFactory.addOperationMix(new SimpleRunner(new IterationCondition(1),new SequentialGenerator()){
			@Override
			public String getName() {
				return super.getName() + "3 - 20%";
			}

			@Override
			public void doUnitOfWork(Long key) {
				//log.info("Running " + getName() + " and do something with this key:" + key);
			}
			
			@Override
			protected boolean resetStatsBeforeEachExecute() {
				return false;
			}

			@Override
			protected boolean printStatsAfterEachExecute() {
				return false;
			}
		}, 20);
		
		return runnerFactory;
	}
	
	@Test
	public void testStartProcess() throws Exception {
		//get the number of threads and number of customers to create
		int numThreads = 1;
		
		//number of iteration per thread
		long numOperations = 100000;
		
		ConcurrentRunner runner = ConcurrentRunner.create(createRunnerFactory(numThreads, numOperations));
		runner.run();
		
		assertEquals(numOperations, runner.getStatsOperationObserver().getAggregateStats().getTxnCount());
	}
	
	@Test
	public void testStartProcessMultiThreads() throws Exception {
		//get the number of threads and number of customers to create
		int numThreads = 4;
		
		//number of iteration per thread
		long numOperations = 100000;
		
		ConcurrentRunner runner = ConcurrentRunner.create(createRunnerFactory(numThreads, numOperations));
		runner.run();
		
		assertEquals(numOperations, runner.getStatsOperationObserver().getAggregateStats().getTxnCount());
	}
}
