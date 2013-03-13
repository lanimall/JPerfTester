package org.terracotta.utils.perftester.cache.customcachesearchtester.generators;

import net.sf.ehcache.Cache;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;

import org.terracotta.utils.commons.cache.SearchUtils;
import org.terracotta.utils.perftester.generators.impl.RandomGenerator;

public class SearchQueryGenerator extends RandomGenerator<Query> {
	private final int maxResults;
	private final RandomCacheKeyGenerator queryValuesGenerator;
	private final Cache cache;

	public SearchQueryGenerator(Cache cache, int maxResults, RandomCacheKeyGenerator queryValuesGenerator) {
		super();
		this.cache = cache;
		this.maxResults = maxResults;
		this.queryValuesGenerator = queryValuesGenerator;
	}

	@Override
	protected Query generateSafe() throws Exception {
		return pickRandomWithEquals();
	}

	private Query pickRandomWithEquals() throws Exception{
		Query query = cache.createQuery();
		query.addCriteria(cache.getSearchAttribute("iOid").eq(queryValuesGenerator.generateRandomInternalAccountId()));
		query.includeKeys();
		if(maxResults > 0){
			query.maxResults(maxResults);
		}
		
		query.end();
		return query;
	}
	
	private Query pickRandomWithIlikes() throws Exception{
		Query query = null;
		int pick = randomUtil.generateRandomInt(1, 6, true);
		switch(pick){
		case 1:
			query = SearchUtils.buildSearchTextQuery(
					cache,
					new Attribute[] {cache.getSearchAttribute("cUrl")}, 
					new String[] {queryValuesGenerator.generateRandomUrlSearch(null, null, null)}, 
					false, 
					maxResults);
			query.end();
			break;
		case 2:
			query = SearchUtils.buildSearchTextQuery(
					cache,
					new Attribute[] {cache.getSearchAttribute("cUrl")}, 
					new String[] {queryValuesGenerator.generateRandomUrlSearch(queryValuesGenerator.generateRandomNodeId(), null, null)}, 
					false, 
					maxResults);
			query.end();
			break;
		case 3:
			query = SearchUtils.buildSearchTextQuery(
					cache,
					new Attribute[] {cache.getSearchAttribute("cUrl")}, 
					new String[] {queryValuesGenerator.generateRandomUrlSearch(queryValuesGenerator.generateRandomNodeId(), queryValuesGenerator.generateRandomAccountId(), null)}, 
					false, 
					maxResults);
			query.end();
			break;
		case 4:
			query = SearchUtils.buildSearchTextQuery(
					cache,
					new Attribute[] {cache.getSearchAttribute("cUrl")}, 
					new String[] {queryValuesGenerator.generateRandomUrlSearch(null, null, null)}, 
					false, 
					maxResults,
					false);
			query.addCriteria(cache.getSearchAttribute("nOid").eq(queryValuesGenerator.generateRandomNodeId()));
			query.end();
			break;
		case 5:
			query = SearchUtils.buildSearchTextQuery(
					cache,
					new Attribute[] {cache.getSearchAttribute("cUrl")}, 
					new String[] {queryValuesGenerator.generateRandomUrlSearch(null, null, null)}, 
					false, 
					maxResults,
					false);
			query.addCriteria(cache.getSearchAttribute("aOid").eq(queryValuesGenerator.generateRandomAccountId()));
			query.end();
			break;
		case 6:
			query = SearchUtils.buildSearchTextQuery(
					cache,
					new Attribute[] {cache.getSearchAttribute("cUrl")}, 
					new String[] {queryValuesGenerator.generateRandomUrlSearch(null, null, null)}, 
					false, 
					maxResults,
					false);
			query.addCriteria(cache.getSearchAttribute("uOid").eq(queryValuesGenerator.generateRandomUserId()));
			query.end();
			break;
		default:
			query = null;
			break;
		}

		return query;						
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
