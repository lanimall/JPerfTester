package org.terracotta.utils.perftester.cache.runners;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.RandomUtil;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.runners.impl.AbstractRunner;

/**
 * @author Fabien Sanglier
 * 
 */
public abstract class AbstractCacheRunner<T> extends AbstractRunner<T> {
	private static final Logger log = LoggerFactory.getLogger(AbstractCacheRunner.class);
	protected final Cache cache;
	protected final RandomUtil randomUtil = new RandomUtil();
	
	protected AbstractCacheRunner(Cache cache, Condition termination, ObjectGenerator<T> generator) {
		super(termination,generator);
		this.cache = cache;
	}
}