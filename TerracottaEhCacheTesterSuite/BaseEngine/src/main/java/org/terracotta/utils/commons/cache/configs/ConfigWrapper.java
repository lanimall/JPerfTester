package org.terracotta.utils.commons.cache.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.GlobalConfigSingleton;

public class ConfigWrapper {
	private static Logger log = LoggerFactory.getLogger(ConfigWrapper.class);

	private static final String CACHEPOUNDER_CACHENAME = "jperftester.cache.terracotta.cachename";
	private static final String CACHEPOUNDER_MULTICLIENTS_ENABLESYNC = "jperftester.cache.terracotta.multiclientsync.enable";
	private static final String CACHEPOUNDER_NBCLIENTS = "jperftester.cache.terracotta.multiclientsync.numberofclients";

	private static final String CACHEPOUNDER_OBJLOADER_NBOBJECTS = "jperftester.cache.terracotta.objectload.numberofobjects";
	private static final String CACHEPOUNDER_OBJLOADER_NBTHREADS = "jperftester.cache.terracotta.objectload.numberofthreads";
	private static final String CACHEPOUNDER_OBJLOADER_BULKLOAD = "jperftester.cache.terracotta.objectload.bulkload.enable";
	private static final String CACHEPOUNDER_OBJLOADER_REMOVEALL = "jperftester.cache.terracotta.objectload.startwithremoveall.enable";
	//TODO we should find a nicer way to deal with this...in case the key is actually something non-sequential...
	private static final String CACHEPOUNDER_OBJLOADER_KEYGENSTART = "jperftester.cache.terracotta.objectload.keystart";

	private static final String CACHEPOUNDER_WARMUP_NBOBJECTS = "jperftester.cache.terracotta.warmup.numberofobjects";
	private static final String CACHEPOUNDER_WARMUP_NBTHREADS = "jperftester.cache.terracotta.warmup.numberofthreads";
	//TODO we should find a nicer way to deal with this...in case the key is actually something non-sequential...
	private static final String CACHEPOUNDER_WARMUP_KEYGENSTART = "jperftester.cache.terracotta.warmup.keystart";

	private static final String CACHEPOUNDER_TX_NBOBJECTS = "jperftester.cache.terracotta.steadystate.numberofoperations";
	private static final String CACHEPOUNDER_TX_NBTHREADS = "jperftester.cache.terracotta.steadystate.numberofthreads";
	//TODO we should find a nicer way to deal with this...in case the key is actually something non-sequential...
	private static final String CACHEPOUNDER_TX_KEYGEN_MINVALUE = "jperftester.cache.terracotta.steadystate.keygen.minvalue";
	private static final String CACHEPOUNDER_TX_KEYGEN_MAXVALUE = "jperftester.cache.terracotta.steadystate.keygen.maxvalue";

	//various object generator factories
	private static final String CACHEPOUNDER_OBJLOADER_KEYGENFACTORY = "jperftester.cache.terracotta.objectload.keygenfactory";
	private static final String CACHEPOUNDER_OBJLOADER_VALUEGENFACTORY = "jperftester.cache.terracotta.objectload.valuegenfactory";
	private static final String CACHEPOUNDER_SEARCH_QUERYGENFACTORY = "jperftester.cache.terracotta.objectload.querygenfactory";

