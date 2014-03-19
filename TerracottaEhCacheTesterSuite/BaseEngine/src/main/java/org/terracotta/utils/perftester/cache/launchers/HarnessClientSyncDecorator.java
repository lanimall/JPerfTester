package org.terracotta.utils.perftester.cache.launchers;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.coordination.Barrier;
import org.terracotta.utils.commons.cache.CacheManagerDecorator;
import org.terracotta.utils.perftester.launchers.HarnessDecorator;
import org.terracotta.utils.perftester.launchers.HarnessDecoratorNoOp;

public class HarnessClientSyncDecorator extends HarnessDecoratorNoOp implements HarnessDecorator {
	private static Logger log = LoggerFactory.getLogger(HarnessClientSyncDecorator.class);
	
	private Barrier barrier = null;
	private final boolean multiClientEnabled;
	private final int numClients;
	private final Cache cache;
	
	public HarnessClientSyncDecorator(Cache cache, boolean multiClientEnabled, int numClients) {
		super();
		this.multiClientEnabled = multiClientEnabled;
		this.numClients = numClients;
		this.cache = cache;
	}

	public Barrier getBarrier() {
		return barrier;
	}

	public boolean isMultiClientEnabled() {
		return multiClientEnabled;
	}

	public int getNumClients() {
		return numClients;
	}

	public Cache getCache() {
		return cache;
	}

	@Override
	public void doBefore() throws Exception {
		log.info("Begin doBefore()");
		super.doBefore();
		if(null != cache && multiClientEnabled && numClients > 1){
			log.info("Implementing a thread barrier with name=" + this.getClass().toString() + " and barrier count=" + numClients);
			this.barrier = CacheManagerDecorator.getInstance().getBarrier(this.getClass().toString(), numClients);
		}
		log.info("End doBefore()");
	}
}
