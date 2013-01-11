package org.terracotta.utils.perftester.launchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.runners.Runner;
import org.terracotta.utils.perftester.runners.impl.RamdomMixRunner.RamdomMixRunnerFactory;

public class RandomMixLauncher extends ConcurrentLauncher {
	private static Logger log = LoggerFactory.getLogger(RandomMixLauncher.class);

	public RandomMixLauncher(int numThreads, long numOperations) {
		super(numThreads, new RamdomMixRunnerFactory(numOperations));
	}
	
	public void addOperationMix(Runner operation, int mix){
		if(getRunnerFactory() instanceof RamdomMixRunnerFactory)
			((RamdomMixRunnerFactory)getRunnerFactory()).addOperationMix(operation, mix);
		else{
			log.warn("The runner factory for this launcher is not the right type");
		}
	}
}