	static {
		if(log.isDebugEnabled()){
			log.debug("*********** General Application Params *************");
			log.debug("Cache name: " + ConfigWrapper.getCacheName());
			log.debug("Sync Between Clients: " + ConfigWrapper.isAppMultiClientsSyncEnabled());
			log.debug("Number of test clients: " + ConfigWrapper.getAppNbClients());

			log.debug("*********** Cache Objects Load Params *************");
			log.debug("Number of Loader Threads: " + ConfigWrapper.getCacheLoaderThreads());
			log.debug("Number of objects to load: " + ConfigWrapper.getCacheLoaderNbObjects());
			log.debug("Key generator factory: " + ConfigWrapper.getCacheLoaderKeyGenFactory());
			log.debug("Key number ot start with: " + ConfigWrapper.getCacheLoaderKeyStart());
			log.debug("Value generator factory: " + ConfigWrapper.getCacheLoaderValueGenFactory());
			log.debug("Put cache in bulk mode during loading operation: " + ConfigWrapper.isCacheLoaderDoBulkLoad());
			log.debug("Remove all before reloading: " + ConfigWrapper.isCacheLoaderRemoveAll());
			log.debug("Number of operations per thread: " + (ConfigWrapper.getCacheLoaderNbObjects()/ConfigWrapper.getCacheLoaderThreads()));

			log.debug("*********** Cache Warmup Params *************");
			log.debug("Number of Loader Threads: " + ConfigWrapper.getCacheWarmerThreads());
			log.debug("Number of objects to fetch: " + ConfigWrapper.getCacheWarmerNbObjects());
			log.debug("Key number to start with: " + ConfigWrapper.getCacheWarmerKeyStart());
			log.debug("Number of operations per thread: " + (ConfigWrapper.getCacheWarmerNbObjects()/ConfigWrapper.getCacheWarmerThreads()));

			log.debug("*********** Cache Steady State Params *************");
			log.debug("Number of transaction Threads: " + ConfigWrapper.getCacheSteadyStateThreads());
			log.debug("Number of transactions: " + ConfigWrapper.getCacheSteadyStateNbOperations());
			log.debug("Number of operations per thread: " + (ConfigWrapper.getCacheSteadyStateNbOperations()/ConfigWrapper.getCacheSteadyStateThreads()));
			log.debug("Key number between " + ConfigWrapper.getCacheSteadyStateKeyMinValue() + " and " + ConfigWrapper.getCacheSteadyStateKeyMaxValue());

			log.debug("*********** Cache Object Generation Factories *************");
			log.debug("Search query generator factory: " + ConfigWrapper.getCacheSearchQueryGenFactory());
		}
	}

	public static String getCacheName() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getProperty(CACHEPOUNDER_CACHENAME);
	}
	
	public static boolean isAppMultiClientsSyncEnabled() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsBoolean(CACHEPOUNDER_MULTICLIENTS_ENABLESYNC) && getAppNbClients() > 1;
	}

	public static int getAppNbClients() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsInt(CACHEPOUNDER_NBCLIENTS, 1);
	}

	public static int getCacheLoaderThreads() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsInt(CACHEPOUNDER_OBJLOADER_NBTHREADS, 2);
	}

	public static long getCacheLoaderNbObjects() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsLong(CACHEPOUNDER_OBJLOADER_NBOBJECTS, 10000);
	}

	public static long getCacheLoaderKeyStart() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsLong(CACHEPOUNDER_OBJLOADER_KEYGENSTART, 0);
	}

	public static boolean isCacheLoaderRemoveAll() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsBoolean(CACHEPOUNDER_OBJLOADER_REMOVEALL, true);
	}

	public static boolean isCacheLoaderDoBulkLoad() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsBoolean(CACHEPOUNDER_OBJLOADER_BULKLOAD, true);
	}

	public static String getCacheLoaderKeyGenFactory() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getProperty(CACHEPOUNDER_OBJLOADER_KEYGENFACTORY, "");
	}

	public static String getCacheLoaderValueGenFactory() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getProperty(CACHEPOUNDER_OBJLOADER_VALUEGENFACTORY, "");
	}

	public static int getCacheWarmerThreads() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsInt(CACHEPOUNDER_WARMUP_NBTHREADS, 2);
	}

	public static long getCacheWarmerNbObjects() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsLong(CACHEPOUNDER_WARMUP_NBOBJECTS, 10000);
	}

	public static long getCacheWarmerKeyStart() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsLong(CACHEPOUNDER_WARMUP_KEYGENSTART, 0);
	}

	public static String getCacheSearchQueryGenFactory() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getProperty(CACHEPOUNDER_SEARCH_QUERYGENFACTORY, "");
	}

	public static int getCacheSteadyStateThreads() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsInt(CACHEPOUNDER_TX_NBTHREADS, 2);
	}

	public static long getCacheSteadyStateNbOperations() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsLong(CACHEPOUNDER_TX_NBOBJECTS, 10000);
	}

	public static int getCacheSteadyStateKeyMinValue() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsInt(CACHEPOUNDER_TX_KEYGEN_MINVALUE, 0);
	}

	public static int getCacheSteadyStateKeyMaxValue() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsInt(CACHEPOUNDER_TX_KEYGEN_MAXVALUE, 10000);
	}
}
