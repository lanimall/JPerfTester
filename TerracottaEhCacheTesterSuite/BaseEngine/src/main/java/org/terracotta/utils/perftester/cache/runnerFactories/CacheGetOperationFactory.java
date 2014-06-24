package org.terracotta.utils.perftester.cache.runnerFactories;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.loader.CacheLoader;

import org.terracotta.utils.perftester.cache.runners.CacheGetOperation;
import org.terracotta.utils.perftester.cache.runners.CacheGetWithLoaderOperation;
import org.terracotta.utils.perftester.conditions.impl.IterationCondition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.runners.impl.KeyRunner;

public class CacheGetOperationFactory extends CacheRunnerFactory {
	private final ObjectGenerator keyGen;
	private CacheLoader loader = null;
	private Object loaderArgument = null;

	public CacheGetOperationFactory(Ehcache cache, long numOperations, ObjectGenerator keyGen) {
		super(cache, numOperations);
		this.keyGen = keyGen;
	}

	public CacheLoader getLoader() {
		return loader;
	}

	public void setLoader(CacheLoader loader) {
		this.loader = loader;
	}

	public Object getLoaderArgument() {
		return loaderArgument;
	}

	public void setLoaderArgument(Object loaderArgument) {
		this.loaderArgument = loaderArgument;
	}

	@Override
	public KeyRunner create() {
		if(null == keyGen)
			throw new IllegalArgumentException("KenGen object may not be null");

		KeyRunner cacheGet;
		if(null != loader || (null != getCache() && null != getCache().getRegisteredCacheLoaders())) {
			cacheGet = new CacheGetWithLoaderOperation(getCache(), new IterationCondition(getNumOperations()), keyGen, loader, loaderArgument);
		} else {
			cacheGet = new CacheGetOperation(getCache(), new IterationCondition(getNumOperations()), keyGen);
		}
		
		return cacheGet;
	}
}