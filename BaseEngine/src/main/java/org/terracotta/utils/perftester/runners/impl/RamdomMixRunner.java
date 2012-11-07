package org.terracotta.utils.perftester.runners.impl;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.conditions.impl.IterationCondition;
import org.terracotta.utils.perftester.monitoring.StatsOperationObserver;
import org.terracotta.utils.perftester.runners.Runner;
import org.terracotta.utils.perftester.runners.RunnerFactory;

/**
 * @author Fabien Sanglier
 *
 */
public class RamdomMixRunner extends BaseRunner {
	private static Logger log = LoggerFactory.getLogger(RamdomMixRunner.class);
	private StatsOperationObserver statsOperationObserver;
	private static final int OPS_COUNT_WINDOW = 100;

	private final Runner[] operations;
	private final Integer[] randomOperationsPercentMix;

	private final List<OperationsCounter> opsIterationsCountDown;

	/**
	 * The arrays operations and randomOperationMix work together in order to define which run ratio each operation should follow.
	 * In other words, both array should be the same length, and each index should correlate between both array to give a random operation mix ratio for each operation
	 * 
	 * @param termination
	 * @param operations
	 * @param randomOperationMix
	 */
	public RamdomMixRunner(Condition termination, Runner[] operations, Integer[] randomOperationMix) {
		super(termination);
		if(null == operations || null == randomOperationMix || operations.length != randomOperationMix.length)
			throw new IllegalArgumentException("Arguments operations and/or randomOperationMix are not valid. Make sure the size of both collection is the same.");

		this.operations = operations;
		this.randomOperationsPercentMix = randomOperationMix;

		this.opsIterationsCountDown = new LinkedList<OperationsCounter>();
		resetCounterOperationMix();

		this.statsOperationObserver = new StatsOperationObserver(operations);
	}

	private void resetCounterOperationMix(){
		opsIterationsCountDown.clear();
		for(int i=0; i<randomOperationsPercentMix.length; i++){
			opsIterationsCountDown.add(new OperationsCounter(i, OPS_COUNT_WINDOW * randomOperationsPercentMix[i] / 100));
		}
	}

	private void decrementAndGetCountOperation(int index){
		opsIterationsCountDown.get(index).decrementCounter();
		if(opsIterationsCountDown.get(index).isDone()){
			opsIterationsCountDown.remove(index);
		}
	}

	@Override
	public String getName() {
		return "Random Operation Mix Runner";
	}

	@Override
	protected void execute() {
		log.info("Starting run for " + getName());

		try {
			do {
				try {
					pickAndRunOperation();
				} catch (Exception e) {
					log.error("Error during execution of the operation", e);
				}
			} while (!termination.isDone());
		} catch(Exception e) {
			log.error("Error in processing Pending Events.", e);
		} finally {
			//stats.add(getStatsOperationObserver().getAggregateStats());
		}

		log.info("End run for " + getName());
	}

	private void pickAndRunOperation(){
		//if the opsIterationsCountDown is empty, it's time to reset
		if(opsIterationsCountDown.size() == 0)
			resetCounterOperationMix();
		
		//generate random number
		int opsIndex = randomUtil.generateRandom(opsIterationsCountDown.size());
		
		//find the operation pair in the opsIterationsCountDown list
		OperationsCounter opCounter = opsIterationsCountDown.get(opsIndex);
		
		//execute the operation related to that pair, through the stored array index
		long iterationStartTime = System.currentTimeMillis();
		operations[opCounter.getOperationIndex()].run();
		stats.add(System.currentTimeMillis() - iterationStartTime);
		
		//decrement the counter
		decrementAndGetCountOperation(opsIndex);
	}
	
	private static class OperationsCounter {
		private final int operationIndex;
		private int operationCounter;
		
		public OperationsCounter(int operationIndex, int counterStart) {
			super();
			this.operationIndex = operationIndex;
			this.operationCounter = counterStart;
		}
		
		public int getOperationIndex() {
			return operationIndex;
		}

		public int decrementCounter(){
			return --operationCounter;
		}
		
		public boolean isDone(){
			return operationCounter <= 0;
		}
	}
	
	public static class RamdomMixRunnerFactory extends RunnerFactory {
		private List<Runner> operations = new LinkedList<Runner>();
		private List<Integer> randomOperationsPercentMix = new LinkedList<Integer>();
		
		public RamdomMixRunnerFactory(int numThreads, long numOperations) {
			super(numThreads, numOperations);
		}

		public void addOperationMix(Runner operation, int mix){
			operations.add(operation);
			randomOperationsPercentMix.add(mix);
		}
		
		@Override
		public RamdomMixRunner create() {
			return new RamdomMixRunner(
					new IterationCondition(getNumOperations() / getNumThreads()), 
					(Runner[])operations.toArray(new Runner[operations.size()]),
					(Integer[])randomOperationsPercentMix.toArray(new Integer[randomOperationsPercentMix.size()]));	
		}
	}
}
