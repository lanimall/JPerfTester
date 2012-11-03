package org.terracotta.utils.perftester.monitoring;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.runners.Runner;

/**
 * @author Fabien Sanglier
 * 
 */
public class StatsOperationObserver {
	private static Logger logger = LoggerFactory.getLogger(StatsOperationObserver.class);

	private final List<Runner> monitoredOperations = new LinkedList<Runner>();

	public StatsOperationObserver() {
		super();
	}

	public StatsOperationObserver(Collection<? extends Runner> operations) {
		super();
		registerMultiple(operations);
	}

	public void register(Runner operation) {
		logger.debug(operation.getName() + " being observed...");
		monitoredOperations.add(operation);
	}

	public void registerMultiple(Collection<? extends Runner> operations) {
		for(Runner op : operations){
			register(op);
		}
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