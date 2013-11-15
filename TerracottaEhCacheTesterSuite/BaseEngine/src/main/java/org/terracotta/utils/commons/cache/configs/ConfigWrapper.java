package org.terracotta.utils.commons.cache.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.GlobalConfigSingleton;

public class ConfigWrapper {
	private static Logger log = LoggerFactory.getLogger(ConfigWrapper.class);

	private static final String CACHEPOUNDER_CACHENAME = "jperftester.cache.terracotta.cachename";
	private static final String CACHEPOUNDER_MULTICLIENTS_ENABLESYNC = "jperftester.cache.terracotta.multiclientsync.enable";
	private static final String CACHEPOUNDER_NBCLIENTS = "jperftester.cache.terracotta.multiclientsync.numberofclients";

	//various object generator factories
	private static final String CACHEPOUNDER_WARMUP_KEYGENFACTORY = "jperftester.cache.terracotta.warmup.keygenfactory";
	private static final String CACHEPOUNDER_WARMUP_VALUEGENFACTORY = "jperftester.cache.terracotta.warmup.valuegenfactory";
	
	private static final String CACHEPOUNDER_STEADY_KEYGENFACTORY = "jperftester.cache.terracotta.steady.keygenfactory";
	private static final String CACHEPOUNDER_STEADY_VALUEGENFACTORY = "jperftester.cache.terracotta.steady.valuegenfactory";
	private static final String CACHEPOUNDER_STEADY_SEARCHQUERYGENFACTORY = "jperftester.cache.terracotta.steady.searchquerygenfactory";

	static {
		if(log.isDebugEnabled()){
			log.debug("*********** General Application Params *************");
			log.debug("Cache name: " + ConfigWrapper.getCacheName());
			log.debug("Sync Between Clients: " + ConfigWrapper.isAppMultiClientsSyncEnabled());
			log.debug("Number of test clients: " + ConfigWrapper.getAppNbClients());

			log.debug("*********** Cache Warmup Params *************");
			log.debug("Key generator factory: " + ConfigWrapper.getCacheWarmupKeyGenFactory());
			log.debug("Value generator factory: " + ConfigWrapper.getCacheWarmupValueGenFactory());

			log.debug("*********** Cache Object Generation Factories for Steady State *************");
			log.debug("Key generator factory: " + ConfigWrapper.getCacheSteadyKeyGenFactory());
			log.debug("Value generator factory: " + ConfigWrapper.getCacheSteadyValueGenFactory());
			log.debug("Search query generator factory: " + ConfigWrapper.getCacheSteadySearchQueryGenFactory());
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

	public static String getCacheWarmupKeyGenFactory() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getProperty(CACHEPOUNDER_WARMUP_KEYGENFACTORY, "");
	}

	public static String getCacheWarmupValueGenFactory() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getProperty(CACHEPOUNDER_WARMUP_VALUEGENFACTORY, "");
	}

	public static String getCacheSteadyKeyGenFactory() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getProperty(CACHEPOUNDER_STEADY_KEYGENFACTORY, "");
	}

	public static String getCacheSteadyValueGenFactory() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getProperty(CACHEPOUNDER_STEADY_VALUEGENFACTORY, "");
	}
	
	public static String getCacheSteadySearchQueryGenFactory() {
		return GlobalConfigSingleton.getInstance().getPropWrapper().getProperty(CACHEPOUNDER_STEADY_SEARCHQUERYGENFACTORY, "");
	}
}
