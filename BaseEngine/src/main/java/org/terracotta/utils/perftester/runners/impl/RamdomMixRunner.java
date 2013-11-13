package org.terracotta.utils.perftester.runners.impl;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.conditions.impl.IterationCondition;
import org.terracotta.utils.perftester.monitoring.StatsOperationObserver;
import org.terracotta.utils.perftester.runners.OpsCountRunnerFactory;
import org.terracotta.utils.perftester.runners.Runner;
import org.terracotta.utils.perftester.runners.RunnerDecoratorDefault;
import org.terracotta.utils.perftester.runners.RunnerFactory;

/**
 * @author Fabien Sanglier
 *
 */
public class RamdomMixRunner extends BaseRunner {
	private static Logger log = LoggerFactory.getLogger(RamdomMixRunner.class);
	private static final int OPS_COUNT_WINDOW = 100;

	//private final Stats[] operationStats;
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
	private RamdomMixRunner(Condition termination, Runner[] operations, Integer[] randomOperationMix) {
		super(termination);

		if(termination == null)
			throw new IllegalArgumentException("Termination object may not be null");

		if(null == operations || null == randomOperationMix || operations.length != randomOperationMix.length)
			throw new IllegalArgumentException("Arguments operations and/or randomOperationMix are not valid. Make sure the size of both collection is the same.");

		this.operations = operations;
		
		//TODO: cleanup the null operations in the array...as well as the operations corresponding to a mix = 0
		
		//ensures that all the inner operations are not resetting / finalizing stats
		for(Runner op : operations){
			op.setRunnerDecorator(new RunnerDecoratorDefault() {
				@Override
				public boolean doResetStatsBeforeRun() {
					return false;
				}
				
				@Override
				public boolean doPrintStatsAfterRun() {
					return false;
				}
				
				@Override
				public boolean doFinalizeStatsAfterRun() {
					return false;
				}
			});
		}

		this.randomOperationsPercentMix = randomOperationMix;

		this.opsIterationsCountDown = new LinkedList<OperationsCounter>();
		resetCounterOperationMix();

		//this.operationStats = new Stats[operations.length];
		//resetOperationsStats();

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

		if(log.isInfoEnabled()){
			log.info("Ramdom Operation Mix:");
			for(int i=0; i<operations.length; i++){
				log.info(operations[i].getName() + " - " + randomOperationsPercentMix[i] + "%");
			}
		}

		getStatsOperationObserver().resetMonitoredOpsStats();

		try {
			do {
				try {
					pickAndRunOperation();
				} catch (Exception e) {
					log.error("Error during execution of the operation", e);
				}
			} while (!termination.isDone(null));
		} catch(Exception e) {
			log.error("Error in processing Pending Events.", e);
		} finally {
			getStatsOperationObserver().finalizeMonitoredOpsStats();

			getStatsOperationObserver().printMonitoredOpsStats();
		}

		log.info("End run for " + getName());
	}

	private void pickAndRunOperation(){
		//if the opsIterationsCountDown is empty, it's time to reset
		if(opsIterationsCountDown.size() == 0)
			resetCounterOperationMix();

		//generate random number
		int opsIndex = randomUtil.generateRandomInt(opsIterationsCountDown.size());

		//find the operation pair in the opsIterationsCountDown list
		OperationsCounter opCounter = opsIterationsCountDown.get(opsIndex);

		//execute the operation related to that pair, through the stored array index
		//long iterationStartTime = System.currentTimeMillis();
		operations[opCounter.getOperationIndex()].run();
		//operationStats[opCounter.getOperationIndex()].add(System.currentTimeMillis() - iterationStartTime);

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

	public static class RamdomMixRunnerFactory extends OpsCountRunnerFactory {
		private List<RunnerFactory> operationFactories = new LinkedList<RunnerFactory>();
		private List<Integer> randomOperationsPercentMix = new LinkedList<Integer>();
		private int totalMix = 0;
		
		public RamdomMixRunnerFactory(long numOperations) {
			super(numOperations);
		}

		public void addOperationMix(RunnerFactory operationFactory, int mix){
			if(null != operationFactory && mix > 0){
				if((totalMix+=mix) > 100)
					throw new IllegalArgumentException("The total mix is higher than 100%. Please check the mix values.");
				
				operationFactories.add(operationFactory);
				randomOperationsPercentMix.add(mix);
			} else {
				log.info("Not adding the operation mix because either the mix or the operation is null");
			}
		}

		@Override
		public RamdomMixRunner create() {
			//make sure the operations are cloned properly between each create()
			Runner[] ops = new Runner[operationFactories.size()];
			for(int i=0; i<operationFactories.size(); i++){
				ops[i] = (Runner)operationFactories.get(i).create();
			}

			return new RamdomMixRunner(
					new IterationCondition(getNumOperations()), 
					ops,
					(Integer[])randomOperationsPercentMix.toArray(new Integer[randomOperationsPercentMix.size()]));	
		}
	}
}
