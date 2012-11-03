package org.terracotta.utils.perftester.cache.launchers;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.coordination.Barrier;
import org.terracotta.utils.commons.cache.CacheUtils;
import org.terracotta.utils.perftester.cache.runners.CacheRunnerFactory;
import org.terracotta.utils.perftester.runners.impl.ConcurrentRunner;

/**
 * @author Fabien Sanglier
 * 
 */
public abstract class BaseCacheLauncher {
	private static Logger log = LoggerFactory.getLogger(BaseCacheLauncher.class);
	private CacheRunnerFactory runnerFactory;
	private Barrier barrier = null;
	private boolean multiClientEnabled = false;
	private int numClients = 1;
	
	public BaseCacheLauncher(CacheRunnerFactory runnerFactory) {
		super();
		this.runnerFactory = runnerFactory;
	}
	
	public CacheRunnerFactory getRunnerFactory() {
		return runnerFactory;
	}

	public Barrier getBarrier() {
		return barrier;
	}

	public void doBeforeRun() throws Exception {
		//noop
	}
	
	public void doAfterRun() throws Exception {
		//noop
	}
	
	public void run() throws Exception {
		log.info("Starting load operation...");
		
		if(isMultiClientEnabled()){
			this.barrier = CacheUtils.getBarrier(getCache().getCacheManager(), CachePutLauncher.class.toString(), getNumClients());
		}

		long start = System.currentTimeMillis();
		try {
			doBeforeRun();
			run_internal();
		} catch(Exception e) {
			log.error("Error in processing runner", e);
		} finally {
			try {
				//make sure we execute the after load
				doAfterRun();
			} catch (Exception e) {
				log.error("Error in during execution of the doAfterRun().", e);
			}
			long stop = System.currentTimeMillis();
			log.debug("Runner operation done in {}ms", stop - start);
		}
	}

	private void run_internal() {
		ConcurrentRunner runner = ConcurrentRunner.create(runnerFactory);
		runner.run();
	}
	
	public Cache getCache() {
		return (null != getRunnerFactory())?getRunnerFactory().getCache(): null;
	}
	
	public boolean isMultiClientEnabled() {
		return multiClientEnabled;
	}

	public void setMultiClientEnabled(boolean multiClientEnabled) {
		this.multiClientEnabled = multiClientEnabled;
	}

	public int getNumClients() {
		return numClients;
	}

	public void setNumClients(int numClients) {
		this.numClients = numClients;
	}
}