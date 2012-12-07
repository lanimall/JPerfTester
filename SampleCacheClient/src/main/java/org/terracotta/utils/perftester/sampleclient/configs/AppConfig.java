package org.terracotta.utils.perftester.sampleclient.configs;

import java.text.NumberFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.PropertyUtils;

public class AppConfig {
	private static Logger log = LoggerFactory.getLogger(AppConfig.class);
	private static final NumberFormat nf = NumberFormat.getInstance();

	public static final String CACHEPOUNDER_CONFIGPATH_DEFAULT = "app.properties";
	public static final String CACHEPOUNDER_CONFIGPATH_ENVPROP = "app.config.path";
	
	private static final String CACHEPOUNDER_MULTICLIENTS_ENABLESYNC = "cachepounder.multiclients.enablesync";
	private static final String CACHEPOUNDER_NBCLIENTS = "cachepounder.multiclients";
	
	private static final String CACHEPOUNDER_OBJLOADER_NBOBJECTS = "cachepounder.objectloader.numberofobjects";
	private static final String CACHEPOUNDER_OBJLOADER_NBTHREADS = "cachepounder.objectloader.threads";
	private static final String CACHEPOUNDER_OBJLOADER_BULKLOAD = "cachepounder.objectloader.bulkload";
	private static final String CACHEPOUNDER_OBJLOADER_REMOVEALL = "cachepounder.objectloader.removeallfirst";
	private static final String CACHEPOUNDER_OBJLOADER_KEYGENSTART = "cachepounder.objectloader.keystart";

	private static final String CACHEPOUNDER_WARMUP_NBOBJECTS = "cachepounder.warmup.numberofobjects";
	private static final String CACHEPOUNDER_WARMUP_NBTHREADS = "cachepounder.warmup.threads";
	private static final String CACHEPOUNDER_WARMUP_KEYGENSTART = "cachepounder.warmup.keystart";

	private static final String CACHEPOUNDER_TX_NBOBJECTS = "cachepounder.txloader.numberofoperations";
	private static final String CACHEPOUNDER_TX_NBTHREADS = "cachepounder.txloader.threads";
	private static final String CACHEPOUNDER_TX_KEYGEN_RANDOM_DIGITS = "cachepounder.txloader.keygen.randomdigits";
	private static final String CACHEPOUNDER_TX_KEYGEN_RANDOMPREPENDDIGITS = "cachepounder.txloader.keygen.randomdigits.prepend";
	private static final String CACHEPOUNDER_TX_KEYGEN_RANDOMAPPENDDIGITS = "cachepounder.txloader.keygen.randomdigits.append";
	private static final String CACHEPOUNDER_TX_DOPRELOAD = "cachepounder.txloader.preloadobjects";
	private static final String CACHEPOUNDER_TX_DOWARMUP = "cachepounder.txloader.performwarmup";

	//singleton instance
	private static AppConfig instance;

	private final boolean appMultiClientsSyncEnabled;
	private final int appNbClients;
	
	//loader props
	private final int cacheLoaderThreads;
	private final long cacheLoaderNbObjects;
	private final long cacheLoaderKeyStart;
	private final boolean cacheLoaderRemoveAll;
	private final boolean cacheLoaderDoBulkLoad;

	//fetcher props
	private final int cacheFetcherThreads;
	private final long cacheFetcherNbObjects;
	private final long cacheFetcherKeyStart;

	//transactions props
	private final int cacheTxThreads;
	private final long cacheTxNbOperations;
	private final int cacheTxKeyRandomDigitLength;
	private final int cacheTxKeyPrependDigits;
	private final int cacheTxKeyAppendDigits;
	private final boolean cacheTxDoWarmup;
	private final boolean cacheTxDoPreLoad;

