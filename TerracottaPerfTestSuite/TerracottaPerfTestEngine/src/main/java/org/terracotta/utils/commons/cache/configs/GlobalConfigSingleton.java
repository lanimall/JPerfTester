package org.terracotta.utils.commons.cache.configs;

import java.text.NumberFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.PropertyUtils;

public class GlobalConfigSingleton {
	private static Logger log = LoggerFactory.getLogger(GlobalConfigSingleton.class);
	private static final NumberFormat nf = NumberFormat.getInstance();

	public static final String CACHEPOUNDER_CONFIGPATH_DEFAULT = "app.properties";
	public static final String CACHEPOUNDER_CONFIGPATH_ENVPROP = "app.config.path";
	
	private static final String CACHEPOUNDER_CACHENAME = "jperftester.cache.terracotta.cachename";
	private static final String CACHEPOUNDER_MULTICLIENTS_ENABLESYNC = "jperftester.cache.terracotta.multiclientsync.enable";
	private static final String CACHEPOUNDER_NBCLIENTS = "jperftester.cache.terracotta.multiclientsync.numberofclients";
	
	private static final String CACHEPOUNDER_OBJLOADER_NBOBJECTS = "jperftester.cache.terracotta.objectload.numberofobjects";
	private static final String CACHEPOUNDER_OBJLOADER_NBTHREADS = "jperftester.cache.terracotta.objectload.numberofthreads";
	private static final String CACHEPOUNDER_OBJLOADER_BULKLOAD = "jperftester.cache.terracotta.objectload.bulkload.enable";
	private static final String CACHEPOUNDER_OBJLOADER_REMOVEALL = "jperftester.cache.terracotta.objectload.startwithremoveall.enable";
	private static final String CACHEPOUNDER_OBJLOADER_KEYGENFACTORY = "jperftester.cache.terracotta.objectload.keygenfactory";
	private static final String CACHEPOUNDER_OBJLOADER_VALUEGENFACTORY = "jperftester.cache.terracotta.objectload.valuegenfactory";
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

	//singleton instance
	private static GlobalConfigSingleton instance;

	private final String cacheName;
	private final boolean appMultiClientsSyncEnabled;
	private final int appNbClients;
	
	//loader props
	private final int cacheLoaderThreads;
	private final long cacheLoaderNbObjects;
	private final long cacheLoaderKeyStart;
	private final boolean cacheLoaderRemoveAll;
	private final boolean cacheLoaderDoBulkLoad;
	private final String cacheLoaderKeyGenFactory;
	private final String cacheLoaderValueGenFactory;
	
	//fetcher props
	private final int cacheWarmerThreads;
	private final long cacheWarmerNbObjects;
	private final long cacheWarmerKeyStart;

	//transactions props
	private final int cacheSteadyStateThreads;
	private final long cacheSteadyStateNbOperations;
	private final int cacheSteadyStateKeyMinValue;
	private final int cacheSteadyStateKeyMaxValue;

	//wrapper property
	private final PropertyUtils propWrapper;
	
