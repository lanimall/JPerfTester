package org.terracotta.utils.perftester.cache.launchers;

import net.sf.ehcache.Cache;
import net.sf.ehcache.search.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.cache.runners.CacheGetOperation.CacheRandomGetOperationFactory;
import org.terracotta.utils.perftester.cache.runners.CachePutOperation.CachePutOperationFactory;
import org.terracotta.utils.perftester.cache.runners.CacheRunnerFactory;
import org.terracotta.utils.perftester.cache.runners.CacheSearchOperation.CacheSearchOperationFactory;
import org.terracotta.utils.perftester.generators.ObjectGeneratorFactory;
import org.terracotta.utils.perftester.generators.impl.SequentialGenerator;
import org.terracotta.utils.perftester.runners.impl.RamdomMixRunner.RamdomMixRunnerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public final class CacheRandomMixLauncher extends BaseCacheLauncher {
	private static Logger log = LoggerFactory.getLogger(CacheRandomMixLauncher.class);
	private int totalMix = 0;
	
	public CacheRandomMixLauncher(Cache cache, int numThreads, long numOperations) {
		super(numThreads, new RamdomMixRunnerFactory(numOperations/numThreads));
		setCache(cache);
	}
	
	public void addCachePutOperationMix(int mix, Cache cache, ObjectGeneratorFactory keyGeneratorFactory, ObjectGeneratorFactory valueGeneratorFactory){
		if((totalMix+=mix) > 100)
			throw new IllegalArgumentException("The total mix is higher than 100%. Please check the mix values.");
		
		CacheRunnerFactory factory = new CachePutOperationFactory(cache, 1, (null != keyGeneratorFactory)? keyGeneratorFactory.createObjectGenerator():null, (null != valueGeneratorFactory)?valueGeneratorFactory.createObjectGenerator():null);
		((RamdomMixRunnerFactory)getRunnerFactory()).addOperationMix(factory.create(), mix);
	}
	
	public void addCacheGetOperationMix(int mix, Cache cache, long keyMinValue, long keyMaxValue){
		if((totalMix+=mix) > 100)
			throw new IllegalArgumentException("The total mix is higher than 100%. Please check the mix values.");
		
		CacheRunnerFactory factory = new CacheRandomGetOperationFactory(cache, 1, keyMinValue, keyMaxValue);
		((RamdomMixRunnerFactory)getRunnerFactory()).addOperationMix(factory.create(), mix);
	}
	
	public void addCacheSearchOperationMix(int mix, Cache cache, Query[] queries){
		addCacheSearchOperationMix(mix, cache, new CacheSearchOperationFactory(cache, 1, queries));
	}
	public void addCacheSearchOperationMix(int mix, Cache cache, CacheRunnerFactory factory){
		if(null != cache && !cache.isSearchable()){
			log.error("The cache " + cache.getName() + " is not searchable...not adding this operation mix");
			return;
		}
		
		if((totalMix+=mix) > 100)
			throw new IllegalArgumentException("The total mix is higher than 100%. Please check the mix values.");
		
		((RamdomMixRunnerFactory)getRunnerFactory()).addOperationMix(factory.create(), mix);
	}
}