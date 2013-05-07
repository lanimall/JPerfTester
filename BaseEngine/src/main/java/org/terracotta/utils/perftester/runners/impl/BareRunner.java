package org.terracotta.utils.perftester.runners.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.runners.Runner;

public abstract class BareRunner extends BaseRunner implements Runner {
	private static Logger log = LoggerFactory.getLogger(BareRunner.class);

	protected BareRunner(Condition termination) {
		super(termination);

		if(termination == null)
			throw new IllegalArgumentException("Termination object may not be null");
	}

	protected void execute(){
		Object unitOfWorkResult = null;
		long iterationStartTime;

		if(enableDefaultStats()){
			do {
				try {
					iterationStartTime = System.currentTimeMillis();
					unitOfWorkResult = doUnitOfWork();
					stats.add(System.currentTimeMillis() - iterationStartTime);
				} catch (Exception e) {
					log.error("Error during execution of unit of work.", e);
				}
			} while (!termination.isDone(unitOfWorkResult));
		} else {
			do {
				try {
					unitOfWorkResult = doUnitOfWork();
				} catch (Exception e) {
					log.error("Error during execution of unit of work.", e);
				}
			} while (!termination.isDone(unitOfWorkResult));
		}
	}

	public boolean enableDefaultStats(){
		return true;
	}

	public abstract Object doUnitOfWork();
}