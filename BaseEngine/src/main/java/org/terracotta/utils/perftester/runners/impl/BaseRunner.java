package org.terracotta.utils.perftester.runners.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.RandomUtil;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.monitoring.Stats;
import org.terracotta.utils.perftester.monitoring.StatsOperationObserver;
import org.terracotta.utils.perftester.runners.Runner;
import org.terracotta.utils.perftester.runners.RunnerDecorator;
import org.terracotta.utils.perftester.runners.RunnerDecoratorDefault;

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
	protected final RandomUtil randomUtil = new RandomUtil();
	protected StatsOperationObserver statsOperationObserver;

	protected Condition termination;
	private RunnerDecorator runnerDecorator;
	
	protected BaseRunner(Condition termination) {
		this(termination, null);
	}
	
	protected BaseRunner(Condition termination, RunnerDecorator runnerDecorator) {
		super();
		this.termination = termination;
		setRunnerDecorator(runnerDecorator);
	}

	@Override
	public void setRunnerDecorator(RunnerDecorator runnerDecorator) {
		if(null == runnerDecorator)
			this.runnerDecorator = new RunnerDecoratorDefault();
		else
			this.runnerDecorator = runnerDecorator;
	}

	@Override
	public String getName(){
		return this.getClass().toString();
	}

	@Override
	public Object clone() {
		try {
			BaseRunner cloned = (BaseRunner)super.clone();
			cloned.stats = new Stats(stats);
			if(this.statsOperationObserver != null)
				cloned.statsOperationObserver = (StatsOperationObserver)this.statsOperationObserver.clone();

			return cloned;
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
	public void run() {
		try {
			//reset stat before execute
			if(runnerDecorator.doResetStatsBeforeRun())
				stats.reset();

			execute();
		} catch(Exception e) {
			log.error("Error in processing Pending Events.", e);
		} finally {
			//finalize stats after execute
			if(runnerDecorator.doFinalizeStatsAfterRun())
				stats.finalise();

			if(null != getStatsOperationObserver())
				stats.add(getStatsOperationObserver().getAggregateStats());

			if(runnerDecorator.doPrintStatsAfterRun())
				stats.printToConsole(runnerDecorator.printStatsHeaderText(getName()),runnerDecorator.printStatsFooterText(getName()));
		}
	}

	protected abstract void execute();
}
