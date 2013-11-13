package org.terracotta.utils.perftester.runners;

public interface RunnerDecorator {
	void doBefore() throws Exception;

	void doAfter() throws Exception;
	
	boolean doResetStatsBeforeRun();
	
	boolean doFinalizeStatsAfterRun();
	
	boolean doPrintStatsAfterRun();
	
	String printStatsHeaderText(String runnerName);
	
	String printStatsFooterText(String runnerName);
}