	private GlobalConfigSingleton(String propertyFile) throws Exception {
		propWrapper = new PropertyUtils(propertyFile);

		cacheName = propWrapper.getProperty(CACHEPOUNDER_CACHENAME, "");
		appMultiClientsSyncEnabled = propWrapper.getPropertyAsBoolean(CACHEPOUNDER_MULTICLIENTS_ENABLESYNC, false);
		appNbClients = propWrapper.getPropertyAsInt(CACHEPOUNDER_NBCLIENTS, 1);
		cacheLoaderThreads = propWrapper.getPropertyAsInt(CACHEPOUNDER_OBJLOADER_NBTHREADS, 2);
		cacheLoaderNbObjects = propWrapper.getPropertyAsLong(CACHEPOUNDER_OBJLOADER_NBOBJECTS, 10000);
		cacheLoaderKeyStart = propWrapper.getPropertyAsLong(CACHEPOUNDER_OBJLOADER_KEYGENSTART, 0);
		cacheLoaderRemoveAll = propWrapper.getPropertyAsBoolean(CACHEPOUNDER_OBJLOADER_REMOVEALL, true);
		cacheLoaderDoBulkLoad = propWrapper.getPropertyAsBoolean(CACHEPOUNDER_OBJLOADER_BULKLOAD, true);
		cacheLoaderKeyGenFactory = propWrapper.getProperty(CACHEPOUNDER_OBJLOADER_KEYGENFACTORY, "");
		cacheLoaderValueGenFactory = propWrapper.getProperty(CACHEPOUNDER_OBJLOADER_VALUEGENFACTORY, "");
		
		cacheWarmerThreads = propWrapper.getPropertyAsInt(CACHEPOUNDER_WARMUP_NBTHREADS, 2);
		cacheWarmerNbObjects = propWrapper.getPropertyAsLong(CACHEPOUNDER_WARMUP_NBOBJECTS, 10000);
		cacheWarmerKeyStart = propWrapper.getPropertyAsLong(CACHEPOUNDER_WARMUP_KEYGENSTART, 0);

		cacheSteadyStateThreads = propWrapper.getPropertyAsInt(CACHEPOUNDER_TX_NBTHREADS, 2);
		cacheSteadyStateNbOperations = propWrapper.getPropertyAsLong(CACHEPOUNDER_TX_NBOBJECTS, 10000);
		cacheSteadyStateKeyMinValue = propWrapper.getPropertyAsInt(CACHEPOUNDER_TX_KEYGEN_MINVALUE, 0);
		cacheSteadyStateKeyMaxValue = propWrapper.getPropertyAsInt(CACHEPOUNDER_TX_KEYGEN_MAXVALUE, 10000);

		if(log.isDebugEnabled()){
			log.debug("*********** General Application Params *************");
			log.debug("Cache name: " + cacheName);
			log.debug("Sync Between Clients: " + appMultiClientsSyncEnabled);
			log.debug("Number of test clients: " + appNbClients);
			
			log.debug("*********** Cache Objects Load Params *************");
			log.debug("Number of Loader Threads: " + cacheLoaderThreads);
			log.debug("Number of objects to load: " + cacheLoaderNbObjects);
			log.debug("Key generator factory: " + cacheLoaderKeyGenFactory);
			log.debug("Key number ot start with: " + cacheLoaderKeyStart);
			log.debug("Value generator factory: " + cacheLoaderValueGenFactory);
			log.debug("Put cache in bulk mode during loading operation: " + cacheLoaderDoBulkLoad);
			log.debug("Remove all before reloading: " + cacheLoaderRemoveAll);
			log.debug("Number of operations per thread: " + (cacheLoaderNbObjects/cacheLoaderThreads));

			log.debug("*********** Cache Warmup Params *************");
			log.debug("Number of Loader Threads: " + cacheWarmerThreads);
			log.debug("Number of objects to fetch: " + cacheWarmerNbObjects);
			log.debug("Key number to start with: " + cacheWarmerKeyStart);
			log.debug("Number of operations per thread: " + (cacheWarmerNbObjects/cacheWarmerThreads));

			log.debug("*********** Cache Steady State Params *************");
			log.debug("Number of transaction Threads: " + cacheSteadyStateThreads);
			log.debug("Number of transactions: " + cacheSteadyStateNbOperations);
			log.debug("Number of operations per thread: " + (cacheSteadyStateNbOperations/cacheSteadyStateThreads));
			log.debug("Key number between " + cacheSteadyStateKeyMinValue + " and " + cacheSteadyStateKeyMaxValue);
		}
	}

	public PropertyUtils getPropWrapper() {
		return propWrapper;
	}

	public String getCacheName() {
		return cacheName;
	}

	public boolean isAppMultiClientsSyncEnabled() {
		return appMultiClientsSyncEnabled && appNbClients > 1;
	}

	public int getAppNbClients() {
		return appNbClients;
	}

	public int getCacheLoaderThreads() {
		return cacheLoaderThreads;
	}

	public long getCacheLoaderNbObjects() {
		return cacheLoaderNbObjects;
	}
	
	public long getCacheLoaderKeyStart() {
		return cacheLoaderKeyStart;
	}

	public boolean isCacheLoaderRemoveAll() {
		return cacheLoaderRemoveAll;
	}

	public boolean isCacheLoaderDoBulkLoad() {
		return cacheLoaderDoBulkLoad;
	}

	public String getCacheLoaderKeyGenFactory() {
		return cacheLoaderKeyGenFactory;
	}

	public String getCacheLoaderValueGenFactory() {
		return cacheLoaderValueGenFactory;
	}

	public int getCacheWarmerThreads() {
		return cacheWarmerThreads;
	}

	public long getCacheWarmerNbObjects() {
		return cacheWarmerNbObjects;
	}

	public long getCacheWarmerKeyStart() {
		return cacheWarmerKeyStart;
	}

	public int getCacheSteadyStateThreads() {
		return cacheSteadyStateThreads;
	}

	public long getCacheSteadyStateNbOperations() {
		return cacheSteadyStateNbOperations;
	}

	public int getCacheSteadyStateKeyMinValue() {
		return cacheSteadyStateKeyMinValue;
	}

	public int getCacheSteadyStateKeyMaxValue() {
		return cacheSteadyStateKeyMaxValue;
	}

	public static GlobalConfigSingleton getInstance() {
		if (instance == null)
		{
			synchronized(GlobalConfigSingleton.class) {  //1
				if (instance == null){
					try {
						String location = "";
						if(null != System.getProperty(CACHEPOUNDER_CONFIGPATH_ENVPROP)){
							location = System.getProperty(CACHEPOUNDER_CONFIGPATH_ENVPROP);
							log.info(CACHEPOUNDER_CONFIGPATH_ENVPROP + " environment property specified: Loading application configuration from " + location);
						}
						else{
							log.info("Loading application configuration from classpath " + CACHEPOUNDER_CONFIGPATH_DEFAULT);
							location = CACHEPOUNDER_CONFIGPATH_DEFAULT;
						}

						instance = new GlobalConfigSingleton(location);
					} catch (Exception e) {
						log.error("Could not load the property file", e);
					}
				}
			}
		}
		return instance;
	}
}
