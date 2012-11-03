package org.terracotta.utils.perftester.cache.launchers;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.cache.runners.CachePutOperation.CachePutOperationFactory;
import org.terracotta.utils.perftester.generators.ObjectGenerator;

/**
 * @author Fabien Sanglier
 * 
 */
public class CachePutLauncher extends BaseCacheLauncher {
	private static Logger log = LoggerFactory.getLogger(CachePutLauncher.class);
	private final boolean bulkLoadEnabled;
	private final boolean removeAllEntriesFirst;
	
	public CachePutLauncher(Cache cache, int numThreads, long numOperations, ObjectGenerator valueGenerator, long keyStart, boolean bulkLoadEnabled, boolean removeAllFirst) {
		super(new CachePutOperationFactory(cache, numThreads, numOperations, valueGenerator, keyStart));

		this.bulkLoadEnabled = bulkLoadEnabled;
		this.removeAllEntriesFirst = removeAllFirst;

		if(log.isInfoEnabled()){
			log.info("*********** Loading up the cache *************");
			log.info("Params:");
			log.info("Number of Loader Threads: " + numThreads);
			log.info("Number of objects to load: " + numOperations);
			log.info("Key number ot start with: " + keyStart);
			log.info("Enable Bulk Load: " + bulkLoadEnabled);
			log.info("Remove all before reloading: " + removeAllFirst);
			log.info("Number of operations per thread: " + (numOperations/numThreads));
		}
	}

	public boolean isBulkLoadEnabled() {
		return bulkLoadEnabled;
	}

	public boolean isRemoveAllEntriesFirst() {
		return removeAllEntriesFirst;
	}

	@Override
	public void doBeforeRun() throws Exception {
		//if requested, putting cache in bulkload mode
		if(isBulkLoadEnabled() && !getCache().isNodeBulkLoadEnabled()){
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
	public void doAfterRun() throws Exception {
		if(null != getBarrier()){
			//wait for all client to reach that point
			log.info("Waiting for all clients to finish bulk loading");
			getBarrier().await();
		}

		//put cache back in consistent mode
		log.info("Restoring cache " + getCache().getName() + " in Consistent Mode.");
		getCache().setNodeBulkLoadEnabled(false);

		getCache().waitUntilClusterBulkLoadComplete();
		log.info("Done loading up the cache!");
	}
}