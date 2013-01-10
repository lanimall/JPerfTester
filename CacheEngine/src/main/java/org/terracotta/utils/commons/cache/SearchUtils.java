package org.terracotta.utils.commons.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.expression.Criteria;

/**
 * @author Fabien Sanglier
 * 
 */
public class SearchUtils {

	public static Query buildSearchTextQuery(Cache cache, Attribute[] attributes, String[] searchText, boolean isOrOperator, int maxResults){
		return buildSearchTextQuery(cache, attributes, searchText, isOrOperator, maxResults, true);
	}
	
	public static Query buildSearchTextQuery(Cache cache, Attribute[] attributes, String[] searchText, boolean isOrOperator, int maxResults, boolean finalize){
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
		//query.includeValues();

		if(maxResults > 0){
			query.maxResults(maxResults);
		}

		if(finalize){
			query.end();
		}

		return query;
	}
}
