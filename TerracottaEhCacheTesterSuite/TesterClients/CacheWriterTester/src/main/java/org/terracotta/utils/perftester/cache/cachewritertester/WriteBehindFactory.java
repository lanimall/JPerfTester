package org.terracotta.utils.perftester.cache.cachewritertester;

import java.util.Properties;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.CacheWriterFactory;

public class WriteBehindFactory extends CacheWriterFactory {

	public CacheWriter createCacheWriter(Ehcache arg0, Properties arg1) {
		return new WriteBehindConsolePrinter();
	}
}