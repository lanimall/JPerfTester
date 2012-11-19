package org.terracotta.utils.perftester.cache.launchers;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.coordination.Barrier;
import org.terracotta.utils.commons.cache.CacheUtils;
import org.terracotta.utils.perftester.cache.runners.CacheRunnerFactory;
import org.terracotta.utils.perftester.launchers.ConcurrentLauncher;
import org.terracotta.utils.perftester.runners.RunnerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public abstract class BaseCacheLauncher extends ConcurrentLauncher {
	private static Logger log = LoggerFactory.getLogger(BaseCacheLauncher.class);
	private Barrier barrier = null;
	private boolean multiClientEnabled = false;
	private int numClients = 1;
	private Cache cache = null;
	
	public BaseCacheLauncher(int numThreads, RunnerFactory runnerFactory) {
		super(numThreads, runnerFactory);
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
	
	public Barrier getBarrier() {
		return barrier;
	}
	
	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public Cache getCache() {
		if(null != cache)
			return cache;
		else
			return (null != getRunnerFactory())?((CacheRunnerFactory)getRunnerFactory()).getCache(): null;
	}
	
	@Override
	public void doBefore() throws Exception {
		super.doBefore();
		log.info("Begin doBefore()");
		
		Cache cache = null;
		if(isMultiClientEnabled() && null != (cache = getCache())){
			log.info("Implementing a thread barrier with name=" + this.getClass().toString() + " and barrier count=" + getNumClients());
			this.barrier = CacheUtils.getBarrier(cache.getCacheManager(), this.getClass().toString(), getNumClients());
		}
		log.info("End doBefore()");
	}
}