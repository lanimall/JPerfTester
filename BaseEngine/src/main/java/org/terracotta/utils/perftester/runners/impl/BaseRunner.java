package org.terracotta.utils.perftester.runners.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.RandomUtil;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.monitoring.Stats;
import org.terracotta.utils.perftester.monitoring.StatsOperationObserver;
import org.terracotta.utils.perftester.runners.Runner;

/**
 * @author Fabien Sanglier
 * 
 * An abstract Runner ensuring that a call to run() does execute the doBeforeRun() and doAfterRun() methods.
 * Sub-classes must override the following method:
 *  - execute()
 * Sub-classes should override the following methods, if need be:
 *  - doBeforeLoad(): operations to execute before the Runner execute() method
 *  - doAfterLoad(): operations to execute after the Runner execute() method
 */
public abstract class BaseRunner implements Runner {
	private static Logger log = LoggerFactory.getLogger(BaseRunner.class);

	protected Stats stats = new Stats();
	protected Condition termination;
	protected final RandomUtil randomUtil = new RandomUtil();
	protected StatsOperationObserver statsOperationObserver;
	
	protected BaseRunner(Condition termination) {
		super();
		this.termination = termination;
	}
	
	@Override
	public String getName(){
		return this.getClass().toString();
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}
	
	public StatsOperationObserver getStatsOperationObserver() {
		return statsOperationObserver;
	}

	public void setStatsOperationObserver(
			StatsOperationObserver statsOperationObserver) {
		this.statsOperationObserver = statsOperationObserver;
	}

	public Condition getTermination() {
		return termination;
	}

	public void setTermination(Condition termination) {
		this.termination = termination;
	}
	
	@Override
	public void doBeforeRun() {
		//noop
	}

	@Override
	public void doAfterRun() {
		//noop
	}
	
	@Override
	public void run() {
		try {
			doBeforeRun();
			
			//reset stat before execute
			if(resetStatsBeforeEachExecute())
				stats.reset();
			
			execute();
		} catch(Exception e) {
			log.error("Error in processing Pending Events.", e);
		} finally {
			//finalize stats after execute
			if(resetStatsBeforeEachExecute())
				stats.finalise();
			
			try {
				//make sure we execute the after load
				doAfterRun();
			} catch (Exception e) {
				log.error("Error in during execution of the afterLoad().", e);
			}
			
			if(null != getStatsOperationObserver())
				stats.add(getStatsOperationObserver().getAggregateStats());
			
			if(printStatsAfterEachExecute())
				stats.printToConsole(printStatsHeaderText(),printStatsFooterText());
		}
	}
	
	protected boolean resetStatsBeforeEachExecute(){
		return true;
	}
	
	protected boolean printStatsAfterEachExecute(){
		return true;
	}
	
	protected String printStatsHeaderText(){
		return "Final Stats for:" + getName();
	}
	
	protected String printStatsFooterText(){
		return "";
	}
	
	protected abstract void execute();
}
