package org.terracotta.utils.perftester.cache.runners;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.cache.generators.RandomSearchQueryGenerator;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.conditions.impl.IterationCondition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;

/**
 * @author Fabien Sanglier
 * 
 */
public class CacheSearchOperation extends AbstractCacheRunner<Query> {
	private static Logger log = LoggerFactory.getLogger(CacheSearchOperation.class);
	private static final boolean isDebug = log.isDebugEnabled();
	
	public CacheSearchOperation(Cache cache, Condition termination, ObjectGenerator<Query> keyGenerator) {
		super(cache, termination, keyGenerator);
	}
	
	@Override
	public String getName() {
		return "Cache Search Operation";
	}
	
	@Override
	public void doUnitOfWork(Query query) {
		if(null == query){
			log.info("Query is null...doing nothing");
			return;
		}
			
		if(isDebug)
			log.debug("Search cache with query:" + query);
		
		Results results = query.execute();
		if(isDebug){
			log.debug("==========Results Details=============");
			for (Result result : results.all()) {
				Element entry = cache.get(result.getKey());
				log.debug("Entry " + entry.getKey() + " found is " + entry.getValue());
			}
		}
		results.discard();
	}
	
	public static class CacheSearchOperationFactory extends CacheRunnerFactory {
		private Query[] queries = null;
		public CacheSearchOperationFactory(Cache cache, long numOperations, Query[] queries) {
			super(cache, numOperations);
			this.queries = queries;
		}

		@Override
		public CacheSearchOperation create() {
			if(null == queries)
				return null;
			
			RandomSearchQueryGenerator queryGenerator = new RandomSearchQueryGenerator(queries);
			return new CacheSearchOperation(getCache(), new IterationCondition(getNumOperations()), queryGenerator);	
		}
	}
}
