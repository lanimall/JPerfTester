package org.terracotta.utils.perftester.runners;

import org.terracotta.utils.perftester.monitoring.Stats;

/**
 * @author Fabien Sanglier
 * 
 */
public interface Runner extends Cloneable, Runnable {
	String getName();
	
	Stats getStats();
	
	Object clone();
	
	void setRunnerDecorator(RunnerDecorator runnerDecorator);
}