	private AppConfig(String propertyFile) throws Exception {
		PropertyUtils propWrapper = new PropertyUtils(propertyFile);

		appMultiClientsSyncEnabled = propWrapper.getPropertyAsBoolean(CACHEPOUNDER_MULTICLIENTS_ENABLESYNC, false);
		appNbClients = propWrapper.getPropertyAsInt(CACHEPOUNDER_NBCLIENTS, 1);
		cacheLoaderThreads = propWrapper.getPropertyAsInt(CACHEPOUNDER_OBJLOADER_NBTHREADS, 2);
		cacheLoaderNbObjects = propWrapper.getPropertyAsLong(CACHEPOUNDER_OBJLOADER_NBOBJECTS, 10000);
		cacheLoaderKeyStart = propWrapper.getPropertyAsLong(CACHEPOUNDER_OBJLOADER_KEYGENSTART, 0);
		cacheLoaderRemoveAll = propWrapper.getPropertyAsBoolean(CACHEPOUNDER_OBJLOADER_REMOVEALL, true);
		cacheLoaderDoBulkLoad = propWrapper.getPropertyAsBoolean(CACHEPOUNDER_OBJLOADER_BULKLOAD, true);

		cacheFetcherThreads = propWrapper.getPropertyAsInt(CACHEPOUNDER_WARMUP_NBTHREADS, 2);
		cacheFetcherNbObjects = propWrapper.getPropertyAsLong(CACHEPOUNDER_WARMUP_NBOBJECTS, 10000);
		cacheFetcherKeyStart = propWrapper.getPropertyAsLong(CACHEPOUNDER_WARMUP_KEYGENSTART, 0);

		cacheTxThreads = propWrapper.getPropertyAsInt(CACHEPOUNDER_TX_NBTHREADS, 2);
		cacheTxNbOperations = propWrapper.getPropertyAsLong(CACHEPOUNDER_TX_NBOBJECTS, 10000);
		cacheTxKeyRandomDigitLength = propWrapper.getPropertyAsInt(CACHEPOUNDER_TX_KEYGEN_RANDOM_DIGITS, 4);
		cacheTxKeyPrependDigits = propWrapper.getPropertyAsInt(CACHEPOUNDER_TX_KEYGEN_RANDOMPREPENDDIGITS, -1);
		cacheTxKeyAppendDigits = propWrapper.getPropertyAsInt(CACHEPOUNDER_TX_KEYGEN_RANDOMAPPENDDIGITS, -1);
		cacheTxDoWarmup = propWrapper.getPropertyAsBoolean(CACHEPOUNDER_TX_DOWARMUP, true);
		cacheTxDoPreLoad = propWrapper.getPropertyAsBoolean(CACHEPOUNDER_TX_DOPRELOAD, true);

		if(log.isDebugEnabled()){
			log.debug("*********** General Application Params *************");
			log.debug("Sync Between Clients: " + appMultiClientsSyncEnabled);
			log.debug("Number of test clients: " + appNbClients);
			
			log.debug("*********** Cache Objects Loader Params *************");
			log.debug("Number of Loader Threads: " + cacheLoaderThreads);
			log.debug("Number of objects to load: " + cacheLoaderNbObjects);
			log.debug("Key number ot start with: " + cacheLoaderKeyStart);
			log.debug("Remove all before reloading: " + cacheLoaderRemoveAll);
			log.debug("Number of operations per thread: " + (cacheLoaderNbObjects/cacheLoaderThreads));

			log.debug("*********** Cache Objects Fetcher Params *************");
			log.debug("Number of Loader Threads: " + cacheFetcherThreads);
			log.debug("Number of objects to fetch: " + cacheFetcherNbObjects);
			log.debug("Key number ot start with: " + cacheFetcherKeyStart);
			log.debug("Number of operations per thread: " + (cacheFetcherNbObjects/cacheFetcherThreads));

			log.debug("*********** Cache Transaction Loader Params *************");
			log.debug("Number of transaction Threads: " + cacheTxThreads);
			log.debug("Number of transactions: " + cacheTxNbOperations);
			log.debug("Number of operations per thread: " + (cacheTxNbOperations/cacheTxThreads));
			log.debug("Key length for the transaction object fetch: " + cacheTxKeyRandomDigitLength);
			log.debug("Perform warmup of cache? " + cacheTxDoWarmup);
			log.debug("Force reloading the cache with objects? " + cacheTxDoPreLoad);
		}
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

	public int getCacheFetcherThreads() {
		return cacheFetcherThreads;
	}

	public long getCacheFetcherNbObjects() {
		return cacheFetcherNbObjects;
	}

	public long getCacheFetcherKeyStart() {
		return cacheFetcherKeyStart;
	}

	public int getCacheTxThreads() {
		return cacheTxThreads;
	}

	public long getCacheTxNbObjects() {
		return cacheTxNbOperations;
	}

	public int getCacheTxKeyRandomDigitLength() {
		return cacheTxKeyRandomDigitLength;
	}

	public int getCacheTxKeyPrependDigits() {
		return cacheTxKeyPrependDigits;
	}

	public int getCacheTxKeyAppendDigits() {
		return cacheTxKeyAppendDigits;
	}

	public boolean isCacheTxDoWarmup() {
		return cacheTxDoWarmup;
	}

	public boolean isCacheTxDoPreLoad() {
		return cacheTxDoPreLoad;
	}

	public static AppConfig getInstance() {
		if (instance == null)
		{
			synchronized(AppConfig.class) {  //1
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

						instance = new AppConfig(location);
					} catch (Exception e) {
						log.error("Could not load the property file", e);
					}
				}
			}
		}
		return instance;
	}
}
