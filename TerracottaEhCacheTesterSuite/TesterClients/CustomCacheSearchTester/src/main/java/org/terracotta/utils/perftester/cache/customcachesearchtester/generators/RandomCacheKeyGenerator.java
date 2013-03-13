package org.terracotta.utils.perftester.cache.customcachesearchtester.generators;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.cache.customcachesearchtester.domain.ComplexCacheKey;
import org.terracotta.utils.perftester.generators.impl.RandomGenerator;

public class RandomCacheKeyGenerator extends RandomGenerator<ComplexCacheKey> {
	private static Logger log = LoggerFactory.getLogger(RandomCacheKeyGenerator.class);

	public static final String URL_PREFIX = "http://some.url.com/rest/something/long/in/the/url/";
	
	private AtomicLong counter = new AtomicLong(0);

	@Override
	protected ComplexCacheKey generateSafe() throws Exception {
		ComplexCacheKey cacheKey = new ComplexCacheKey();
		cacheKey.setqStr(generateRandomUrlQueryString());
		cacheKey.setaOid(generateRandomAccountId());
		cacheKey.setnOid(generateRandomNodeId());
		cacheKey.setuOid(generateRandomUserId());
		cacheKey.setiOid(generateRandomInternalAccountId());
		cacheKey.setcUrl(generateRandomUrl(cacheKey.getnOid(), cacheKey.getaOid(), cacheKey.getuOid()));
		
		if(log.isDebugEnabled())
			log.debug(cacheKey.toString());
		
		return cacheKey;
	}

	//something unique
	public String generateRandomUrl(String nodeID, String accountId, String userId){
		return URL_PREFIX + nodeID + "/" + accountId + "/" + userId + "/" + counter.getAndIncrement();
	}
	
	public String generateRandomUrlSearch(String nodeID, String accountId, String userId){
		StringBuffer sb = new StringBuffer();
		sb.append(URL_PREFIX);
		if(null != nodeID){
			sb.append(nodeID);
			if(null != accountId || null != userId){
				sb.append("/");
				sb.append((null != accountId)?accountId:"*");
				if(null != userId){
					sb.append("/");
					sb.append(userId);
				} else {
					sb.append("/");
					sb.append("*");
				}
			} else {
				sb.append("/");
				sb.append("*");
			}
		} else {
			sb.append("*");
		}
		
		if(log.isDebugEnabled())
			log.debug("RandomUrlSearch: " + sb.toString());
		
		return sb.toString();
	}
	
	public String generateRandomUrlQueryString() throws Exception{
		return randomUtil.generateRandomText(30);
	}

	public String generateRandomNodeId() throws Exception{
		return String.format("%09d", new Integer(randomUtil.generateRandomInt(1, 10, true)));
	}

	public String generateRandomAccountId() throws Exception{
		return String.format("%09d", new Integer(randomUtil.generateRandomInt(1, 100, true)));
	}
	
	public String generateRandomUserId() throws Exception{
		return String.format("%09d", new Integer(randomUtil.generateRandomInt(1, 10000, true)));
	}
	
	public String generateRandomInternalAccountId() throws Exception{
		return String.format("%09d", new Integer(randomUtil.generateRandomInt(1, 100000, true)));
	}
}