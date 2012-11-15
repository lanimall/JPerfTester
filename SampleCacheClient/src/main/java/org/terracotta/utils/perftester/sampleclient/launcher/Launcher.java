package org.terracotta.utils.perftester.sampleclient.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.cache.launchers.BaseCacheLauncher;
import org.terracotta.utils.perftester.cache.launchers.CachePutLauncher;
import org.terracotta.utils.perftester.cache.launchers.CacheRandomGetLauncher;
import org.terracotta.utils.perftester.cache.launchers.CacheSequentialGetLauncher;
import org.terracotta.utils.perftester.cache.launchers.InteractiveLauncher;
import org.terracotta.utils.perftester.sampleclient.configs.AppConfig;
import org.terracotta.utils.perftester.sampleclient.domain.RandomAddressCategoryGenerator;
import org.terracotta.utils.perftester.sampleclient.domain.RandomAddressGenerator;
import org.terracotta.utils.perftester.sampleclient.domain.RandomCustomerGenerator;

public class Launcher extends InteractiveLauncher {
	private static Logger log = LoggerFactory.getLogger(Launcher.class);
	private static final String CACHE_NAME = "Customers";

	public Launcher(String cacheName) {
		super(cacheName);
	}

	public static void main(String[] args) throws Exception {
		Launcher runner = new Launcher(CACHE_NAME);
		runner.run();
		System.out.println("Completed");
		System.exit(0);
	}

	@Override
	protected void printOptions() {
		System.out.println();
		System.out.println("What do you want to do now?");
		System.out.println("1 - Start Full Test (as defined in properties file)");
		System.out.println("2 - Start Random Gets Only");
		System.out.println("3 - Start Warmup Only");
		System.out.println("4 - Start BulkLoading Only");
		System.out.println("Q - Quit");
	}

	@Override
	protected boolean processInput(String input) throws Exception {
		BaseCacheLauncher cacheLauncher = null;

		switch (input.charAt(0)) {
		case '1':
			processInput("4");
			processInput("3");
			processInput("2");
			break;
		case '2':
			cacheLauncher = new CacheRandomGetLauncher(
					getCache(), 
					AppConfig.getInstance().getCacheTxThreads(), 
					AppConfig.getInstance().getCacheTxNbObjects(), 
					AppConfig.getInstance().getCacheTxKeyRandomDigitLength(), 
					AppConfig.getInstance().getCacheTxKeyPrependDigits(),
					AppConfig.getInstance().getCacheTxKeyAppendDigits());
			break;
		case '3':
			cacheLauncher = new CacheSequentialGetLauncher(
					getCache(),
					AppConfig.getInstance().getCacheFetcherThreads(), 
					AppConfig.getInstance().getCacheFetcherNbObjects(), 
					AppConfig.getInstance().getCacheFetcherKeyStart());
			break;
		case '4':
			cacheLauncher = new CachePutLauncher(
					getCache(),
					AppConfig.getInstance().getCacheLoaderThreads(),
					AppConfig.getInstance().getCacheLoaderNbObjects(), 
					new RandomCustomerGenerator(new RandomAddressGenerator(new RandomAddressCategoryGenerator())),
					AppConfig.getInstance().getCacheLoaderKeyStart(),
					AppConfig.getInstance().isCacheLoaderDoBulkLoad(), 
					AppConfig.getInstance().isCacheLoaderRemoveAll());
			break;
		case 'Q':
			return false;
		}

		if(null != cacheLauncher){
			cacheLauncher.setMultiClientEnabled(AppConfig.getInstance().isAppMultiClientsSyncEnabled());
			cacheLauncher.setNumClients(AppConfig.getInstance().getAppNbClients());
			cacheLauncher.launch();
		}
		
		return true;
	}
}
