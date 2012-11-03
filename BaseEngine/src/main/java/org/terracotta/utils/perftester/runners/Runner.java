package org.terracotta.utils.perftester.runners;

import org.terracotta.utils.perftester.monitoring.Stats;

/**
 * @author Fabien Sanglier
 * 
 */
public interface Runner extends Cloneable, Runnable {
	String getName();
	
	void doBeforeRun();
	
	void doAfterRun();
	
	Stats getStats();
	
	Object clone();
}
