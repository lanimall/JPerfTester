package org.terracotta.utils.perftester.sampleclient.launcher;

import java.util.LinkedList;
import java.util.List;

import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.cache.SearchUtils;
import org.terracotta.utils.perftester.cache.launchers.BaseCacheLauncher;
import org.terracotta.utils.perftester.cache.launchers.CachePutLauncher;
import org.terracotta.utils.perftester.cache.launchers.CacheRandomGetLauncher;
import org.terracotta.utils.perftester.cache.launchers.CacheRandomMixLauncher;
import org.terracotta.utils.perftester.cache.launchers.CacheSequentialGetLauncher;
import org.terracotta.utils.perftester.cache.launchers.InteractiveLauncher;
import org.terracotta.utils.perftester.sampleclient.configs.AppConfig;
import org.terracotta.utils.perftester.sampleclient.domain.RandomAddressCategoryGenerator;
import org.terracotta.utils.perftester.sampleclient.domain.RandomAddressGenerator;
import org.terracotta.utils.perftester.sampleclient.domain.RandomCustomerGenerator;

public class Launcher extends InteractiveLauncher {
	private static Logger log = LoggerFactory.getLogger(Launcher.class);
	public static final String CACHE_NAME = "Customers";

	public static final char LAUNCH_INPUT_FULLTEST1 = '1';
	public static final char LAUNCH_INPUT_FULLTEST2 = '2';
	public static final char LAUNCH_INPUT_BULKLOAD = '3';
	public static final char LAUNCH_INPUT_WARMUP = '4';
	public static final char LAUNCH_INPUT_RDM_GETS = '5';
	public static final char LAUNCH_INPUT_RDMMIX = '6';

	public Launcher(String cacheName) {
		super(cacheName);
	}

	public static void main(String[] args) throws Exception {
		Launcher launcher = new Launcher(CACHE_NAME);

		if(null != args && args.length > 0){
			launcher.processInput(args);
		} else {
			launcher.run();
		}

		System.out.println("Completed");
		System.exit(0);
	}

	@Override
	public void printOptions() {
		System.out.println();
		System.out.println("What do you want to do now?");
		System.out.println(LAUNCH_INPUT_FULLTEST1 + " - Start Full Sequence 1 (Load, Warmup, Random Gets)");
		System.out.println(LAUNCH_INPUT_FULLTEST2 + " - Start Full Sequence 2 (Load, Warmup, Random Mix)");
		System.out.println(LAUNCH_INPUT_BULKLOAD + " - Start BulkLoading Only");
		System.out.println(LAUNCH_INPUT_WARMUP + " - Start Warmup Only");
		System.out.println(LAUNCH_INPUT_RDM_GETS + " - Start Random Gets Only");
		System.out.println(LAUNCH_INPUT_RDMMIX + " - Start Random Mix - 3 Params: [% Gets] [% Puts] [% Search] (default: 60 25 15)");
		System.out.println(LAUNCH_INPUT_QUIT + " - Quit");
	}

