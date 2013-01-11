package org.terracotta.utils.perftester.launchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseLauncher implements Launcher {
	private static Logger log = LoggerFactory.getLogger(BaseLauncher.class);
	
	/*
	 * noop: override if something needed ot be executed before the run
	 */
	@Override
	public void doBefore() throws Exception {
		//noop
	}

	/*
	 * noop: override if something needed ot be executed after the run
	 */
	@Override
	public void doAfter() throws Exception {
		//noop
	}

	@Override
	public void launch() throws Exception {
		log.info("Starting launch...");

		long start = System.currentTimeMillis();
		try {
			doBefore();
			run();
		} catch(Exception e) {
			log.error("Error in processing runner", e);
		} finally {
			try {
				//make sure we execute the after load
				doAfter();
			} catch (Exception e) {
				log.error("Error in during execution of the doAfter().", e);
			}
			long stop = System.currentTimeMillis();
			log.debug("Launch done in {}ms", stop - start);
		}
	}
	
	protected abstract void run();
}
