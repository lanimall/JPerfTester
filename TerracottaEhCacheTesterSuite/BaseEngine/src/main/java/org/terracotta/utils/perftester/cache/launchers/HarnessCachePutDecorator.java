package org.terracotta.utils.perftester.cache.launchers;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.launchers.HarnessDecorator;

public class HarnessCachePutDecorator extends HarnessClientSyncDecorator implements HarnessDecorator{
	private static Logger log = LoggerFactory.getLogger(HarnessCachePutDecorator.class);
	
	private final boolean bulkLoadEnabled;
	private final boolean removeAllEntriesFirst;
	
	public HarnessCachePutDecorator(Cache cache, boolean bulkLoadEnabled, boolean removeAllEntriesFirst) {
		this(cache, bulkLoadEnabled, removeAllEntriesFirst, false, 0);
	}

	public HarnessCachePutDecorator(Cache cache, boolean bulkLoadEnabled, boolean removeAllEntriesFirst, boolean multiClientEnabled, int numClients) {
		super(cache, multiClientEnabled, numClients);
		this.bulkLoadEnabled = bulkLoadEnabled;
		this.removeAllEntriesFirst = removeAllEntriesFirst;
	}
	
	public boolean isBulkLoadEnabled() {
		return bulkLoadEnabled;
	}

	public boolean isRemoveAllEntriesFirst() {
		return removeAllEntriesFirst;
	}

	@Override
	public void doBefore() throws Exception {
		super.doBefore();
		
		//if requested, putting cache in bulkload mode
		//!getCache().isNodeBulkLoadEnabled()
		if(isBulkLoadEnabled()){
			log.info("Putting cache " + getCache().getName() + " in Bulk Mode.");
			getCache().setNodeBulkLoadEnabled(true);
		}

		//let's make sure that only one client remove the entries
		if(isRemoveAllEntriesFirst()){
			if(null != getBarrier()){
				log.info("Let's make sure that only one client removes all the entries");
				if(0 == getBarrier().await()){
					log.info("Removing all entries first...");
					getCache().removeAll();
				}
			} else {
				log.info("Removing all entries first...");
				getCache().removeAll();
			}
		}
	}

	@Override
	public void doAfter() throws Exception {
		super.doAfter();
		
		if(null != getBarrier()){
			//wait for all client to reach that point
			log.info("Waiting for all clients to come through");
			getBarrier().await();
		}

		//put cache back in consistent mode
		log.info("Restoring cache " + getCache().getName() + " in Consistent Mode.");
		getCache().setNodeBulkLoadEnabled(false);

		getCache().waitUntilClusterBulkLoadComplete();
		log.info("Done loading up the cache!");
	}
}
