package org.terracotta.utils.perftester.cache.launchers;

import net.sf.ehcache.Cache;
import net.sf.ehcache.search.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.cache.runners.CacheSearchOperation.CacheSearchOperationFactory;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.ObjectGeneratorFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class CacheSearchLauncher extends BaseCacheLauncher {
	private static Logger log = LoggerFactory.getLogger(CacheSearchLauncher.class);
	
	public CacheSearchLauncher(Cache cache, int numThreads, long numOperations, ObjectGenerator<Query> queryGenerator) {
		super(numThreads, new CacheSearchOperationFactory(cache, numOperations/numThreads, queryGenerator));
	}
	
	public CacheSearchLauncher(Cache cache, int numThreads, long numOperations, ObjectGeneratorFactory queryGeneratorFactory) {
		this(cache, numThreads, numOperations, (null != queryGeneratorFactory)? queryGeneratorFactory.createObjectGenerator():null);
	}
}