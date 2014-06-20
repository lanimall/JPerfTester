package org.terracotta.utils.perftester.runners.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.monitoring.StatsOperationObserver;
import org.terracotta.utils.perftester.runners.OpsCountRunnerFactory;
import org.terracotta.utils.perftester.runners.Runner;
import org.terracotta.utils.perftester.runners.RunnerDecoratorDefault;
import org.terracotta.utils.perftester.runners.RunnerFactory;

/**
 * @author Fabien Sanglier
 *
 */
public class AggregateRunner extends BaseRunner {
	private static Logger log = LoggerFactory.getLogger(AggregateRunner.class);
	
	//private final Stats[] operationStats;
	private final Runner[] operations;
	private final Long[] opCounts;

	private final List<OperationsCounter> opsIterationsCountDown;
	
	/**
	 * The arrays operations and randomOperationMix work together in order to define which run ratio each operation should follow.
	 * In other words, both array should be the same length, and each index should correlate between both array to give a random operation mix ratio for each operation
	 * 
	 * @param termination
	 * @param operations
	 * @param randomOperationMix
	 */
	private AggregateRunner(Runner[] operations, Long[] opCounts) {
		super(null);

		if(null == operations || null == opCounts || operations.length != opCounts.length)
			throw new IllegalArgumentException("Arguments operations and/or opCounts are not valid. Make sure the size of both arrays is the same.");

		this.operations = operations;
		this.opCounts = opCounts;
		
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

		//create and initialize 
		this.opsIterationsCountDown = new ArrayList<AggregateRunner.OperationsCounter>(operations.length);
		for(int i=0; i<opCounts.length; i++){
			opsIterationsCountDown.add(new OperationsCounter(i, opCounts[i]));
		}

		//this.operationStats = new Stats[operations.length];
		//resetOperationsStats();

		this.statsOperationObserver = new StatsOperationObserver(operations);
	}

	private void decrementAndGetCountOperation(int index){
		opsIterationsCountDown.get(index).decrementCounter();
		if(opsIterationsCountDown.get(index).isDone()){
			opsIterationsCountDown.remove(index);
		}
	}

	@Override
	public String getName() {
		return "Aggregate Operation Mix Runner";
	}

	@Override
	protected void execute() {
		log.info("Starting run for " + getName());

		if(log.isDebugEnabled()){
			log.debug(getName());
			for(int i=0; i<operations.length; i++){
				log.debug(operations[i].getName() + ":" + opCounts[i]);
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
			} while (opsIterationsCountDown.size() > 0); //if the opsIterationsCountDown is empty, it's done
		} catch(Exception e) {
			log.error("Error in processing Pending Events.", e);
		} finally {
			getStatsOperationObserver().finalizeMonitoredOpsStats();

			getStatsOperationObserver().printMonitoredOpsStats();
		}

		log.info("End run for " + getName());
	}

	private void pickAndRunOperation(){
		//generate random number
		int opsIndex = randomUtil.generateRandomInt(opsIterationsCountDown.size());

		//find the operation pair in the opsIterationsCountDown list
		OperationsCounter opCounter = opsIterationsCountDown.get(opsIndex);

		//execute the operation related to that pair, through the stored array index
		//long iterationStartTime = System.currentTimeMillis();
		operations[opCounter.getOperationIndex()].run();
		//operationStats[opCounter.getOperationIndex()].add(System.currentTimeMillis() - iterationStartTime);

		//decrement the counter
		decrementAndGetCountOperation(opCounter.getOperationIndex());
	}
	
	private static class OperationsCounter {
		private final int operationIndex;
		private long operationCounter;

		public OperationsCounter(int operationIndex, long counterStart) {
			super();
			this.operationIndex = operationIndex;
			this.operationCounter = counterStart;
		}

		public int getOperationIndex() {
			return operationIndex;
		}

		public long decrementCounter(){
			return --operationCounter;
		}

		public boolean isDone(){
			return operationCounter <= 0;
		}
	}

	public static class AggregateRunnerFactory extends OpsCountRunnerFactory {
		private List<RunnerFactory> operationFactories = new ArrayList<RunnerFactory>();
		private List<Long> operationCounts = new ArrayList<Long>();
		
		public AggregateRunnerFactory() {
			super();
		}

		public void addOperationMix(RunnerFactory operationFactory, long opCount){
			if(null != operationFactory && opCount > 0){
				operationFactories.add(operationFactory);
				operationCounts.add(opCount);
			} else {
				log.info("Not adding the operation mix because either the mix or the operation is null");
			}
		}

		@Override
		public AggregateRunner create() {
			//make sure the operations are cloned properly between each create()
			Runner[] ops = new Runner[operationFactories.size()];
			for(int i=0; i<operationFactories.size(); i++){
				ops[i] = (Runner)operationFactories.get(i).create();
			}

			return new AggregateRunner(
					ops,
					(Long[])operationCounts.toArray(new Long[operationCounts.size()]));	
		}
	}
}
