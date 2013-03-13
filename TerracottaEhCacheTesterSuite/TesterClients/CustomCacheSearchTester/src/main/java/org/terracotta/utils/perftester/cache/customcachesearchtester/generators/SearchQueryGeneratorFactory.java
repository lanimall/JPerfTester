package org.terracotta.utils.perftester.cache.customcachesearchtester.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.cache.CacheUtils;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.ObjectGeneratorFactory;

public class SearchQueryGeneratorFactory implements ObjectGeneratorFactory {
	private static Logger log = LoggerFactory.getLogger(SearchQueryGeneratorFactory.class);

	private static final int DEFAULT_MAXRESULTS = 25;
	
	@SuppressWarnings("rawtypes")
	@Override
	public ObjectGenerator createObjectGenerator() {
		ObjectGenerator objGenerator = null;
		try {
			objGenerator = new SearchQueryGenerator(CacheUtils.getCache(), DEFAULT_MAXRESULTS, new RandomCacheKeyGenerator());
		} catch (Exception e) {
			log.error("", e);
		}
		return objGenerator;
	}
}
