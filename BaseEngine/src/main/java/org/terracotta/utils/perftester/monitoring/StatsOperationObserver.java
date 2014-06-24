package org.terracotta.utils.perftester.monitoring;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.runners.Runner;

/**
 * @author Fabien Sanglier
 * 
 */
public class StatsOperationObserver implements Cloneable {
	private static Logger logger = LoggerFactory.getLogger(StatsOperationObserver.class);

	private final List<Runner> monitoredOperations = new ArrayList<Runner>();

	public StatsOperationObserver() {
		super();
	}

	public StatsOperationObserver(Runner[] operations) {
		super();
		registerMultiple(operations);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public void register(Runner operation) {
		logger.debug(operation.getName() + " being observed...");
		monitoredOperations.add(operation);
	}

	public void registerMultiple(Runner[] operations) {
		for(Runner op : operations){
			register(op);
		}
	}

	public void resetMonitoredOpsStats(){
		for(int i=0; i<monitoredOperations.size(); i++){
			monitoredOperations.get(i).getStats().reset();
		}
	}
	
	public void finalizeMonitoredOpsStats(){
		for(int i=0; i<monitoredOperations.size(); i++){
			monitoredOperations.get(i).getStats().finalise();
		}
	}
	
	public void printMonitoredOpsStats(){
		for(int i=0; i<monitoredOperations.size(); i++){
			monitoredOperations.get(i).getStats().printToConsole(monitoredOperations.get(i).getName());
		}
	}
	
	public Stats getMonitoredOpsStats(int operationIndex){
		return monitoredOperations.get(operationIndex).getStats();
	}
	
	public Stats getAggregateStats() {
		logger.info("Entering getAggregateStats...");
		Stats overallStats = new Stats();
		for (Runner op : monitoredOperations) {
			overallStats.add(op.getStats());
		}
		return overallStats;
	}
}