	@Override
	public boolean processInput(String input, String[] args) throws Exception {
		BaseCacheLauncher cacheLauncher = null;

		if(null == input || "".equals(input)){
			System.out.println("Unrecognized entry...");
			return true; 
		}
			
		switch (input.charAt(0)) {
		case LAUNCH_INPUT_FULLTEST1:
			processInput(String.valueOf(LAUNCH_INPUT_BULKLOAD));
			processInput(String.valueOf(LAUNCH_INPUT_WARMUP));
			processInput(String.valueOf(LAUNCH_INPUT_RDM_GETS));
			break;
		case LAUNCH_INPUT_FULLTEST2:
			processInput(String.valueOf(LAUNCH_INPUT_BULKLOAD));
			processInput(String.valueOf(LAUNCH_INPUT_WARMUP));
			processInput(String.valueOf(LAUNCH_INPUT_RDMMIX));
			break;
		case LAUNCH_INPUT_BULKLOAD:
			cacheLauncher = new CachePutLauncher(
					getCache(),
					AppConfig.getInstance().getCacheLoaderThreads(),
					AppConfig.getInstance().getCacheLoaderNbObjects(), 
					new RandomCustomerGenerator(new RandomAddressGenerator(new RandomAddressCategoryGenerator())),
					AppConfig.getInstance().getCacheLoaderKeyStart(),
					AppConfig.getInstance().isCacheLoaderDoBulkLoad(), 
					AppConfig.getInstance().isCacheLoaderRemoveAll());
			break;
		case LAUNCH_INPUT_WARMUP:
			cacheLauncher = new CacheSequentialGetLauncher(
					getCache(),
					AppConfig.getInstance().getCacheFetcherThreads(), 
					AppConfig.getInstance().getCacheFetcherNbObjects(), 
					AppConfig.getInstance().getCacheFetcherKeyStart());
			break;
		case LAUNCH_INPUT_RDM_GETS:
			cacheLauncher = new CacheRandomGetLauncher(
					getCache(), 
					AppConfig.getInstance().getCacheTxThreads(), 
					AppConfig.getInstance().getCacheTxNbObjects(), 
					AppConfig.getInstance().getCacheTxKeyRandomDigitLength(), 
					(AppConfig.getInstance().getCacheTxKeyPrependDigits() > -1)?new Integer[] {AppConfig.getInstance().getCacheTxKeyPrependDigits()}:null,
					(AppConfig.getInstance().getCacheTxKeyAppendDigits() > -1)?new Integer[] {AppConfig.getInstance().getCacheTxKeyAppendDigits()}:null
					);
			break;
		case LAUNCH_INPUT_RDMMIX:
			cacheLauncher = new CacheRandomMixLauncher(
					getCache(),
					AppConfig.getInstance().getCacheTxThreads(), 
					AppConfig.getInstance().getCacheTxNbObjects());

			if(null == args){
				args = new String[] {"60", "25", "15"};
			}

			for(int i=0; i<args.length;i++){
				int mix = 0;
				try {
					mix = Integer.parseInt(args[i]);
					if(mix > 100)
						throw new NumberFormatException("Mix value should be 100 or less");

					switch(i){
					case 0:
						((CacheRandomMixLauncher)cacheLauncher).addCacheGetOperationMix(mix, getCache(), AppConfig.getInstance().getCacheTxKeyRandomDigitLength(), (AppConfig.getInstance().getCacheTxKeyPrependDigits() > -1)?new Integer[] {AppConfig.getInstance().getCacheTxKeyPrependDigits()}:null, (AppConfig.getInstance().getCacheTxKeyAppendDigits() > -1)?new Integer[] {AppConfig.getInstance().getCacheTxKeyAppendDigits()}:null);
						break;
					case 1:
						((CacheRandomMixLauncher)cacheLauncher).addCachePutOperationMix(mix, getCache(), new RandomCustomerGenerator(new RandomAddressGenerator(new RandomAddressCategoryGenerator())), AppConfig.getInstance().getCacheLoaderKeyStart());
						break;
					case 2:
						int maxResults = 50;
						List<Query> queries = new LinkedList<Query>();
						queries.add(SearchUtils.buildSearchTextQuery(
								getCache(),
								new Attribute[] {getCache().getSearchAttribute("firstName")}, 
								new String[] {"Sar*"}, 
								false, 
								maxResults));

						queries.add(SearchUtils.buildSearchTextQuery(
								getCache(),
								new Attribute[] {getCache().getSearchAttribute("firstName"), getCache().getSearchAttribute("lastName")}, 
								new String[] {"Sar*","Carls*"}, 
								false,
								maxResults));

						((CacheRandomMixLauncher)cacheLauncher).addCacheSearchOperationMix(mix, getCache(), queries.toArray(new Query[queries.size()]));
						break;
					}
				} catch (Exception e) {
					log.error("Could not add the operation to the ramdomMix", e);
				}
			}
			break;
		case LAUNCH_INPUT_QUIT:
			return false;
		default:
			System.out.println("Unrecognized entry...");
			return true;
		}

		if(null != cacheLauncher){
			cacheLauncher.setMultiClientEnabled(AppConfig.getInstance().isAppMultiClientsSyncEnabled());
			cacheLauncher.setNumClients(AppConfig.getInstance().getAppNbClients());
			cacheLauncher.launch();
		}

		return true;
	}
}
