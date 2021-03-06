package org.terracotta.utils.commons.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.expression.Criteria;

/**
 * @author Fabien Sanglier
 * 
 */
public class SearchUtils {

	public static Query buildSearchTextQuery(Cache cache, Attribute[] attributes, String[] searchText, boolean useLikeOperator, boolean isOrOperator, int maxResults){
		return buildSearchTextQuery(cache, attributes, searchText, useLikeOperator, isOrOperator, maxResults, true);
	}

	public static Query buildSearchTextQuery(Cache cache, Attribute[] attributes, String[] searchText, boolean useLikeOperator, boolean isOrOperator, int maxResults, boolean finalize){
		Query  query = cache.createQuery();
		Criteria searchCriteria = null;

		if(null != attributes && null != searchText && attributes.length == searchText.length){
			for(int i = 0; i < attributes.length; i++){
				if(null == searchCriteria) //this is the first time it enters the loop: create the criteria instance
					searchCriteria = attributes[i].ilike(searchText[i]);
				else {  //all other times, it goes here and add the OR
					if(isOrOperator){
						if(useLikeOperator)
							searchCriteria = searchCriteria.or(attributes[i].ilike(searchText[i]));
						else
							searchCriteria = searchCriteria.or(attributes[i].eq(searchText[i]));
					}
					else {
						if(useLikeOperator)
							searchCriteria = searchCriteria.and(attributes[i].ilike(searchText[i]));
						else
							searchCriteria = searchCriteria.and(attributes[i].eq(searchText[i]));
					}
				}
			}
		} else {
			throw new IllegalArgumentException("Arguments are not valid");
		}

		query.addCriteria(searchCriteria);
		query.includeKeys();
		//query.includeValues();

		if(maxResults > 0){
			query.maxResults(maxResults);
		}

		if(finalize){
			query.end();
		}

		return query;
	}

	private static Query buildSearchTextQuery(Cache cache, Attribute[] attributes, String[] searchText, int maxResults, String sortBy, String sortAsc, boolean isOrOperator){
		return buildSearchTextQuery(cache, attributes, searchText, maxResults, (null != sortBy)?new String[]{sortBy}:null, (null != sortAsc)?new String[]{sortAsc}:null, isOrOperator);
	}
	
	private static Query buildSearchTextQuery(Cache cache, Attribute[] attributes, String[] searchText, int maxResults, String[] sortBy, String[] sortAsc, boolean isOrOperator){
		Query  query = cache.createQuery();
		Criteria searchCriteria = null;

		if(null != attributes && null != searchText && attributes.length == searchText.length){
			for(int i = 0; i < attributes.length; i++){
				if(null == searchCriteria) //this is the first time it enters the loop: create the criteria instance
					searchCriteria = attributes[i].ilike(searchText[i]);
				else {  //all other times, it goes here and add the OR
					if(isOrOperator)
						searchCriteria = searchCriteria.or(attributes[i].ilike(searchText[i]));
					else
						searchCriteria = searchCriteria.and(attributes[i].ilike(searchText[i]));
				}
			}
		} else {
			throw new IllegalArgumentException("Arguments are not valid");
		}
		query.addCriteria(searchCriteria);
		query.includeKeys();
		if(null != sortBy){
			for(int i=0;i<sortBy.length;i++){
				//figure out the sort direction for each sort field - defaulting to DESCENDING if nothing is specified
				Direction sortDirection = Direction.DESCENDING;
				if(null != sortAsc && sortAsc.length >= i+1){
					sortDirection = ("Y".equalsIgnoreCase(sortAsc[i]))?Direction.ASCENDING: Direction.DESCENDING;
				}
				
				try{
					Attribute sortAttribute = cache.getSearchAttribute(sortBy[i]);
					query.addOrderBy(
							sortAttribute, 
							sortDirection
							);
				} catch (CacheException exc){
					throw new IllegalArgumentException("Attribute " + sortBy + " is not searchable or does not exist...");
				}
			}
		}
		if(maxResults > 0){
			query.maxResults(maxResults);
		}
		query.end();
		return query;
	}
}
