package org.terracotta.utils.perftester.cache.generators;

import net.sf.ehcache.search.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.generators.impl.RandomGenerator;

/**
 * @author Fabien Sanglier
 * 
 */
public class RandomSearchQueryGenerator extends RandomGenerator<Query> {
	private static Logger log = LoggerFactory.getLogger(RandomSearchQueryGenerator.class);
	
	private Query[] queries;
	
	public RandomSearchQueryGenerator(Query[] queries) {
		super();
		this.queries = queries;
	}

	@Override
	protected Query generateSafe() throws Exception {
		return randomUtil.getRandomObjectFromArray(queries);
	}
}