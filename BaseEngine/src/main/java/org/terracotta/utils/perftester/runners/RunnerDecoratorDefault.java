package org.terracotta.utils.perftester.runners;

public class RunnerDecoratorDefault implements RunnerDecorator {

	@Override
	public void doBefore() throws Exception {
		//do nothing
	}

	@Override
	public void doAfter() throws Exception {
		//do nothing
	}

	@Override
	public boolean doResetStatsBeforeRun() {
		return true;
	}

	@Override
	public boolean doFinalizeStatsAfterRun() {
		return true;
	}

	@Override
	public boolean doPrintStatsAfterRun() {
		return true;
	}
	
	@Override
	public String printStatsHeaderText(String runnerName){
		return "Final Stats for:" + runnerName;
	}

	@Override
	public String printStatsFooterText(String runnerName){
		return "";
	}
}
